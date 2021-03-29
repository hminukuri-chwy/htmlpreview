#!/bin/bash
docker build . -t htmlpreview
mkdir -p build
docker run --rm --entrypoint cat htmlpreview  /home/application/function.zip > build/function.zip
