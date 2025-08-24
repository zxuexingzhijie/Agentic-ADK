#!/bin/bash

# Install script for ali-langengine-github module

echo "Installing ali-langengine-github module..."

# Check if pom.xml exists
if [ ! -f "pom.xml" ]; then
    echo "Error: pom.xml not found in current directory"
    exit 1
fi

# Install to local repository
mvn clean install

# Check installation result
if [ $? -eq 0 ]; then
    echo "Installation successful"
    echo "Module installed to local Maven repository"
else
    echo "Installation failed"
    exit 1
fi
