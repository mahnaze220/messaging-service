In both scenarios, Observer design pattern is used to notify sever (as an observer) by sending message from 
a client client (as an observable) to broadcast messages.
And also Strategy design pattern is used to implements two strategies (single-process and multi-process).

Single Java Process Scenario:

	In this scenario both clients (SimpleClient class instances) and server (GeneralMessageServer class instance) 
	run on the same java process. 
	Each client has own thread. SingleProcessMessaging class is used as an strategy and SingleProcessMessageHandler class
	as message handler to connect clients to server and handle messages.
	For each client a SingleProcessMessageHandler instance is created and it reads messages from client and send to the server.

Multiple Java Process Scenario:
	In this scenario each client (MultiProcessClient class instance) and also server (GeneralMessageServer class instance) 
	run on own java process. 
	Each client has own thread and creates a socket to connect to the server. 
	MultiProcessMessaging is used an a messaging strategy anf MultiProcessMessageHandler as a message handler
	to connect clients to server and handle messages.
	For each client a MultiProcessMessageHandler instance is created and it reads messages from the socket 
	and send to the server.
	
