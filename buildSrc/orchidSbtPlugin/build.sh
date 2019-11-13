#!/usr/bin/env bash
set -e

./sbtw -Dorchid.version=${GRADLE_PROJECT_RELEASE_NAME} "; clean; package"
