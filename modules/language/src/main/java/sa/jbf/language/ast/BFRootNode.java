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

package sa.jbf.language.ast;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;
import sa.jbf.language.BFLanguage;
import sa.jbf.language.runtime.BFNull;

import java.util.List;

@NodeInfo(language = BFLanguage.ID, description = "Root node")
public class BFRootNode extends RootNode {
    @Children
    private final BFNode[] children;
    private final SourceSection sourceSection;

    public BFRootNode(final BFLanguage language,
                      final FrameDescriptor descriptor,
                      final List<BFNode> children,
                      final SourceSection sourceSection) {
        super(language, descriptor);
        this.children = children.toArray(new BFNode[0]);
        this.sourceSection = sourceSection;
    }

    @Override
    public Object execute(final VirtualFrame frame) {
        initializeMemorySlots(frame);
        for (var node : children) {
            node.execute(frame);
        }
        return BFNull.INSTANCE;
    }

    @ExplodeLoop
    private void initializeMemorySlots(final VirtualFrame frame) {
        final var descriptor = frame.getFrameDescriptor();
        for (var slot : descriptor.getSlots()) {
            var frameSlotKind = descriptor.getFrameSlotKind(slot);
            if (frameSlotKind == FrameSlotKind.Byte) {
                frame.setByte(slot, (byte) 0);
            } else if (frameSlotKind == FrameSlotKind.Int) {
                frame.setInt(slot, 0);
            }
        }
    }

    @Override
    public SourceSection getSourceSection() {
        return sourceSection;
    }
}
