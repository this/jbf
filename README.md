# jbf - Brainf\*ck on JVM
A Brainf\*ck implementation on top of the JVM, utilizing GraalVM and Truffel framework.

![Build Bot](https://github.com/this/jbf/workflows/Build%20Bot/badge.svg?branch=master&event=push)

[Brainf\*ck](https://en.wikipedia.org/wiki/Brainfuck) is a turing complete [esoteric programming language](https://en.wikipedia.org/wiki/Esoteric_programming_language) that is famous for its extreme minimalism. This project implements Brainf\*ck language using [Truffel framework](https://github.com/oracle/graal/tree/master/truffle) for the [GraalVM](https://www.graalvm.org/)

## Getting started
### Prerequisites
- [Maven 3.x](https://maven.apache.org/download.cgi)

### Installation
This project works with GraalVM `v20.0.0` version (or higher `v20.x.x`) which can be downloaded from [here](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-20.0.0). Follow [these instructions](https://www.graalvm.org/getting-started/#install-graalvm) to install it. Make sure that `$JAVA_HOME` environment variable point to the Graal installation and `$JAVA_HOME/bin` in the `$PATH`.

### Building the project
Execute `mvn clean package` to build the project.

### Running a Brainf\*ck program
`$ sh ./bf path/to/program.bf`

### Building a native binary
Native binary generation is done with the `native-image` utility, which is not bundled by default with the Graal distribution. Follow [these instructions](https://www.graalvm.org/docs/reference-manual/native-image/#install-native-image) to install it.

After installing, execute `mvn clean package -P native` which will create the binary in the `./modules/launcher/target` directory.

## Project structure
- **language** module - contains the Brainf\*ck language implementation
- **launcher** module - contains the command line launcher

## Resources
Following resources was very helpful for this project.
- [Writing a Language in Truffle](http://cesquivias.github.io/blog/2014/10/13/writing-a-language-in-truffle-part-1-a-simple-slow-interpreter/) by Cristian Esquivias
- [Building a programming language on GraalVM](https://maarten.mulders.it/2019/10/building-a-programming-language-on-graalvm-part-1/) by Maarten Mulders
- [SimpleLanguage](https://github.com/graalvm/simplelanguage) - A simple demonstration language built using Truffle for the GraalVM
- [TruffleSqueak](https://github.com/hpi-swa/trufflesqueak) - A Squeak/Smalltalk implementation for the GraalVM
- [Truffle BF](https://github.com/chumer/bf/)
