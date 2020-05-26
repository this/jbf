#!/usr/bin/env bash
java \
  -cp "modules/launcher/target/launcher-1.0.0-SNAPSHOT.jar:$JAVA_HOME/lib/graalvm/launcher-common.jar" \
  -Dtruffle.class.path.append="modules/language/target/language-1.0.0-SNAPSHOT.jar" \
  "sa.jbf.launcher.BFLauncher" \
  "$1"
