#!/usr/bin/env bash
set -e

if [ "$1" = "release" ]; then
  echo "[Gradle Plugin] Test Release"
  ./gradlew build -Prelease
else
  echo "[Gradle Plugin] Test Debug"
  ./gradlew build
fi
