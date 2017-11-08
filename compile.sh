##! /bin/bash

echo "Compiling gvnapster..."
cd gvnapster/
rm -rf build/*
javac -d build/ src/*.java
cd ../

echo "Compiling server..."
cd server/
rm -rf build/*
javac -d build/ src/*.java
cd ../

echo "Compile finished"
