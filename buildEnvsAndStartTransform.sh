#!/bin/bash

start_environent_dockers(){
    echo "Building and running environment images in background mode..."
    docker-compose up -d --build
}

build_producer_app_docker_image(){
    echo "Building Producer App Docker image..."
    docker build -t jf_data_producer data-producer/
}

build_transform_app_docker_image(){
    echo "Building Transform App Docker image..."
    sbt buildDockerImage
}

run_transform_app_docker_image(){
    echo "Running Transform App Docker image..."
    docker run -it -v C:\Users\Jake\Documents\kafka-meets-spark-and-docker\docker-environment\services\hive\volumes\data:/data/hive jf_transform:latest
}

main() {
    cd docker-environment
    start_environent_dockers

    cd ../
    build_producer_app_docker_image

    cd transform-app
    build_transform_app_docker_image
    
    run_transform_app_docker_image
}

main