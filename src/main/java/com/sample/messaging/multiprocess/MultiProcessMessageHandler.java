package com.sample.messaging.multiprocess;

/**
 * This message handler is created for each client in multi-process scenario. 
 * This class plays role of mediator between the server and client and handle messages. 
 * It uses observer design pattern to notify server to broadcast messages.
 * This handler uses single-process strategy for sending messages.  
 *
 * @author Mahnaz
 */

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.sample.messaging.MessagingHandler;

public class MultiProcessMessageHandler extends Thread implements MessagingHandler {

	private PropertyChangeSupport support;
	private MultiProcessMessaging handler;
	private Boolean listening = true;
	private MultiProcessClient senderClient;
	private String clientMessage;
	private PrintWriter writer;
	private BufferedReader reader;

	/**
	 * Create message handler by related messaging strategy
	 * @param strategy
	 */
	public MultiProcessMessageHandler(MultiProcessMessaging strategy) {		
		this.handler = strategy;

		//add this object as an observable entity
		support = new PropertyChangeSupport(this);

		//add his hander to the server list
		strategy.getServer().addMessageHandler(this);

		try {

			//get input stream from socket
			InputStream input = strategy.getSocket().getInputStream();
			if(input != null) {
				reader = new BufferedReader(new InputStreamReader(input));
				ObjectInputStream objectInput = new ObjectInputStream(input);
				// get sender client form socket
				senderClient = (MultiProcessClient)objectInput.readObject();
			}

			//get output stream from socket
			OutputStream output = strategy.getSocket().getOutputStream();
			this.writer = new PrintWriter(output, true);

		} catch (IOException | ClassNotFoundException ioe) {
			System.out.println("Exception in creating message handler " + ioe.getMessage());
		}
	}

	@Override
	public void run() {

		//add server to notify it as an observer
		addPropertyChangeListener(handler.getServer());
		listening = true;

		//send messages until the server listening to socket
		while (Boolean.TRUE.equals(listening)) {
			handleMessage();
		}
	}

	/**
	 * Handle sending messages between client and server
	 */
	public void handleMessage() {

		// start the process after connecting the second client
		Boolean shouldStop = false;

		// read client message from the socket and send it until the number of initiator's sent messages meet limit  
		do {
			shouldStop = handler.getServer().canStopProcess(senderClient.getUsername());
			try {
				clientMessage = reader.readLine();
			} catch (IOException e) {
				System.out.println("Exception in raeding client message" + e.getMessage());
			}

			// notify the server to broadcast messages to the client
			support.firePropertyChange("MessageHandlerThread", handler.getServer(), this);
		} 
		while (Boolean.FALSE.equals(shouldStop) && !handler.getSocket().isClosed());

		//stop sever 
		handler.getServer().stopServer(this);
		listening = false;
	}

	/**
	 * Get sender client
	 */
	public MultiProcessClient getSenderClient() {
		return senderClient;
	}

	/**
	 * Set sender client
	 */
	public void setSenderClient(MultiProcessClient senderClient) {
		this.senderClient = senderClient;
	}

	/**
	 * Get client' message
	 */
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

	/**
	 * Send message to client
	 */
	public void sendMessage(String message) {
		writer.println(message);
	}
}
