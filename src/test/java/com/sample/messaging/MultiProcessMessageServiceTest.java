package com.sample.messaging;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.sample.messaging.multiprocess.MultiProcessClient;
import com.sample.messaging.multiprocess.MultiProcessMessageHandler;
import com.sample.messaging.multiprocess.MultiProcessMessaging;

/**
 * This class tests sending messages in the scenario when clients on own java processes
 * and use socket for sending messages to the server
 *
 * @author Mahnaz
 */

@RunWith(MockitoJUnitRunner.class)
public class MultiProcessMessageServiceTest {

	@Mock
	private Socket socket;

	@Spy
	@InjectMocks
	private GeneralMessageServer messageServer;

	@Test
	public void broadcast_successfullScenario() {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		MultiProcessMessageHandler initiatorHandler = createMultiProcessMsgHandler(socket);
		initiatorHandler.setSenderClient(createInitiatorClient());
		MultiProcessMessageHandler receiverHandler = createMultiProcessMsgHandler(socket);
		receiverHandler.setSenderClient(createReceiverClient());
		List<MultiProcessMessageHandler> handlers = new ArrayList<>();
		handlers.add(initiatorHandler);
		handlers.add(receiverHandler);

		Mockito.doReturn(Boolean.FALSE).when(messageServer).canStopProcess(anyString());
		Mockito.doReturn(handlers).when(messageServer).getMessageHandlers();
		Mockito.doReturn(createUsernames()).when(messageServer).getUserNames();
		messageServer.broadcastMessage("hello", initiatorHandler);

		Assert.assertTrue(byteArrayOutputStream.toString().contains("receiver/initiator: hello") 
				&& byteArrayOutputStream.toString().contains("initiator/receiver: hello0"));
	}

	@Test
	public void broadcast_withThreeMessageCount_successfullScenario(){
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		MultiProcessMessageHandler initiatorHandler = createMultiProcessMsgHandler(socket);
		initiatorHandler.setSenderClient(createInitiatorClient());
		MultiProcessMessageHandler receiverHandler = createMultiProcessMsgHandler(socket);
		receiverHandler.setSenderClient(createReceiverClient());
		List<MultiProcessMessageHandler> handlers = new ArrayList<>();
		handlers.add(initiatorHandler);
		handlers.add(receiverHandler);

		Mockito.doReturn(Boolean.FALSE).when(messageServer).canStopProcess(anyString());
		Mockito.doReturn(handlers).when(messageServer).getMessageHandlers();
		Mockito.doReturn(createUsernames()).when(messageServer).getUserNames();
		Mockito.doReturn(new Integer(3)).when(messageServer).getClientMessageCount(anyString());

		messageServer.broadcastMessage("hello", initiatorHandler);

		Assert.assertTrue(byteArrayOutputStream.toString().contains("receiver/initiator: hello") 
				&& byteArrayOutputStream.toString().contains("initiator/receiver: hello3"));
	}

	@Test
	public void checkMessageCount_withElevenMessageCount_meetStopCondition(){
		Mockito.doReturn(createUsernamesWithCount()).when(messageServer).getUserNames();
		Boolean result = messageServer.canStopProcess("initiator");
		Assert.assertTrue(result);
	}

	public MultiProcessMessageHandler createMultiProcessMsgHandler(Socket socket) {
		MultiProcessMessaging multiProcessMessaging = new MultiProcessMessaging(new GeneralMessageServer(), 8020);
		multiProcessMessaging.setSocket(socket);
		MultiProcessMessageHandler messageHandler = new MultiProcessMessageHandler(multiProcessMessaging);		
		return messageHandler;
	}

	public MultiProcessClient createInitiatorClient() {
		return new MultiProcessClient("localhost", 8020, "initiator");
	}

	public MultiProcessClient createReceiverClient() {
		return new MultiProcessClient("localhost", 8020, "receiver");
	}

	public Map<String, Integer> createUsernames(){
		Map<String, Integer> usernames = new HashMap<String, Integer>();
		usernames.put("initiator", 0);
		usernames.put("receiver", 0);
		return usernames;
	}

	public Map<String, Integer> createUsernamesWithCount(){
		Map<String, Integer> usernames = new HashMap<String, Integer>();
		usernames.put("initiator", 11);
		usernames.put("receiver", 11);
		return usernames;
	}
}
