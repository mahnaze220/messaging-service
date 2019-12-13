package com.sample.messaging.singleprocess;

import java.util.ArrayList;
import java.util.List;

import com.sample.messaging.GeneralMessageServer;
import com.sample.messaging.MessagingStrategy;

/**
 * This class implements single-process messaging scenario for send messages between clients 
 * run on same java process. 
 *
 * @author Mahnaz
 */

public class SingleProcessMessaging implements MessagingStrategy {

	private GeneralMessageServer server;
	private List<SingleProcessClient> clients = new ArrayList<>();

	/**
	 * Creates instance by a server
	 * @param messageServer
	 */
	public SingleProcessMessaging(GeneralMessageServer server) {
		this.server = server;		
	}

	/**
	 * Create message handler for each connected client
	 */
	public void run() {
		for(SingleProcessClient client: clients) {
			SingleProcessMessageHandler singleProcessMessageHandler = new SingleProcessMessageHandler(this);
			singleProcessMessageHandler.setSenderClient(client);
			server.addMessageHandler(singleProcessMessageHandler);
			singleProcessMessageHandler.run();
		}
	}

	/**
	 * Get server
	 */
	public GeneralMessageServer getServer() {
		return server;
	}

	/**
	 * Add client for creating related message handler
	 * @param client
	 */
	public void addClient(SingleProcessClient client) {
		clients.add(client);
	}
}