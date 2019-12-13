package com.sample.messaging.multiprocess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sample.messaging.Client;

/**
 * This class creates instances for client client which run on own java process.
 * Each instance sends message to the server by socket on a specific host and port.
 *
 * @author Mahnaz
 */

public class MultiProcessClient implements Client, Serializable {

	private static final long serialVersionUID = 2463591595170023897L;

	private String hostname;
	private int port;
	private String username;
	private String message; 

	/**
	 * Create client instance 
	 * @param hostname of socket
	 * @param port of socket
	 * @param username of client
	 */
	public MultiProcessClient(String hostname, int port, String username) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
	}

	/**
	 * This method runs each client on own java process and send the message by a socket to the server
	 */
	public void run() {
		try {

			// create socket
			Socket socket = new Socket(hostname, port);

			System.out.println(username + " client connected to the message server");

			// get message from the server
			new ReadMessageThread(socket, this).start();

			// send message to the server
			new WriteMessageThread(socket, this).start();

		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O Exception: " + ex.getMessage());
		}		
	}

	/**
	 * Print the received message on the client's console
	 */
	public void sendMessage(String message) {
		System.out.println(message);
	}

	/**
	 * Get client's username
	 */
	@Override
	public String getUsername() {
		return this.username;
	}

	/**
	 * Set palyer's username
	 */
	@Override
	public void setUsername(String username){
		this.username = username;
	}

	/**
	 * This class reads message from the socket and send it to the client.
	 * It runs until the client client disconnects from the server.
	 */

	private class ReadMessageThread extends Thread {

		private BufferedReader reader;
		private Socket socket;
		private Client client;

		/**
		 * Creates an instance of ReadMessageThread for each client
		 * @param socket 
		 * @param client
		 */
		public ReadMessageThread(Socket socket, Client client) {
			this.socket = socket;
			this.client = client;

			try {
				InputStream input = socket.getInputStream();
				reader = new BufferedReader(new InputStreamReader(input));
			} catch (IOException ex) {
				System.out.println("IO Exception: " + ex.getMessage());
			}
		}

		/**
		 * Read message from socket and send it to client
		 */
		@Override
		public void run() {
			while (true) {
				try {

					// read message from the socket 
					String response = reader.readLine();
					if(response != null ) {					
						String[] result = response.split("/");

						// each message contains the receiver name, so each client gets own message
						if(result[0].equals(client.getUsername())) {
							client.sendMessage("\n" + result[1]);
							String[] msg = result[1].split(":");
							client.setMessage(msg[1]);
						}
					}
				} catch (IOException ex) {
					System.out.println("Exception in reading message from server: " + ex.getMessage());
					break;
				}
			}
		}
	}

	/**
	 * This inner class reads user input and send it to the server to broadcast it to another client client.
	 * It uses socket to write messages and send to the server.
	 * It stops when after closing the socket.
	 */

	private class WriteMessageThread extends Thread {

		private PrintWriter writer;
		private Socket socket;
		private MultiProcessClient client;

		/**
		 * Creates an instance of WriteMessageThread for each client
		 * @param socket
		 * @param client
		 */
		public WriteMessageThread(Socket socket, MultiProcessClient client) {
			this.socket = socket;
			this.client = client;

			try {
				OutputStream output = socket.getOutputStream();
				writer = new PrintWriter(output, true);
			} catch (IOException ex) {
				System.out.println("IO Exception: " + ex.getMessage());
			}
		}

		/**
		 * Get messages from client console and write on the socket
		 */
		@Override
		public void run() {
			try {

				// write on the socket
				ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
				objectOutput.writeObject(client);
				
				/* 
				 * read message 
				 */
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Enter your message: ");

				// stop writing on the socket until socket is not closed
				do {
					message = reader.readLine();
					writer.println(getMessage());
				} while (!socket.isClosed());

			} catch (IOException e) {
				System.out.println("IO Exception in sending message to the server: " + e.getMessage());
			}
			try {
				socket.close();
			} catch (IOException ex) {
				System.out.println("Exception in sending message to the server: " + ex.getMessage());
			}
		}
	}

	/**
	 * Get client message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set client message
	 */
	public void setMessage(String message) {
		this.message= message ;
	}

	/**
	 * Main class to crates client instance on a new java process
	 * @param args: host name, port and client username
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			return;
		}

		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		String clientName = args[2];

		MultiProcessClient client = new MultiProcessClient(hostname, port, clientName);
		client.run();
	}
}
