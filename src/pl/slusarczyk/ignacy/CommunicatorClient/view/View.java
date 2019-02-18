package pl.slusarczyk.ignacy.CommunicatorClient.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ClientHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.AfterConnectionServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConversationServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.AlertServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.MessageData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;

/**
 * â ǥ�� �� ��ȭ ������ ���
 */
public class View {
	/** �� �� ���� �Ǵ� ���������� ���� â */
	private CreateOrJoinRoomView createOrJoinRoomView;
	/** �⺻ ä�� â */
	private MainChatView mainChatView;
	/** ��Ʈ�ѷ��� ó�� �� �̺�Ʈ �̺�Ʈ�� ������ ���ŷ ť */
	private final BlockingQueue<ServerHandledEvent> eventQueue;

	private final Map<Class<? extends ClientHandledEvent>, ClientHandledEventStrategy> strategyMap;

	/**
	 * ������ �Ķ���Ϳ� �ٰ��ϴ� �並 �ۼ��ϴ� ������
	 * 
	 * @param eventQueue
	 */
	public View(BlockingQueue<ServerHandledEvent> eventQueue) {
		this.createOrJoinRoomView = new CreateOrJoinRoomView(eventQueue);
		this.eventQueue = eventQueue;

		/** ���� �� ���� �� �Ҵ� */
		this.strategyMap = new HashMap<Class<? extends ClientHandledEvent>, ClientHandledEventStrategy>();
		this.strategyMap.put(AfterConnectionServerEvent.class, new AfterConnectionStrategy());
		this.strategyMap.put(ConversationServerEvent.class, new ConversationServerEventStrategy());
		this.strategyMap.put(AlertServerEvent.class, new MessageServerEventStrategy());
	}

	/**
	 * �����ϴ� ������ ������ �����ϴ� å���� ���� �޼ҵ�
	 * 
	 * @param clientHandeledEventObject
	 */
	public void executeClientHandeledEvent(ClientHandledEvent clientHandeledEventObject) {
		ClientHandledEventStrategy clientHandeledEventStrategy = strategyMap.get(clientHandeledEventObject.getClass());
		clientHandeledEventStrategy.execute((ClientHandledEvent) clientHandeledEventObject);
	}

	/**
	 * �̺�Ʈ�� ó���ϴ� ���� Ŭ������ �߻� �⺻ Ŭ����
	 */
	abstract class ClientHandledEventStrategy {
		/**
		 * �־��� �̺�Ʈ�� ���񽺸� ����ϴ� �߻� �޼ҵ�.
		 * 
		 * @param ClientHandledEvent
		 */
		abstract void execute(final ClientHandledEvent clientHandeledEvent);
	}

	/**
	 * ���� ���� �Ǵ� ������ ���������� �Ϸ�� ������ ������ ó���ϴ� ������ �����ϴ� ���� Ŭ����
	 */
	class AfterConnectionStrategy extends ClientHandledEventStrategy {

		@Override
		void execute(ClientHandledEvent clientHandledObject) {
			AfterConnectionServerEvent afterConnectionObject = (AfterConnectionServerEvent) clientHandledObject;
			mainChatView = new MainChatView(eventQueue, afterConnectionObject.getUserIDData(), afterConnectionObject.getRoomName());
			createOrJoinRoomView.closeCreateRoomWindow();
			createOrJoinRoomView = null;
		}
	}

	/**
	 * ��ȭ ���� �� ����� ��� ó�� ����� �����ϴ� ���� Ŭ����
	 */
	class ConversationServerEventStrategy extends ClientHandledEventStrategy {
		@Override
		void execute(ClientHandledEvent clientHandledObject) {
			ConversationServerEvent conversationObject = (ConversationServerEvent) clientHandledObject;
			updateUserConversationAndList(conversationObject);
		}
	}

	/**
	 * �����κ��� ������ ó���Ͽ� ǥ�� �� ���ִ� ������ �����ϴ� ���� Ŭ����
	 */
	class MessageServerEventStrategy extends ClientHandledEventStrategy {
		@Override
		void execute(ClientHandledEvent clientHandledObject) {
			AlertServerEvent messageObject = (AlertServerEvent) clientHandledObject;
			createOrJoinRoomView.displayMessage(messageObject);
		}
	}

