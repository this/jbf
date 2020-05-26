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

import org.junit.jupiter.api.Test;
import sa.jbf.language.meta.SourcePointer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BFLexerTest {
    @Test
    public void testLex() {
        final var source = "+-><,.[]foo";
        final var tokens = BFLexer.lex(source);
        assertEquals(new BFToken(BFToken.Type.INCREMENT_BYTE, new SourcePointer(0, 1)), tokens.get(0));
        assertEquals(new BFToken(BFToken.Type.DECREMENT_BYTE, new SourcePointer(1, 1)), tokens.get(1));
        assertEquals(new BFToken(BFToken.Type.INCREMENT_POINTER, new SourcePointer(2, 1)), tokens.get(2));
        assertEquals(new BFToken(BFToken.Type.DECREMENT_POINTER, new SourcePointer(3, 1)), tokens.get(3));
        assertEquals(new BFToken(BFToken.Type.INPUT_BYTE, new SourcePointer(4, 1)), tokens.get(4));
        assertEquals(new BFToken(BFToken.Type.OUTPUT_BYTE, new SourcePointer(5, 1)), tokens.get(5));
        assertEquals(new BFToken(BFToken.Type.JUMP_FORWARD, new SourcePointer(6, 1)), tokens.get(6));
        assertEquals(new BFToken(BFToken.Type.JUMP_BACKWARD, new SourcePointer(7, 1)), tokens.get(7));
        assertEquals(new BFToken(BFToken.Type.COMMENT, new SourcePointer(8, 1)), tokens.get(8));
        assertEquals(new BFToken(BFToken.Type.COMMENT, new SourcePointer(9, 1)), tokens.get(9));
        assertEquals(new BFToken(BFToken.Type.COMMENT, new SourcePointer(10, 1)), tokens.get(10));
    }
}
