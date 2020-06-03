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
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Source;
import sa.jbf.language.BFLanguage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * Launcher for {@link BFLanguage BF language}.
 */
public class BFLauncher extends AbstractLanguageLauncher {
    private static final int EXIT_NO_SOURCE = -1;
    private final Runtime runtime;
    private Path sourceFile = null;

    public static void main(String[] args) {
        new BFLauncher().doLaunch(args);
    }

    /**
     * Creates a new launcher that uses the {@link Runtime#getRuntime()} to interact with the application environment.
     */
    public BFLauncher() {
        this(Runtime.getRuntime());
    }

    /**
     * Creates a new launcher that uses the specified runtime to interact with the application environment.
     *
     * @param runtime launcher runtime
     */
    BFLauncher(Runtime runtime) {
        this.runtime = runtime;
    }

    /**
     * Wraps {@link AbstractLanguageLauncher#launch(String[])}, hence should be called from the {@link #main(String[])} method.
     *
     * @param args the command line arguments.
     */
    void doLaunch(String[] args) {
        super.launch(args);
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
        runtime.exit(executeSource(contextBuilder.build()));
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
        } catch (PolyglotException e) {
            prettyPrint(e);
            return e.getExitStatus();
        } catch (IOException e) {
            throw abort(e, EXIT_NO_SOURCE);
        }
    }

    private static void prettyPrint(PolyglotException e) {
        final var sourceSection = e.getSourceLocation();
        final var source = sourceSection.getSource();

        System.err.println(format("[ERROR] %s:[%d,%d] %s",
                source.getName(),
                sourceSection.getStartLine(),
                sourceSection.getStartColumn(),
                e.getMessage()));
        System.err.println(format("        %s", source.getCharacters(sourceSection.getStartLine()).toString()));
        System.err.println(format("        %s^", " ".repeat(sourceSection.getStartColumn() - 1)));
    }
}
