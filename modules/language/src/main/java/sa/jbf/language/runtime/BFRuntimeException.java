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

package sa.jbf.language.runtime;

import com.oracle.truffle.api.TruffleException;
import com.oracle.truffle.api.nodes.Node;
import sa.jbf.language.ast.BFNode;

/**
 * Super class of all exceptions that can occur in {@link sa.jbf.language.BFLanguage BF language}.
 */
public class BFRuntimeException extends RuntimeException implements TruffleException {
    private final BFNode location;

    /**
     * Constructs a new exception with the specified location and detail message.
     *
     * @param location node indicating the location where the exception occurred in the AST
     * @param message  the detail message
     */
    public BFRuntimeException(BFNode location, String message) {
        this(location, message, null);
    }

    /**
     * Constructs a new exception with the specified location, detail message, and cause.
     *
     * @param location node indicating the location where the exception occurred in the AST
     * @param message  the detail message
     * @param cause    the cause
     */
    public BFRuntimeException(BFNode location, String message, Throwable cause) {
        super(message, cause);
        this.location = location;
    }

    @Override
    public Node getLocation() {
        return location;
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
}
