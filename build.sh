#!/bin/bash

git clone --depth 1 https://github.com/Zeex/samp-plugin-sdk.git $HOME/samp-plugin-sdk

git clone --depth 1 https://github.com/Zeex/sampgdk.git sampgdk

cd sampgdk
mkdir build && cd build

cmake .. -DSAMP_SDK_ROOT=$HOME/samp-plugin-sdk
cmake --build . --config Release
cmake --build . --config Release --target install

cd ../../kamp

./gradlew build --scan -s
