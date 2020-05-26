/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sa.jbf.language.parser;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.source.Source;
import sa.jbf.language.BFLanguage;
import sa.jbf.language.ast.BFDecrementByteNode;
import sa.jbf.language.ast.BFDecrementPointerNode;
import sa.jbf.language.ast.BFIncrementByteNode;
import sa.jbf.language.ast.BFIncrementPointerNode;
import sa.jbf.language.ast.BFInputByteNode;
import sa.jbf.language.ast.BFLoopNode;
import sa.jbf.language.ast.BFNode;
import sa.jbf.language.ast.BFOutputByteNode;
import sa.jbf.language.ast.BFRootNode;
import sa.jbf.language.meta.SourcePointer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BFParser {
    private static final int FRAME_DEFAULT_VALUE = 0;
    private static final String POINTER_SLOT_IDENTIFIER = "__data_pointer";
    private static final int MEMORY_SIZE = 30000;

    private final BFLanguage language;
    private final Source source;

    public BFParser(BFLanguage language, Source source) {
        this.language = language;
        this.source = source;
    }

    public BFRootNode parse() {
        final var frameDescriptor = new FrameDescriptor(FRAME_DEFAULT_VALUE);
        final var pointerSlot = frameDescriptor.addFrameSlot(POINTER_SLOT_IDENTIFIER, FrameSlotKind.Int);
        final var dataSlots = IntStream.range(0, MEMORY_SIZE)
                .mapToObj(index -> frameDescriptor.addFrameSlot(index, FrameSlotKind.Byte))
                .toArray(FrameSlot[]::new);
        final var tokens = BFLexer.lex(source.getCharacters());
        final var ast = buildAst(tokens, pointerSlot, dataSlots);
        final var sourceSection = source.createSection(tokens.get(0).getSourcePointer().getStartIndex(),
                source.getLength());
        return new BFRootNode(language, frameDescriptor, ast, sourceSection);
    }

    private List<BFNode> buildAst(final List<BFToken> tokens,
                                  final FrameSlot pointerSlot,
                                  final FrameSlot[] dataSlots) {
        final var ast = new ArrayList<BFNode>();
        final var loops = new ArrayDeque<BFLoopNodeBuilder>();

        for (var token : tokens) {
            switch (token.getType()) {
                case COMMENT:
                    continue;
                case JUMP_FORWARD:
                    loops.push(new BFLoopNodeBuilder(pointerSlot, dataSlots, token));
                    continue;
                case JUMP_BACKWARD: {
                    if (loops.isEmpty()) {
                        throw new ParserException(token, "No matching [", source);
                    }
                    var loopNode = loops.pop().closeToken(token).build();
                    if (loops.isEmpty()) {
                        ast.add(loopNode);
                    } else {
                        loops.peek().addChild(loopNode);
                    }
                    continue;
                }
                default:
                    var node = createNode(token, pointerSlot, dataSlots);
                    if (loops.isEmpty()) {
                        ast.add(node);
                    } else {
                        loops.peek().addChild(node);
                    }
            }
        }

        if (!loops.isEmpty()) {
            throw new ParserException(loops.peek().openToken, "No matching ]", source);
        }

        return ast;
    }

    private BFNode createNode(final BFToken token, final FrameSlot pointerSlot, final FrameSlot[] dataSlots) {
        switch (token.getType()) {
            case INCREMENT_BYTE:
                return new BFIncrementByteNode(pointerSlot, dataSlots, token.getSourcePointer());
            case DECREMENT_BYTE:
                return new BFDecrementByteNode(pointerSlot, dataSlots, token.getSourcePointer());
            case INCREMENT_POINTER:
                return new BFIncrementPointerNode(pointerSlot, dataSlots, token.getSourcePointer());
            case DECREMENT_POINTER:
                return new BFDecrementPointerNode(pointerSlot, dataSlots, token.getSourcePointer());
            case INPUT_BYTE:
                return new BFInputByteNode(pointerSlot, dataSlots, token.getSourcePointer());
            case OUTPUT_BYTE:
                return new BFOutputByteNode(pointerSlot, dataSlots, token.getSourcePointer());
            default:
                throw new ParserException(token, "Should not reach", source);
        }
    }

    private static class BFLoopNodeBuilder {
        private final FrameSlot pointerSlot;
        private final FrameSlot[] dataSlots;
        private final BFToken openToken;
        private final List<BFNode> children = new ArrayList<>();
        private BFToken closeToken;

        private BFLoopNodeBuilder(final FrameSlot pointerSlot, final FrameSlot[] dataSlots, final BFToken openToken) {
            this.pointerSlot = pointerSlot;
            this.dataSlots = dataSlots;
            this.openToken = openToken;
        }

        private BFLoopNodeBuilder addChild(BFNode child) {
            this.children.add(child);
            return this;
        }

        private BFLoopNodeBuilder closeToken(BFToken closeToken) {
            this.closeToken = closeToken;
            return this;
        }

        private BFLoopNode build() {
            if (closeToken == null) {
                throw new IllegalStateException("No close token of the loop");
            }
            var sourcePointer = new SourcePointer(openToken.getSourcePointer().getStartIndex(),
                    closeToken.getSourcePointer().getStartIndex() - openToken.getSourcePointer().getStartIndex());
            return new BFLoopNode(children, pointerSlot, dataSlots, sourcePointer);
        }
    }
}
