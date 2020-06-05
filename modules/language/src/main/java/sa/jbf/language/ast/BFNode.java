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

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.source.SourceSection;
import sa.jbf.language.BFContext;
import sa.jbf.language.BFLanguage;
import sa.jbf.language.meta.SourcePointer;
import sa.jbf.language.runtime.InvalidPointerException;

@NodeInfo(language = BFLanguage.ID, description = "An AST node in BF language")
public abstract class BFNode extends Node {
    private final FrameSlot pointerSlot;
    private final FrameSlot[] dataSlots;
    private final SourcePointer sourcePointer;

    public BFNode(final FrameSlot pointerSlot, final FrameSlot[] dataSlots, SourcePointer sourcePointer) {
        this.pointerSlot = pointerSlot;
        this.dataSlots = dataSlots;
        this.sourcePointer = sourcePointer;
    }

    public abstract void execute(VirtualFrame frame);

    @Override
    @CompilerDirectives.TruffleBoundary
    public SourceSection getSourceSection() {
        return getRootNode()
                .getSourceSection()
                .getSource()
                .createSection(sourcePointer.getStartIndex(), sourcePointer.getLength());
    }

    FrameSlot getPointerSlot() {
        return pointerSlot;
    }

    FrameSlot[] getDataSlots() {
        return dataSlots;
    }

    protected int getPointer(final VirtualFrame frame) {
        return FrameUtil.getIntSafe(frame, pointerSlot);
    }

    protected void setPointer(final VirtualFrame frame, int newPointer) {
        if (newPointer < 0) {
            throw new InvalidPointerException(this, "Pointer cannot be less than zero");
        } else if (newPointer >= dataSlots.length) {
            throw new InvalidPointerException(this, "Pointer cannot be equal to or greater than " + dataSlots.length);
        }
        frame.setInt(pointerSlot, newPointer);
    }

    protected byte getCurrentByte(final VirtualFrame frame) {
        return FrameUtil.getByteSafe(frame, dataSlots[getPointer(frame)]);
    }

    protected void setCurrentByte(final VirtualFrame frame, byte newByte) {
        frame.setByte(dataSlots[getPointer(frame)], newByte);
    }

    protected BFContext getContext() {
        return lookupContextReference(BFLanguage.class).get();
    }
}
