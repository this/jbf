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

import sa.jbf.language.meta.SourcePointer;

import java.util.ArrayList;
import java.util.List;

public class BFLexer {
    public static List<BFToken> lex(final CharSequence characters) {
        List<BFToken> tokens = new ArrayList<>(characters.length());
        for (int i = 0; i < characters.length(); i++) {
            char symbol = characters.charAt(i);
            var type = getType(symbol);
            tokens.add(new BFToken(type, new SourcePointer(i, 1)));
        }
        return tokens;
    }

    private static BFToken.Type getType(final char symbol) {
        switch (symbol) {
            case '+':
                return BFToken.Type.INCREMENT_BYTE;
            case '-':
                return BFToken.Type.DECREMENT_BYTE;
            case '>':
                return BFToken.Type.INCREMENT_POINTER;
            case '<':
                return BFToken.Type.DECREMENT_POINTER;
            case ',':
                return BFToken.Type.INPUT_BYTE;
            case '.':
                return BFToken.Type.OUTPUT_BYTE;
            case '[':
                return BFToken.Type.JUMP_FORWARD;
            case ']':
                return BFToken.Type.JUMP_BACKWARD;
            default:
                return BFToken.Type.COMMENT;
        }
    }
}
