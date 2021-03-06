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

import com.oracle.truffle.api.TruffleException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

/**
 * Thrown to indicate an anomaly detected during parsing a source of {@link sa.jbf.language.BFLanguage BF language}.
 */
public class ParserException extends RuntimeException implements TruffleException {
    private final BFToken token;
    private final Source source;

    /**
     * Creates a new exception.
     *
     * @param token   token where the exception occurred
     * @param message the detailed message
     * @param source  source code
     */
    public ParserException(BFToken token, String message, Source source) {
        super(message);
        this.token = token;
        this.source = source;
    }

    /**
     * {@inheritDoc}
     *
     * @return always {@code null}
     */
    @Override
    public Node getLocation() {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @return always {@code true}
     */
    @Override
    public boolean isSyntaxError() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return always {@code true}
     */
    @Override
    public boolean isCancelled() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return always {@code true}
     */
    @Override
    public boolean isExit() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return always {@code 1}
     */
    @Override
    public int getExitStatus() {
        return 1;
    }

    @Override
    public SourceSection getSourceLocation() {
        return source.createSection(token.getSourcePointer().getStartIndex(), token.getSourcePointer().getLength());
    }
}
