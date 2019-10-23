#!/usr/bin/env bash

# Build and deploy normal Gradle projects
./gradlew assemble deploy :OrchidCore:orchidDeploy -Penv=prod -Prelease

git config --local user.name "Travis CI Deployment Bot"
git config --local user.email "deploy@travis-ci.org"
./gradlew tag -Prelease

export GRADLE_PROJECT_RELEASE_NAME=$(./gradlew getReleaseName --quiet)
export GRADLE_PROJECT_RELEASE_NOTES=$(./gradlew getReleaseNotes --quiet)

# Deploy Gradle plugin
pushd buildSrc
./deploy.sh

# Deploy Maven plugin
pushd orchidMavenPlugin
./deploy.sh
popd

# Restore original directory
popd
