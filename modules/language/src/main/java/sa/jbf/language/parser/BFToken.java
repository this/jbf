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

import java.util.Objects;

/**
 * Represents a token of the {@link sa.jbf.language.BFLanguage BF lanuage} in a source.
 */
public class BFToken {
    private final Type type;
    private final SourcePointer sourcePointer;

    /**
     * Creates a new token.
     *
     * @param type          type of the token
     * @param sourcePointer pointer to the source of the token
     */
    public BFToken(Type type, SourcePointer sourcePointer) {
        this.type = type;
        this.sourcePointer = sourcePointer;
    }

    /**
     * Returns the type of this toke.
     *
     * @return token type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the source pointer of this token.
     *
     * @return source pointer
     */
    public SourcePointer getSourcePointer() {
        return sourcePointer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BFToken)) {
            return false;
        }
        var that = (BFToken) o;
        return (type == that.type) && Objects.equals(sourcePointer, that.sourcePointer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, sourcePointer);
    }

    @Override
    public String toString() {
        return String.format("BFToken{type=%s, sourcePointer=%s}", type, sourcePointer);
    }

    /**
     * Represents a token type.
     */
    public enum Type {
        INCREMENT_BYTE,
        DECREMENT_BYTE,
        INCREMENT_POINTER,
        DECREMENT_POINTER,
        INPUT_BYTE,
        OUTPUT_BYTE,
        JUMP_FORWARD,
        JUMP_BACKWARD,
        COMMENT;
    }
}
