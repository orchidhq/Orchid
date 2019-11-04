#!/usr/bin/env bash

./sbtw -Dorchid.version=${GRADLE_PROJECT_RELEASE_NAME} "; clean; package"
