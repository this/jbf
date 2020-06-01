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

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Test;
import sa.jbf.language.BFLanguage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BFInputByteNodeTest {
    @Test
    void exceptionOnReadTest() throws IOException {
        final var source = ",+[-.,+]"; // from cat.bf
        final var standardIn = mock(InputStream.class);
        when(standardIn.read()).thenThrow(new IOException());
        final var engine = Engine.newBuilder()
                .in(standardIn)
                .out(OutputStream.nullOutputStream())
                .err(OutputStream.nullOutputStream())
                .build();
        final var context = Context.newBuilder(BFLanguage.ID)
                .engine(engine)
                .build();

        final var polyglotException = assertThrows(PolyglotException.class, () -> context.eval(BFLanguage.ID, source));
        assertTrue(polyglotException.isExit());
        assertEquals(1, polyglotException.getExitStatus());
        assertEquals(0, polyglotException.getSourceLocation().getCharIndex());
        assertEquals(1, polyglotException.getSourceLocation().getCharLength());
    }
}