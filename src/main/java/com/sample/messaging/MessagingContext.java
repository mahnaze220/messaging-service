package com.sample.messaging;

/**
 * This class holds a reference to a messaging strategy object and delegates it executing the behavior 
 * in Strategy design pattern.
 *
 * @author Mahnaz
 */

public class MessagingContext {

	private MessagingStrategy strategy;

	/**
	 * Set the messaging strategy at runtime by the application preferences
	 * @param strategy
	 */
	public void setStrategy(MessagingStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * Run the selected strategy
	 */
	public void run() {
		strategy.run();
	}
}
