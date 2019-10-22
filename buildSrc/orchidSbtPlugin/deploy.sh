#!/usr/bin/env bash

./sbtw -Dorchid.version=${GRADLE_PROJECT_RELEASE_NAME} -Dorchid.testbuild=false -Dbintray.user=${ORG_GRADLE_PROJECT_bintray_username} -Dbintray.pass=${ORG_GRADLE_PROJECT_bintray_apiKey} publish


