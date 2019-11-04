#!/usr/bin/env bash

./mvnw versions:set -DnewVersion=$GRADLE_PROJECT_RELEASE_NAME
./mvnw deploy -s settings.xml
