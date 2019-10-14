#!/bin/bash

start_producer_app(){
    echo "Running the producer application..."
    docker run jf_data_producer
}

start_producer_app