	/**
	 * ǥ�õ� ��ȭ �� Ȱ�� ����ڸ� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param conversationServerEvent
	 */
	private void updateUserConversationAndList(ConversationServerEvent conversationServerEvent) {
		updateConversation(conversationServerEvent);
		updateUserList(conversationServerEvent);
	}

	/**
	 * ǥ�õ� ȣ���� ���� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param conversationServerEvent
	 */
	private void updateConversation(ConversationServerEvent conversationServerEvent) {
		addUsersNicksToMessage(conversationServerEvent);
		HashSet<MessageData> allMessages = gatherAllMessages(conversationServerEvent);
		ArrayList<MessageData> sortedMessages = sortAllMessages(allMessages);
		String conversationToDisplay = sortedMessagesToString(sortedMessages);

		mainChatView.updateUsersConversation(conversationToDisplay);
	}

	/**
	 * ǥ�� �� �Ŀ� �޽����� �ĺ� �� �� �ֵ��� �� �޽����� ������� �̸��� �߰��ϴ� �޼ҵ�
	 * 
	 * @param conversationServerEvent
	 */
	void addUsersNicksToMessage(ConversationServerEvent conversationServerEvent) {
		HashSet<UserData> userDataSet = conversationServerEvent.getRoom().getUserSet();

		for (UserData userData : userDataSet) {
			for (MessageData messageData : userData.getUsersMessages()) {
				messageData.setUserMessage(userData.getUserIdData().getUserName() + ":" + messageData.getMessage());
			}
		}
	}

	/**
	 * �濡�ִ� ������� ��� �޽����� ������ �޼ҵ�
	 * 
	 * @param conversationServerEvent
	 * @return conversation
	 */
	HashSet<MessageData> gatherAllMessages(ConversationServerEvent conversationServerEvent) {
		HashSet<MessageData> conversation = new HashSet<MessageData>();

		for (UserData userData : conversationServerEvent.getRoom().getUserSet()) {
			conversation.addAll(userData.getUsersMessages());
		}
		return conversation;
	}

	/**
	 * ����ڰ� ���� ��¥�� ��� �޽����� �����ϴ� �޼ҵ�
	 * 
	 * @param usersMessages
	 * 
	 * @return sortedMessages
	 */
	ArrayList<MessageData> sortAllMessages(HashSet<MessageData> usersMessages) {
		ArrayList<MessageData> sortedMessages = new ArrayList<MessageData>();
		sortedMessages.addAll(usersMessages);
		Collections.sort(sortedMessages);
		return sortedMessages;
	}

	/**
	 * ���� �� �޽��� ������ ä�� â�� ǥ�� �� �غ� �� ���ڿ��� ��ȯ�ϴ� �޼ҵ�
	 * 
	 * @param sortedMessages
	 * 
	 * @return conversationToDisplay
	 */
	String sortedMessagesToString(ArrayList<MessageData> sortedMessages) {
		String conversationToDisplay = new String("");
		for (MessageData messageData : sortedMessages) {
			conversationToDisplay = conversationToDisplay + messageData.getMessage() + "\n";
		}
		return conversationToDisplay;
	}

	/**
	 * ä�ÿ��� ����� ����� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param conversationServerEvent
	 */
	void updateUserList(ConversationServerEvent conversationServerEvent) {
		String usersListToDisplay = new String("");

		List<String> userListToSort = new ArrayList<String>();

		/** ��� ����ڸ� ���� ��Ͽ� �߰� �� ����ڰ� Ȱ�� �������� Ȯ�� */
		for (UserData userData : conversationServerEvent.getRoom().getUserSet()) {
			if (userData.isActive() == true) {
				userListToSort.add(userData.getUserIdData().getUserName());
			}
		}

		Collections.sort(userListToSort);
		for (String imie : userListToSort) {
			usersListToDisplay = usersListToDisplay + imie + "\n";
		}
		mainChatView.updateUsersList(usersListToDisplay);
	}
}
