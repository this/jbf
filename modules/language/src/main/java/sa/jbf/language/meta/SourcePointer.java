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

package sa.jbf.language.meta;

import java.util.Objects;

/**
 * Represents a pointer that points to a section in a source code of a {@link sa.jbf.language.BFLanguage BF language}
 * program.
 */
public class SourcePointer {
    private final int startIndex;
    private final int length;

    /**
     * Creates a new pointer to a source section.
     *
     * @param startIndex 0-based position of the first character in the pointing section
     * @param length     the number of characters in the pointing section
     */
    public SourcePointer(int startIndex, int length) {
        this.startIndex = startIndex;
        this.length = length;
    }

    /**
     * Returns the 0-based index of the first character in the pointed section.
     *
     * @return the starting character index
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Returns the length of the pointed section in characters.
     *
     * @return the number of characters in the pointed section
     */
    public int getLength() {
        return length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIndex, length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourcePointer)) {
            return false;
        }
        var that = (SourcePointer) o;
        return (startIndex == that.startIndex) && (length == that.length);
    }

    @Override
    public String toString() {
        return String.format("SourcePointer{startIndex=%d, length=%d}", startIndex, length);
    }
}
