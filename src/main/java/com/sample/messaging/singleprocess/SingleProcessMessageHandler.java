package com.sample.messaging.singleprocess;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.sample.messaging.MessagingHandler;
import com.sample.messaging.Client;

/**
 * This message handler is created for each client in single-process scenario. 
 * This class plays role of mediator between the server and client and handle messages. 
 * It uses observer design pattern to notify server to broadcast messages.
 * This handler uses multi-process strategy for sending messages by socket programming.  
 *
 * @author Mahnaz
 */

public class SingleProcessMessageHandler implements MessagingHandler {

	private SingleProcessClient senderClient;
	private SingleProcessMessaging handler;
	private PropertyChangeSupport support;
	private String clientMessage;

	/**
	 * Create message handler by related messaging strategy
	 * @param strategy
	 */
	public SingleProcessMessageHandler(SingleProcessMessaging strategy) {
		this.handler = strategy;

		//add this object as an observable entity
		support = new PropertyChangeSupport(this);
	}

	public void run() {

		//add server to notify it as an observer
		addPropertyChangeListener(handler.getServer());
		Boolean shouldStop = false;

		// start the process after connecting the second client
		if(handler.getServer().getClientsCount() != null && handler.getServer().getClientsCount() > 1) {

			// read client message and send it until the number of initiator's sent messages meet limit
			do {
				clientMessage = senderClient.getMessage();
				shouldStop = handler.getServer().canStopProcess(getSenderClient().getUsername());

				// notify the server to broadcast messages to the client
				support.firePropertyChange("SimpleMessageHandlerThread", handler.getServer(), this);
			} 
			while (Boolean.FALSE.equals(shouldStop));
		}
	}

	/**
	 * Get sender client
	 */
	@Override
	public Client getSenderClient() {
		return senderClient;
	}

	/**
	 * Set sender client
	 */
	public void setSenderClient(SingleProcessClient senderClient) {
		this.senderClient = senderClient;
	}

	/**
	 * Send message to client
	 */
	@Override
	public void sendMessage(String message) {
		senderClient.sendMessage(message);
	}

	/**
	 * Get client' message
	 */
	@Override
	public String getMessage() {
		return clientMessage;
	}

	/**
	 * Add server to observers list
	 * @param pcl
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	/**
	 * remove the server from observers
	 * @param pcl
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}
}
