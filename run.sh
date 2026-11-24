#!/usr/bin/env bash
# Compile and run the Student Grade Tracker
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -d out src/*.java
java -cp out StudentGradeTracker
