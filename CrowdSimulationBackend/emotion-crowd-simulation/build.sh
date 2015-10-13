#!/bin/bash

#clean dirs
rm -rf libs
mkdir libs

# make tungsten
tungstenPath=http://source.concord.org/maven2/thirdparty/org/concord/external/tungsten-fsm/1.0
tungstenName=tungsten-fsm-1.0

curl "$tungstenPath/$tungstenName.jar" > "libs/$tungstenName.jar"

# make Quadtree
quadtreePath=https://raw.githubusercontent.com/kieda/Quadtree/master

curl "$quadtreePath/Quadtree.jar" > "libs/Quadtree-1.0.jar"

# make ModLang
modlangPath=https://raw.githubusercontent.com/kieda/ModLang/master/ModLang

curl "$modlangPath/ModLang.jar" > "libs/ModLang-1.0.jar"
