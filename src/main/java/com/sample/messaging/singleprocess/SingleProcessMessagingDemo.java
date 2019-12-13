package com.sample.messaging.singleprocess;

import com.sample.messaging.GeneralMessageServer;
import com.sample.messaging.MessagingContext;

public class SingleProcessMessagingDemo {

	public static void main(String[] args) {
		run();
	}

	public static void run() {

		SingleProcessClient initiator = new SingleProcessClient();
		initiator.setUsername("receiver");
		Thread t1 = new Thread(initiator);
		t1.start();
		
		SingleProcessClient receiver = new SingleProcessClient();
		receiver.setUsername("initiator");
		Thread t2 = new Thread(receiver);
		t2.start();
		
		SingleProcessMessaging handler = new SingleProcessMessaging(new GeneralMessageServer());
		handler.addClient(initiator);
		handler.addClient(receiver);
		
		MessagingContext ctx = new MessagingContext();
		//set single-process messaging strategy
	    ctx.setStrategy(handler);
	    ctx.run();
	}
}
