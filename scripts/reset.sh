#!/bin/bash
echo "Resetting MS-Fisio Application (removes all data)..."
docker-compose down -v
echo "Building and starting fresh containers..."
docker-compose up --build -d
echo "Application reset complete!"
echo "Waiting for services to be ready..."
sleep 30
echo "Application ready at http://localhost:8080"
