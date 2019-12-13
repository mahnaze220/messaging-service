package com.sample.messaging;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a messaging server to broadcast messages between clients. 
 * It holds list of client's message handlers to keep number of send messages by each client.
 * It user observer design pattern to receive clients' messages.  
 *
 * @author Mahnaz
 */

public class GeneralMessageServer implements MessageServer, PropertyChangeListener {

	/** maximum number of messages each client can send */
	public static final int MESSAGE_SENDING_LIMIT = 9;

	/** This map hold number of sent messages of each client */
	private Map<String, Integer> clientSentMessages = new HashMap<>();

	/** This list holds all connected client's message handlers used for broadcasting */
	private List<MessagingHandler> messageHandlers = new ArrayList<>();

	/**
	 * Default constructor
	 */
	public GeneralMessageServer() {
		super();
	}

	/**
	 * When a client connected to server, its message handler is added to server list
	 */
	@Override
	public void addMessageHandler(MessagingHandler messageHandler) {
		messageHandlers.add(messageHandler);
	}

	/**
	 * Return number of connected clients
	 */
	public Integer getClientsCount() {
		return messageHandlers.size();
	}

	/**
	 * Checks the number of message has sent by the each client, if the number of messages meets specific limit, 
	 * program will be stopped.
	 */
	@Override
	public Boolean canStopProcess(String clientName) {
		if(getUserNames().containsKey(clientName) && getUserNames().get(clientName) > MESSAGE_SENDING_LIMIT) {
			System.out.println("Program finished");
			return true;
		}
		return false;
	}

	/**
	 * Return List of clients by number of their sent messages
	 * @return Map of client name, number of messages
	 */
	public Map<String, Integer> getUserNames() {
		return clientSentMessages;
	}

	/** 
	 * Sends message to all clients except the sender client and also send the message back to the sender client 
	 * @param message
	 * @param message handler of sender client
	 */
	@Override
	public void broadcastMessage(String message, MessagingHandler handler) {

		/*
		 * check number of sent messages by sender client
		 */
		Client senderClient = handler.getSenderClient();
		Boolean canStop = canStopProcess(senderClient.getUsername());

		if(Boolean.FALSE.equals(canStop)) {
			String messageBack = null;

			for (MessagingHandler client : getMessageHandlers()) {
				if (!client.getSenderClient().getUsername().equals(senderClient.getUsername())) {

					// send message to receiver client
					client.sendMessage(new StringBuilder(client.getSenderClient().getUsername()).append("/")
							.append(senderClient.getUsername()).append(": ").append(message).toString());
					
					System.out.println(senderClient.getUsername() + ": " + message);

					/* 
					 * create message back text and send from receiver client to sender client
					 */
					int count = getClientMessageCount(senderClient.getUsername()) != null ? 
							getClientMessageCount(senderClient.getUsername()) : 0;
					
					messageBack = message + count;
					System.out.println(client.getSenderClient().getUsername() + ": " + messageBack);
					messageBack = new StringBuilder(senderClient.getUsername()).append("/")
							.append(client.getSenderClient().getUsername()).append(": ")
							.append(message).append(count).toString();
					handler.sendMessage(messageBack);	

					/*
					 * update number of sent messages by each client
					 */
					updateClientMessageCount(client.getSenderClient().getUsername());
					updateClientMessageCount(senderClient.getUsername());
				}
			}
		}
	}

	/**
	 * Return list of message handlers of connected clients 
	 * @return
	 */
	public List<MessagingHandler> getMessageHandlers() {
		return messageHandlers;
	}

	/**
	 * Update number of messages sent by the client 
	 * @param userName
	 */
	@Override
	public void updateClientMessageCount(String username) {
		if(clientSentMessages.containsKey(username)) {
			clientSentMessages.put(username, clientSentMessages.get(username) + 1);
		}
		else {
			clientSentMessages.put(username, 1);
		}
	}

	/**
	 * Get the number of sent messages by the client
	 */
	@Override
	public Integer getClientMessageCount(String userName) {
		return clientSentMessages.get(userName);
	}

	/**
	 * When the client send the message, server is notified by the message handler to broadcast the message.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		MessagingHandler client = (MessagingHandler)evt.getNewValue();
		broadcastMessage(client.getMessage(), client);
	}

	/**
	 * Stop the server by closing the socket
	 */
	@Override
	public void stopServer(MessagingHandler handler) {
		System.out.println(handler.getSenderClient().getUsername() + " left");
		messageHandlers.remove(handler);
	}

	/**
	 * Get a client by it's username form list of connected clients
	 * @param username
	 * @return
	 */
	public Client getClientByName(String username) {
		for(MessagingHandler client: messageHandlers) {
			if(client.getSenderClient().getUsername().equals(username)) {
				return client.getSenderClient();
			}
		}
		return null;
	}
}
