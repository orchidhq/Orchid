#!/usr/bin/env bash
set -e

if [ "$1" = "release" ]; then
  echo "[Maven Plugin] Test Release"
  ./mvnw package
else
  echo "[Maven Plugin] Test Debug"
  ./mvnw package
fi
