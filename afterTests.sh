#!/usr/bin/env bash
set -e

./gradlew assemble codeCoverageReport sendCoverageToCodacy
