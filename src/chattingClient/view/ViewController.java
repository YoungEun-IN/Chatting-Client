package chattingClient.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.model.data.MessageData;
import chattingServer.model.data.UserData;
import chattingServer.serverSideEvent.AlertToClientEvent;
import chattingServer.serverSideEvent.ChatRoomViewBuildEvent;
import chattingServer.serverSideEvent.ConversationBuildEvent;
import chattingServer.serverSideEvent.ServerSideEvent;

/**
 * â ǥ�� �� ��ȭ ������ ���
 */
public class ViewController {
	/** �� �� ���� �Ǵ� ���������� ���� â */
	private CreateOrJoinRoomView createOrJoinRoomView;
	/** �⺻ ä�� â */
	private ChatRoomView chatRoomView;
	/** ��Ʈ�ѷ��� ó�� �� �̺�Ʈ �̺�Ʈ�� ������ ���ŷ ť */
	private final BlockingQueue<ClientSideEvent> eventQueue;

	private final Map<Class<? extends ServerSideEvent>, ServerSideStrategy> strategyMap;

	/**
	 * ������ �Ķ���Ϳ� �ٰ��ϴ� �並 �ۼ��ϴ� ������
	 * 
	 * @param eventQueue
	 */
	public ViewController(BlockingQueue<ClientSideEvent> eventQueue) {
		this.createOrJoinRoomView = new CreateOrJoinRoomView(eventQueue);
		this.eventQueue = eventQueue;

		/** ���� �� ���� �� �Ҵ� */
		this.strategyMap = new HashMap<Class<? extends ServerSideEvent>, ServerSideStrategy>();
		this.strategyMap.put(ChatRoomViewBuildEvent.class, new ChatRoomViewBuildStrategy());
		this.strategyMap.put(ConversationBuildEvent.class, new ConversationBuildStrategy());
		this.strategyMap.put(AlertToClientEvent.class, new AlertToClientEventStrategy());
	}

	/**
	 * �����ϴ� ������ ������ �����ϴ� å���� ���� �޼ҵ�
	 * 
	 * @param serverSideEventt
	 */
	public void executeServerSideEvent(ServerSideEvent serverSideEventt) {
		ServerSideStrategy serverSideEventStrategy = strategyMap.get(serverSideEventt.getClass());
		serverSideEventStrategy.execute(serverSideEventt);
	}

	/**
	 * �̺�Ʈ�� ó���ϴ� ���� Ŭ������ �߻� �⺻ Ŭ����
	 */
	abstract class ServerSideStrategy {
		/**
		 * �־��� �̺�Ʈ�� ���񽺸� ����ϴ� �߻� �޼ҵ�.
		 * 
		 * @param ServerSideEvent
		 */
		abstract void execute(final ServerSideEvent serverSideEvent);
	}

	/**
	 * ���� ���� �Ǵ� ������ ���������� �Ϸ�� ������ ������ ó���ϴ� ������ �����ϴ� ���� Ŭ����
	 */
	class ChatRoomViewBuildStrategy extends ServerSideStrategy {

		@Override
		void execute(ServerSideEvent serverObject) {
			ChatRoomViewBuildEvent chatRoomViewBuildEvent = (ChatRoomViewBuildEvent) serverObject;
			chatRoomView = new ChatRoomView(eventQueue, chatRoomViewBuildEvent.getUserName(), chatRoomViewBuildEvent.getRoomName());
			createOrJoinRoomView.closeCreateRoomWindow();
			createOrJoinRoomView = null;
		}
	}

	/**
	 * ��ȭ ���� �� ����� ��� ó�� ����� �����ϴ� ���� Ŭ����
	 */
	class ConversationBuildStrategy extends ServerSideStrategy {
		@Override
		void execute(ServerSideEvent serverObject) {
			ConversationBuildEvent conversationObject = (ConversationBuildEvent) serverObject;
			updateUserConversationAndList(conversationObject);
		}
	}
	
	/**
	 * ǥ�õ� ��ȭ �� Ȱ�� ����ڸ� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param conversationBuildEvent
	 */
	private void updateUserConversationAndList(ConversationBuildEvent conversationBuildEvent) {
		updateConversation(conversationBuildEvent);
		updateUserList(conversationBuildEvent);
	}
	
	/**
	 * ǥ�õ� ȣ���� ���� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param conversationBuildEvent
	 */
	private void updateConversation(ConversationBuildEvent conversationBuildEvent) {
		addUserNameToMessage(conversationBuildEvent);
		HashSet<MessageData> allMessages = gatherAllMessages(conversationBuildEvent);
		ArrayList<MessageData> sortedMessages = sortAllMessages(allMessages);
		String conversationToDisplay = convertConversationToString(sortedMessages);
		
		chatRoomView.updateConversation(conversationToDisplay);
	}
	
	/**
	 * ǥ�� �� �Ŀ� �޽����� �ĺ� �� �� �ֵ��� �� �޽����� ������� �̸��� �߰��ϴ� �޼ҵ�
	 * 
	 * @param conversationBuildEvent
	 */
	private void addUserNameToMessage(ConversationBuildEvent conversationBuildEvent) {
		HashSet<UserData> userDataSet = conversationBuildEvent.getRoom().getUserSet();
		
		for (UserData userData : userDataSet) {
			for (MessageData messageData : userData.getUsersMessages()) {
				messageData.setMessage(userData.getUserName() + ":" + messageData.getMessage());
			}
		}
	}

	/**
	 * �����κ��� ������ ó���Ͽ� ǥ�� �� ���ִ� ������ �����ϴ� ���� Ŭ����
	 */
	class AlertToClientEventStrategy extends ServerSideStrategy {
		@Override
		void execute(ServerSideEvent serverObject) {
			AlertToClientEvent messageObject = (AlertToClientEvent) serverObject;
			createOrJoinRoomView.displayMessage(messageObject);
		}
	}

	/**
	 * �濡�ִ� ������� ��� �޽����� ������ �޼ҵ�
	 * 
	 * @param conversationBuildEvent
	 * @return conversation
	 */
	private HashSet<MessageData> gatherAllMessages(ConversationBuildEvent conversationBuildEvent) {
		HashSet<MessageData> conversation = new HashSet<MessageData>();

		for (UserData userData : conversationBuildEvent.getRoom().getUserSet()) {
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
	private ArrayList<MessageData> sortAllMessages(HashSet<MessageData> usersMessages) {
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
	private String convertConversationToString(ArrayList<MessageData> sortedMessages) {
		String conversationToDisplay = new String("");
		for (MessageData messageData : sortedMessages) {
			conversationToDisplay = conversationToDisplay + messageData.getMessage() + "\n";
		}
		return conversationToDisplay;
	}

	/**
	 * ä�ÿ��� ����� ����� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param giveChattingInfoEvent
	 */
	void updateUserList(ConversationBuildEvent giveChattingInfoEvent) {
		String usersListToDisplay = new String("");

		List<String> userListToSort = new ArrayList<String>();

		/** ��� ����ڸ� ���� ��Ͽ� �߰� �� ����ڰ� Ȱ�� �������� Ȯ�� */
		for (UserData userData : giveChattingInfoEvent.getRoom().getUserSet()) {
			if (userData.isActive()) {
				userListToSort.add(userData.getUserName());
			}
		}

		Collections.sort(userListToSort);
		for (String userName : userListToSort) {
			usersListToDisplay = usersListToDisplay + userName + "\n";
		}
		chatRoomView.updateUsersList(usersListToDisplay);
	}
}
