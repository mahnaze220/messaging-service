package com.sample.messaging;

/**
 * This interface has common methods for different types of messaging handlers
 *  
 * @author Mahnaz
 *
 */
public interface MessagingHandler {
	
	/**
	 * Return sender client
	 * @return sender client
	 */
	public Client getSenderClient();
	
	/**
	 * Send message between client and server
	 * @param message
	 */
	public void sendMessage(String message);
	
	/**
	 * Return message
	 * @return message
	 */
	public String getMessage();

}
