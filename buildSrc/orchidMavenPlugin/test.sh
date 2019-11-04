#!/usr/bin/env bash

if [ "$1" = "release" ]; then
  echo "[Maven Plugin] Test Release"
  ./mvnw package
else
  echo "[Maven Plugin] Test Debug"
  ./mvnw package
fi
