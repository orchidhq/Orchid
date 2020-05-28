#!/usr/bin/env bash
set -e

# Build and deploy normal Gradle projects
./gradlew assemble publishToMavenLocal publish :docs:orchidDeploy -PorchidEnvironment=prod -Prelease

git config --local user.name "Travis CI Deployment Bot"
git config --local user.email "deploy@travis-ci.org"
./gradlew tag -Prelease

export GRADLE_PROJECT_RELEASE_NAME=$(./gradlew getReleaseName -Prelease --quiet)

# Deploy Gradle plugin
pushd buildSrc
./deploy.sh

# Deploy Maven plugin
pushd orchidMavenPlugin
./deploy.sh
popd

# Deploy SBT plugin
pushd orchidSbtPlugin
./deploy.sh
popd

# Restore original directory
popd
