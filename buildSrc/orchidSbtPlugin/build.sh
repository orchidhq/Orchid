#!/usr/bin/env bash
set -e

./sbtw -batch -Dorchid.version=${GRADLE_PROJECT_RELEASE_NAME} "; clean; package"
