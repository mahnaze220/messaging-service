package com.sample.messaging.singleprocess;

import com.sample.messaging.Client;

/**
 * This class creates instances for client clients which run on the same java process.
 * Each instance sends message to the server on the same java process.
 *
 * @author Mahnaz
 */

public class SingleProcessClient implements Client, Runnable {

	private String username;
	private String message = "hello";

	/**
	 * Get client's username
	 */

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Set palyer's username
	 */
	@Override
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * Print the received message on the client client's console
	 */
	public void sendMessage(String message) {
		if(message != null) {
			String[] msg = message.split(":");
			setMessage(msg[1]);
		}
	}

	@Override
	public void run() {
		System.out.println(username + " client connected to the message server");
	}

	/**
	 * Get client's message
	 */
	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Set client's message
	 */
	@Override
	public String getMessage() {
		return message;
	}
}
