#!/usr/bin/env bash

# Test normal Gradle projects
if [ "$1" = "release" ]; then
  echo "[Orchid] Test Release"
  ./gradlew build -Prelease
else
  echo "[Orchid] Test Debug"
  ./gradlew build
fi

# Test Gradle plugin
pushd ./buildSrc
./test.sh "$1"

# Test Maven plugin
pushd ./orchidMavenPlugin
./test.sh "$1"
popd

# Restore original directory
popd
