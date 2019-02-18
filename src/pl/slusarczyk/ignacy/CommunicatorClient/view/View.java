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
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.MainChatViewServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConversationServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.InfoServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.MessageData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;

/**
 * â ǥ�� �� ��ȭ ������ ���
 */
public class View {
	/** �� �� ���� �Ǵ� ���������� ���� â */
	private CreateJoinRoomWindow createJoinRoomView;
	/** �⺻ ä�� â */
	private MainChatWindow mainChatView;
	/** ��Ʈ�ѷ��� ó�� �� �̺�Ʈ �̺�Ʈ�� ������ ���ŷ ť */
	private final BlockingQueue<ServerHandledEvent> eventQueue;

	private final Map<Class<? extends ClientHandledEvent>, ClientHandeledEventStrategy> strategyMap;

	/**
	 * ������ �Ķ���Ϳ� �ٰ��ϴ� �並 �ۼ��ϴ� ������
	 * 
	 * @param eventQueue
	 */
	public View(BlockingQueue<ServerHandledEvent> eventQueue) {
		this.createJoinRoomView = new CreateJoinRoomWindow(eventQueue);
		this.eventQueue = eventQueue;

		/** ���� �� ���� �� �Ҵ� */
		this.strategyMap = new HashMap<Class<? extends ClientHandledEvent>, ClientHandeledEventStrategy>();
		this.strategyMap.put(MainChatViewServerEvent.class, new MainChatViewStrategy());
		this.strategyMap.put(ConversationServerEvent.class, new ConversationServerEventStrategy());
		this.strategyMap.put(InfoServerEvent.class, new MessageServerEventStrategy());
	}

	/**
	 * �����ϴ� ������ ������ �����ϴ� å���� ���� �޼ҵ�
	 * 
	 * @param clientHandeledEventObject
	 */
	public void executeClientHandeledEvent(ClientHandledEvent clientHandeledEventObject) {
		ClientHandeledEventStrategy clientHandeledEventStrategy = strategyMap.get(clientHandeledEventObject.getClass());
		clientHandeledEventStrategy.execute((ClientHandledEvent) clientHandeledEventObject);
	}

	/**
	 * �̺�Ʈ�� ó���ϴ� ���� Ŭ������ �߻� �⺻ Ŭ����
	 */
	abstract class ClientHandeledEventStrategy {
		/**
		 * �־��� �̺�Ʈ�� ���񽺸� ����ϴ� �߻� �޼ҵ�.
		 * 
		 * @param ClientHandledEvent
		 */
		abstract void execute(final ClientHandledEvent clientHandeledEventObject);
	}

	/**
	 * ���� ���� �Ǵ� ������ ���������� �Ϸ�� ������ ������ ó���ϴ� ������ �����ϴ� ���� Ŭ����
	 */
	class MainChatViewStrategy extends ClientHandeledEventStrategy {

		@Override
		void execute(ClientHandledEvent clientHandeledEvent) {
			MainChatViewServerEvent mainChatViewServerEvent = (MainChatViewServerEvent) clientHandeledEvent;
			mainChatView = new MainChatWindow(eventQueue, mainChatViewServerEvent.getUserIDData(), mainChatViewServerEvent.getRoomName());
			createJoinRoomView.closeCreateRoomWindow();
			createJoinRoomView = null;
		}
	}

	/**
	 * ��ȭ ���� �� ����� ��� ó�� ����� �����ϴ� ���� Ŭ����
	 */
	class ConversationServerEventStrategy extends ClientHandeledEventStrategy {
		@Override
		void execute(ClientHandledEvent clientHandeledEventObject) {
			ConversationServerEvent conversationObject = (ConversationServerEvent) clientHandeledEventObject;
			updateUserConversationAndList(conversationObject);
		}
	}

	/**
	 * �����κ��� ������ ó���Ͽ� ǥ�� �� ���ִ� ������ �����ϴ� ���� Ŭ����
	 */
	class MessageServerEventStrategy extends ClientHandeledEventStrategy {
		@Override
		void execute(ClientHandledEvent clientHandeledEventObject) {
			InfoServerEvent messageObject = (InfoServerEvent) clientHandeledEventObject;
			createJoinRoomView.displayInfoMessage(messageObject);
		}
	}

	/**************************** ������ ��� **********************/

	/**
	 * ǥ�õ� ��ȭ �� Ȱ�� ����ڸ� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param conversationServerEvent
	 */
	public void updateUserConversationAndList(ConversationServerEvent conversationServerEvent) {
		updateConversation(conversationServerEvent);
		updateUserList(conversationServerEvent);
	}

	/**
	 * ǥ�õ� ȣ���� ���� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param conversationServerEvent
	 */
	public void updateConversation(ConversationServerEvent conversationServerEvent) {
		addUsersNicksToMessage(conversationServerEvent);
		HashSet<MessageData> allMessages = gatherAllMessages(conversationServerEvent);
		ArrayList<MessageData> sortedMessages = sortAllMessages(allMessages);
		String conversationToDisplay = sortedMessagesToString(sortedMessages);

		mainChatView.updateUsersConversation(conversationToDisplay);
	}

	/**
	 * ǥ�� �� �Ŀ� �޽����� �ĺ� �� �� �ֵ��� �� �޽����� ������� �̸��� �߰��ϴ� �޼ҵ�
	 * 
	 * @param conversationInfo
	 */
	void addUsersNicksToMessage(ConversationServerEvent conversationInfo) {
		HashSet<UserData> userDataSet = conversationInfo.getRoom().getUserSet();

		for (UserData userData : userDataSet) {
			for (MessageData messageData : userData.getUsersMessages()) {
				messageData.setUserMessage(userData.getUserIdData().getUserName() + ":" + messageData.getMessage());
			}
		}
	}

	/**
	 * �濡�ִ� ������� ��� �޽����� ������ �޼ҵ�
	 * 
	 * @param conversationInfo
	 * @return conversation
	 */
	HashSet<MessageData> gatherAllMessages(ConversationServerEvent conversationInfo) {
		HashSet<MessageData> conversation = new HashSet<MessageData>();

		for (UserData userData : conversationInfo.getRoom().getUserSet()) {
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
