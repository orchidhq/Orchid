#!/usr/bin/env bash

./gradlew clean build deploy -Penv=prod -Prelease -PskipMavenCentralSync=true