package com.sample.messaging.multiprocess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.sample.messaging.GeneralMessageServer;
import com.sample.messaging.MessagingStrategy;

/**
 * This class implements multi-process messaging scenario for send messages between clients 
 * run on own java processes. It uses socket programming for connecting each client client to the server.
 *
 * @author Mahnaz
 */

public class MultiProcessMessaging implements MessagingStrategy {

	private Socket socket;
	private int port;
	private Boolean listening = true;
	private GeneralMessageServer server;
	private ServerSocket serverSocket;  //Server Socket
	private MultiProcessMessageHandler multiProcessMessageHandler; 

	/**
	 * Creates instance by a server
	 * @param port
	 * @param messageServer
	 */
	public MultiProcessMessaging(GeneralMessageServer messageServer, int port) {
		this.server = messageServer;
		this.port = port;
	}

	/**
	 * Create socket server and message handler (for each connected client)
	 */
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Excption in creating server socket" + e.getMessage());
		}
		while (Boolean.TRUE.equals(listening)) {
			try {
				//listening to socket to accept the connected client
				socket = serverSocket.accept();
				System.out.println("client connected");

				//create message handler
				multiProcessMessageHandler = new MultiProcessMessageHandler(this);
				multiProcessMessageHandler.start();

			} catch (IOException e) {
				System.out.println("Excption in socket" + e.getMessage());
			}
		}
	}

	/**
	 * Get socket 
	 * @return socket
	 */
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * Set socket 
	 * @return socket
	 */
	public void setSocket(Socket socket) {
		this.socket= socket;
	}

	/**
	 * Get server
	 */
	public GeneralMessageServer getServer() {
		return server;
	}
}
