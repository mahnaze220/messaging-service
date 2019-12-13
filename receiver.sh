#!/bin/sh
java -cp ./messaging-1.0-SNAPSHOT.jar com.sample.messaging.multiprocess.MultiProcessClient  $"localhost" $"8020" $"receiver"
