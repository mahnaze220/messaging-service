#!/bin/sh
java -cp ./messaging-1.0-SNAPSHOT.jar com.sample.messaging.singleprocess.SingleProcessMessagingDemo  
trap 'sleep infinity' EXIT
