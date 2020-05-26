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

package sa.jbf.language;

import com.oracle.truffle.api.TruffleLanguage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents the execution context of the {@link BFLanguage BF lanuage}.
 */
public class BFContext {
    private final TruffleLanguage.Env env;

    /**
     * Create new context.
     *
     * @param env language execution environment
     */
    public BFContext(TruffleLanguage.Env env) {
        this.env = env;
    }

    /**
     * Returns an input stream that represents the standard input of the language execution environment.
     *
     * @return the standard in
     */
    public InputStream in() {
        return env.in();
    }

    /**
     * Returns an output stream that represents the standard output of the language execution environment.
     *
     * @return the standard out
     */
    public OutputStream out() {
        return env.out();
    }
}
