package com.sample.messaging.multiprocess;

import com.sample.messaging.GeneralMessageServer;
import com.sample.messaging.MessagingContext;

public class MultiProcessMessagingDemo {

	public static void main(String[] args) {
		MultiProcessMessaging handler = new MultiProcessMessaging(new GeneralMessageServer(), 8020);
		MessagingContext ctx = new MessagingContext();
		
		//set multi-process messaging strategy
		ctx.setStrategy(handler);
		ctx.run();
		
		System.out.println("Sever is listening ...");
	}
}
