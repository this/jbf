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

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SamplesTest {
    @ParameterizedTest
    @ValueSource(strings = {"two-plus-five", "hello-world", "yapi", "rot13", "cat", "cell-size"})
    void executeSample(final String testName) throws Exception {
        final var standardIn = getStandardIn(testName);
        final var standardOut = new ByteArrayOutputStream();
        final var standardErr = new ByteArrayOutputStream();

        final var engine = Engine.newBuilder()
                .in(standardIn)
                .out(standardOut)
                .err(standardErr)
                .build();
        final var source = Source.newBuilder(BFLanguage.ID, getSource(testName))
                .cached(false)
                .buildLiteral();
        final var context = Context.newBuilder(BFLanguage.ID)
                .engine(engine)
                .build();
        context.eval(source);

        final var actualOutput = standardOut.toString();
        final var expectedOutput = getExpectedOut(testName);
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    static File getSource(final String testName) {
        return Path.of(String.format("src/test/resources/%s.bf", testName)).toFile();
    }

    static InputStream getStandardIn(final String testName) throws IOException {
        final var inputFile = Path.of(String.format("src/test/resources/%s.in", testName));
        return Files.exists(inputFile) ? Files.newInputStream(inputFile) : InputStream.nullInputStream();
    }

    static String getExpectedOut(final String testName) throws IOException {
        final var outputFile = Path.of(String.format("src/test/resources/%s.out", testName));
        return Files.exists(outputFile) ? Files.readString(outputFile) : "";
    }
}
