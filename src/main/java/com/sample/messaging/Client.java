package com.sample.messaging;

/**
 * Each client has a username and can connect to the message server and send message to another client.
 *
 * @author Mahnaz
 */

public interface Client {

	/**
	 * Return username of client
	 * @return username
	 */
	public String getUsername();
	
	/**
	 * Set username of client
	 * @param username
	 */
	public void setUsername(String username);
	
	/**
	 * Set client's message
	 * @param message
	 */
	public void setMessage(String message);
	
	/**
	 * Get client's message
	 * @return message
	 */
	public String getMessage();
	
	/**
	 * Send message
	 * @param message
	 */
	public void sendMessage(String message);
}
