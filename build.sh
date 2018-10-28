#!/bin/bash

set -e

cd dcs-client
./build.sh
cd ..

echo "Removing build directories"
for dir in `find . -maxdepth 2 -type d -name "build"`; do
    srm -r -- $dir || rmdir $dir
done

./gradlew clean check test stage
