#!/bin/bash

# Check if a version number was provided
if [ -z "$1" ]; then
    echo "Please provide a version number as an argument."
    echo "Usage: ./server.sh <version>"
    exit 1
fi

VERSION=$1

# Check if Docker is installed
if ! command -v docker &> /dev/null
then
    echo "Docker could not be found. Please install Docker to proceed."
    exit 1
fi

# Check if Gradle is installed
if ! command -v gradle &> /dev/null
then
    echo "Gradle could not be found. Please install Gradle to proceed."
    exit 1
fi

# Clean and build the Gradle project
echo "Running Gradle clean build..."
./gradlew clean build

# Check if the build was successful
if [ $? -ne 0 ]; then
    echo "Gradle build failed. Exiting."
    exit 1
fi

# Build the Docker image
echo "Building Docker image..."
docker build --platform linux/amd64 -t zxcyui6181/monsil-server:${VERSION} .

# Check if Docker build was successful
if [ $? -ne 0 ]; then
    echo "Docker build failed. Exiting."
    exit 1
fi

# Push the Docker image to Docker Hub
echo "Pushing Docker image..."
docker push zxcyui6181/monsil-server:${VERSION}

# Check if Docker push was successful
if [ $? -ne 0 ]; then
    echo "Docker push failed. Exiting."
    exit 1
fi

echo "Docker image zxcyui6181/monsil-server:${VERSION} has been built and pushed successfully."

