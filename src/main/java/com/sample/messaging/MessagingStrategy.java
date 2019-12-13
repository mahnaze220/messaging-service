package com.sample.messaging;

/**
 * This interface has common methods for different types of messaging strategies.
 *
 * @author Mahnaz
 */

public interface MessagingStrategy {

	/**
	 * Return connected server
	 * @return
	 */
	public MessageServer getServer();
	
	/**
	 * Run the strategy 
	 */
	public void run();
}
