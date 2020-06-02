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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SamplesTest {
    private final static InputStream originalStandardIn = System.in;
    private final static PrintStream originalStandardOut = System.out;
    private final static PrintStream originalStandardErr = System.err;

    @AfterAll
    static void setupStreams() {
        System.setIn(originalStandardIn);
        System.setOut(originalStandardOut);
        System.setErr(originalStandardErr);
    }

    @ParameterizedTest
    @ValueSource(strings = {"two-plus-five", "hello-world", "yapi", "rot13", "cat", "cell-size"})
    void executeSample(String testName) throws Exception {
        final var standardIn = getStandardIn(testName);
        final var standardOut = new ByteArrayOutputStream();
        final var standardErr = new ByteArrayOutputStream();
        System.setIn(standardIn);
        System.setOut(new PrintStream(standardOut));
        System.setErr(new PrintStream(standardErr));

        final var testFilePath = String.format("src/test/resources/%s.bf", testName);
        catchSystemExit(() -> BFLauncher.main(new String[]{testFilePath}));
        final var actualOutput = standardOut.toString();
        final var expectedOutput = getExpectedOut(testName);
        assertEquals(expectedOutput, actualOutput);
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
