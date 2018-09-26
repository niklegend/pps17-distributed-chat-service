#/bin/bash
set -e

cwd=`pwd`
root=`git rev-parse --show-toplevel`
cd $root

./gradlew check clean test build

cd $cwd