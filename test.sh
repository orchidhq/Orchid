#!/usr/bin/env bash

# Test normal Gradle projects
if [ "$1" = "release" ]; then
  echo "[Orchid] Test Release"
  ./gradlew build publishToMavenLocal -Prelease
else
  echo "[Orchid] Test Debug"
  ./gradlew build publishToMavenLocal
fi

export GRADLE_PROJECT_RELEASE_NAME=$(./gradlew getReleaseName --quiet)

# Test Gradle plugin
pushd ./buildSrc
./test.sh "$1"

# Test Maven plugin
pushd ./orchidMavenPlugin
./test.sh "$1"
popd

# Test Maven plugin
pushd ./orchidSbtPlugin
./test.sh "$1"
popd

# Restore original directory
popd
