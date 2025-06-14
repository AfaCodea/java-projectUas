#!/bin/bash

# Compile
javac -d bin -cp bin src/com/universitas/perpustakaan/*.java src/com/universitas/perpustakaan/gui/*.java src/com/universitas/perpustakaan/service/*.java src/com/universitas/perpustakaan/model/*.java src/com/universitas/perpustakaan/util/*.java

# Create icons directory if it doesn't exist
mkdir -p bin/icons
mkdir -p bin/icons_toggle

# Copy resources
cp -r src/com/universitas/perpustakaan/resources/icons/* bin/icons/
cp -r src/com/universitas/perpustakaan/resources/icons_toggle/* bin/icons_toggle/

# Run
java -cp bin com.universitas.perpustakaan.MainApp 