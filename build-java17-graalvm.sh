#!/bin/sh

file1="lambda-app-graalvm.zip"
if [ -f "$file1" ] ; then
    rm "$file1"
fi

file2="graalvm-layer.zip"
if [ -f "$file2" ] ; then
    rm "$file2"
fi

docker build -f Dockerfile_graalvm --progress=plain -t lambda-runtime-graalvm-java17 .

docker run --rm --entrypoint cat lambda-runtime-graalvm-java17 lambda-app-graalvm.zip > lambda-app-graalvm.zip
docker run --rm --entrypoint cat lambda-runtime-graalvm-java17 graalvm-layer.zip > graalvm-layer.zip