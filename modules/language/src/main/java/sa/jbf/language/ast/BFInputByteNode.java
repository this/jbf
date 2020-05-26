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

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import sa.jbf.language.BFLanguage;
import sa.jbf.language.meta.SourcePointer;
import sa.jbf.language.runtime.BFIOException;

import java.io.IOException;
import java.io.InputStream;

@NodeInfo(language = BFLanguage.ID, shortName = ",", description = "Input byte command")
public class BFInputByteNode extends BFNode {
    public BFInputByteNode(final FrameSlot pointerSlot, final FrameSlot[] dataSlots, SourcePointer sourcePointer) {
        super(pointerSlot, dataSlots, sourcePointer);
    }

    @Override
    public void execute(final VirtualFrame frame) {
        setCurrentByte(frame, doRead(getContext().in()));
    }

    @TruffleBoundary
    private byte doRead(final InputStream in) {
        try {
            return (byte) in.read();
        } catch (IOException e) {
            throw new BFIOException(this, "Cannot read next byte from standard input", e);
        }
    }
}
