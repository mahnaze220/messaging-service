package com.sample.messaging;

/**
 * This interface is used for creating different types of message servers.
 *
 * @author Mahnaz
 */

public interface MessageServer {

	/**
	 * Stop server after closing a connection and remove message handler from the connected clients list
	 * @param message handler
	 */
	public void stopServer(MessagingHandler handler);

	/**
	 * Add message handler of connected client to server's list
	 * @param message handler
	 */
	public void addMessageHandler(MessagingHandler client);

	/**
	 * Check the criteria for stopping sending message for a client
	 * @param client name
	 * @return
	 */
	public Boolean canStopProcess(String clientName);

	/**
	 * Broadcast message from sender to all connected clients
	 * @param message
	 * @param message handler of sender client
	 */
	public void broadcastMessage(String message, MessagingHandler handler);

	/**
	 * Update number of send messages by the client
	 * @param client name
	 */
	public void updateClientMessageCount(String clientName);

	/**
	 * Retun number of sent messages by the client
	 * @param client name
	 * @return
	 */
	public Integer getClientMessageCount(String clientName);

	/**
	 * Return number of connected clients
	 * @return
	 */
	public Integer getClientsCount();

}
