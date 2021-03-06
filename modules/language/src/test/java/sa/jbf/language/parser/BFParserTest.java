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

import com.oracle.truffle.api.source.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sa.jbf.language.BFLanguage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BFParserTest {
    @Test
    void noMatchingJumpForwardTest() {
        String sourceCode = ",.]";
        final var source = Source.newBuilder(BFLanguage.ID, sourceCode, null).build();
        final var parser = new BFParser(new BFLanguage(), source);
        final var parserException = Assertions.assertThrows(ParserException.class, parser::parse);
        assertEquals("No matching [", parserException.getMessage());
        assertTrue(parserException.isSyntaxError());
        assertTrue(parserException.isCancelled());
        assertTrue(parserException.isExit());
        assertEquals(2, parserException.getSourceLocation().getCharIndex());
        assertEquals(1, parserException.getSourceLocation().getCharLength());
    }

    @Test
    void noMatchingJumpBackwardTest() {
        String sourceCode = "[,.";
        final var source = Source.newBuilder(BFLanguage.ID, sourceCode, null).build();
        final var parser = new BFParser(new BFLanguage(), source);
        final var parserException = Assertions.assertThrows(ParserException.class, parser::parse);
        assertEquals("No matching ]", parserException.getMessage());
        assertTrue(parserException.isSyntaxError());
        assertTrue(parserException.isCancelled());
        assertTrue(parserException.isExit());
        assertEquals(0, parserException.getSourceLocation().getCharIndex());
        assertEquals(1, parserException.getSourceLocation().getCharLength());
    }
}
