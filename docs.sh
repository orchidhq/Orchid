#!/usr/bin/env bash
set -e

./gradlew assemble :docs:orchidBuild -PorchidEnvironment=prod -Prelease
