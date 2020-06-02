#!/usr/bin/env bash
java \
  -Dtruffle.class.path.append="modules/language/target/language-1.0.0-SNAPSHOT.jar" \
  -jar "modules/launcher/target/launcher-1.0.0-SNAPSHOT.jar" \
  "$1"
