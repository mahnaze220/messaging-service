#!/bin/sh
java -cp ./messaging-1.0-SNAPSHOT.jar com.sample.messaging.multiprocess.MultiProcessMessagingDemo
trap 'sleep infinity' EXIT