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

package sa.jbf.launcher;

import org.graalvm.launcher.AbstractLanguageLauncher;
import org.graalvm.options.OptionCategory;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import sa.jbf.language.BFLanguage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Launcher for {@link BFLanguage BF language}.
 */
public class BFLauncher extends AbstractLanguageLauncher {
    protected static final int EXIT_NO_SOURCE = -1;

    private Path sourceFile = null;

    public static void main(String[] args) {
        new BFLauncher().launch(args);
    }

    @Override
    protected List<String> preprocessArguments(List<String> arguments, Map<String, String> polyglotOptions) {
        if (arguments.size() < 1) {
            abort("No BF file to execute", EXIT_NO_SOURCE);
        }
        final var sourceFilePath = arguments.remove(0);
        sourceFile = Path.of(sourceFilePath);
        if (Files.notExists(sourceFile)) {
            abortInvalidArgument(sourceFilePath, "No BF file to execute", EXIT_NO_SOURCE);
        }
        return arguments;
    }

    @Override
    protected void launch(final Context.Builder contextBuilder) {
        System.exit(executeSource(contextBuilder.build()));
    }

    @Override
    protected String getLanguageId() {
        return BFLanguage.ID;
    }

    @Override
    protected void printHelp(final OptionCategory maxCategory) {
    }

    private int executeSource(final Context context) {
        try {
            final var source = Source.newBuilder(BFLanguage.ID, sourceFile.toFile()).build();
            context.eval(source);
            return 0;
        } catch (IOException e) {
            throw abort(e, EXIT_NO_SOURCE);
        }
    }
}
