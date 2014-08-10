#!/usr/bin/env bash

if [ "$(uname -s)" == "Darwin" ]; then
  gcc -shared -fPIC -I. -o liblinenoise.dylib linenoise.c
else
  gcc -shared -fPIC -I. -o liblinenoise.so linenoise.c
fi