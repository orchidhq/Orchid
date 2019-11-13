#!/usr/bin/env bash
set -e

./mvnw versions:set -DnewVersion=$GRADLE_PROJECT_RELEASE_NAME
./mvnw deploy -s settings.xml
