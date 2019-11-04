#!/usr/bin/env bash

if [ "$1" = "release" ]; then
  echo "[Gradle Plugin] Test Release"
  ./gradlew build -Prelease
else
  echo "[Gradle Plugin] Test Debug"
  ./gradlew build
fi
