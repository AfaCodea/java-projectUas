#!/bin/bash

# Compile
javac -d bin -cp bin src/com/universitas/perpustakaan/*.java src/com/universitas/perpustakaan/gui/*.java src/com/universitas/perpustakaan/service/*.java src/com/universitas/perpustakaan/model/*.java src/com/universitas/perpustakaan/util/*.java

# Copy resources
cp -r src/com/universitas/perpustakaan/resources/icons/* bin/icons/

# Run
java -cp bin com.universitas.perpustakaan.MainApp 