#!/usr/bin/env bash
set -e

if [ "$1" = "release" ]; then
  echo "[SBT Plugin] Test Release"
  ./sbtw -batch -Dorchid.version=${GRADLE_PROJECT_RELEASE_NAME} -Dorchid.testbuild=true scripted
else
  echo "[SBT Plugin] Test Debug"
  ./sbtw -batch -Dorchid.version=${GRADLE_PROJECT_RELEASE_NAME} -Dorchid.testbuild=true scripted
fi
