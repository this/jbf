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

public class BFRuntimeException extends RuntimeException implements TruffleException {
    private final BFNode location;

    public BFRuntimeException(BFNode location, String message) {
        this(location, message, null);
    }

    public BFRuntimeException(BFNode location, String message, Throwable cause) {
        super(message, cause);
        this.location = location;
    }

    @Override
    public Node getLocation() {
        return location;
    }

    @Override
    public boolean isExit() {
        return true;
    }

    @Override
    public int getExitStatus() {
        return 1;
    }
}
