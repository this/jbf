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

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import sa.jbf.language.BFLanguage;
import sa.jbf.language.meta.SourcePointer;

import java.util.List;

@NodeInfo(language = BFLanguage.ID, shortName = "[]", description = "Loop")
public class BFLoopNode extends BFNode {
    @Children
    private final BFNode[] children;

    public BFLoopNode(final List<BFNode> children,
                      final FrameSlot pointerSlot,
                      final FrameSlot[] dataSlots,
                      final SourcePointer sourcePointer) {
        super(pointerSlot, dataSlots, sourcePointer);
        this.children = children.toArray(new BFNode[0]);
    }

    @Override
    public void execute(final VirtualFrame frame) {
        if (getCurrentByte(frame) == 0) {
            return;
        }

        do {
            for (var child : children) {
                child.execute(frame);
            }
        } while (getCurrentByte(frame) != 0);
    }
}
