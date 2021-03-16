#!/usr/bin/env bash
set -e

../gradlew publishPlugins -b orchidPlugin/build.gradle.kts -Dgradle.publish.key=${GRADLE_PUBLISH_KEY} -Dgradle.publish.secret=${GRADLE_PUBLISH_SECRET} -PorchidEnvironment=prod -Prelease
