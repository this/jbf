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

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.nodes.ExecutableNode;
import sa.jbf.language.parser.BFParser;
import sa.jbf.language.runtime.BFNull;

/**
 * Represents the BF language.
 */
@TruffleLanguage.Registration(
        id = BFLanguage.ID,
        name = BFLanguage.NAME,
        defaultMimeType = BFLanguage.MIME_TYPE,
        characterMimeTypes = BFLanguage.MIME_TYPE,
        contextPolicy = TruffleLanguage.ContextPolicy.SHARED,
        fileTypeDetectors = BFFileDetector.class
)
public class BFLanguage extends TruffleLanguage<BFContext> {
    public static final String ID = "bf";
    public static final String NAME = "BrainFuck";
    public static final String MIME_TYPE = "application/x-bf";

    @Override
    protected BFContext createContext(Env env) {
        return new BFContext(env);
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        final var rootNode = new BFParser(this, request.getSource()).parse();
        return Truffle.getRuntime().createCallTarget(rootNode);
    }

    @Override
    protected ExecutableNode parse(InlineParsingRequest request) throws Exception {
        return new BFParser(this, request.getSource()).parse();
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        return (object instanceof BFNull);
    }
}
