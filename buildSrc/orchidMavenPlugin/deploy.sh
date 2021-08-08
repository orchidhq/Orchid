#!/usr/bin/env bash
set -e

export GPG_TTY=$(tty)

./mvnw versions:set -DnewVersion=$projectVersion
./mvnw deploy -s settings.xml
