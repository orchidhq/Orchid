#!/usr/bin/env bash

./gradlew publishPlugins -Dgradle.publish.key=${GRADLE_PUBLISH_KEY} -Dgradle.publish.secret=${GRADLE_PUBLISH_SECRET}
