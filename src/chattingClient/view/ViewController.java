package chattingClient.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import chattingClient.serverHandleEvent.ServerHandleEvent;
import chattingServer.clientHandleEvent.AlertToClientEvent;
import chattingServer.clientHandleEvent.ChatRoomViewBuildEvent;
import chattingServer.clientHandleEvent.ClientHandleEvent;
import chattingServer.clientHandleEvent.GiveChattingInfoEvent;
import chattingServer.model.data.MessageData;
import chattingServer.model.data.UserData;

/**
 * â ǥ�� �� ��ȭ ������ ���
 */
public class ViewController {
	/** �� �� ���� �Ǵ� ���������� ���� â */
	private CreateOrJoinRoomView createOrJoinRoomView;
	/** �⺻ ä�� â */
	private ChatRoomView chatRoomView;
	/** ��Ʈ�ѷ��� ó�� �� �̺�Ʈ �̺�Ʈ�� ������ ���ŷ ť */
	private final BlockingQueue<ServerHandleEvent> eventQueue;

	private final Map<Class<? extends ClientHandleEvent>, ClientHandledEventStrategy> strategyMap;

	/**
	 * ������ �Ķ���Ϳ� �ٰ��ϴ� �並 �ۼ��ϴ� ������
	 * 
	 * @param eventQueue
	 */
	public ViewController(BlockingQueue<ServerHandleEvent> eventQueue) {
		this.createOrJoinRoomView = new CreateOrJoinRoomView(eventQueue);
		this.eventQueue = eventQueue;

		/** ���� �� ���� �� �Ҵ� */
		this.strategyMap = new HashMap<Class<? extends ClientHandleEvent>, ClientHandledEventStrategy>();
		this.strategyMap.put(ChatRoomViewBuildEvent.class, new AfterConnectionStrategy());
		this.strategyMap.put(GiveChattingInfoEvent.class, new ConversationServerEventStrategy());
		this.strategyMap.put(AlertToClientEvent.class, new MessageServerEventStrategy());
	}

	/**
	 * �����ϴ� ������ ������ �����ϴ� å���� ���� �޼ҵ�
	 * 
	 * @param clientHandleEventObject
	 */
	public void executeClientHandleEvent(ClientHandleEvent clientHandleEventObject) {
		ClientHandledEventStrategy clientHandleEventStrategy = strategyMap.get(clientHandleEventObject.getClass());
		clientHandleEventStrategy.execute((ClientHandleEvent) clientHandleEventObject);
	}

	/**
	 * �̺�Ʈ�� ó���ϴ� ���� Ŭ������ �߻� �⺻ Ŭ����
	 */
	abstract class ClientHandledEventStrategy {
		/**
		 * �־��� �̺�Ʈ�� ���񽺸� ����ϴ� �߻� �޼ҵ�.
		 * 
		 * @param ClientHandleEvent
		 */
		abstract void execute(final ClientHandleEvent clientHandleEvent);
	}

	/**
	 * ���� ���� �Ǵ� ������ ���������� �Ϸ�� ������ ������ ó���ϴ� ������ �����ϴ� ���� Ŭ����
	 */
	class AfterConnectionStrategy extends ClientHandledEventStrategy {

		@Override
		void execute(ClientHandleEvent clientHandledObject) {
			ChatRoomViewBuildEvent chatRoomViewBuildEvent = (ChatRoomViewBuildEvent) clientHandledObject;
			chatRoomView = new ChatRoomView(eventQueue, chatRoomViewBuildEvent.getUserName(), chatRoomViewBuildEvent.getRoomName());
			createOrJoinRoomView.closeCreateRoomWindow();
			createOrJoinRoomView = null;
		}
	}

	/**
	 * ��ȭ ���� �� ����� ��� ó�� ����� �����ϴ� ���� Ŭ����
	 */
	class ConversationServerEventStrategy extends ClientHandledEventStrategy {
		@Override
		void execute(ClientHandleEvent clientHandledObject) {
			GiveChattingInfoEvent conversationObject = (GiveChattingInfoEvent) clientHandledObject;
			updateUserConversationAndList(conversationObject);
		}
	}

	/**
	 * �����κ��� ������ ó���Ͽ� ǥ�� �� ���ִ� ������ �����ϴ� ���� Ŭ����
	 */
	class MessageServerEventStrategy extends ClientHandledEventStrategy {
		@Override
		void execute(ClientHandleEvent clientHandledObject) {
			AlertToClientEvent messageObject = (AlertToClientEvent) clientHandledObject;
			createOrJoinRoomView.displayMessage(messageObject);
		}
	}

	/**
	 * ǥ�õ� ��ȭ �� Ȱ�� ����ڸ� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param giveChattingInfoEvent
	 */
	private void updateUserConversationAndList(GiveChattingInfoEvent giveChattingInfoEvent) {
		updateConversation(giveChattingInfoEvent);
		updateUserList(giveChattingInfoEvent);
	}

	/**
	 * ǥ�õ� ȣ���� ���� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param giveChattingInfoEvent
	 */
	private void updateConversation(GiveChattingInfoEvent giveChattingInfoEvent) {
		addUserNameToMessage(giveChattingInfoEvent);
		HashSet<MessageData> allMessages = gatherAllMessages(giveChattingInfoEvent);
		ArrayList<MessageData> sortedMessages = sortAllMessages(allMessages);
		String conversationToDisplay = convertConversationToString(sortedMessages);

		chatRoomView.updateConversation(conversationToDisplay);
	}

	/**
	 * ǥ�� �� �Ŀ� �޽����� �ĺ� �� �� �ֵ��� �� �޽����� ������� �̸��� �߰��ϴ� �޼ҵ�
	 * 
	 * @param giveChattingInfoEvent
	 */
	private void addUserNameToMessage(GiveChattingInfoEvent giveChattingInfoEvent) {
		HashSet<UserData> userDataSet = giveChattingInfoEvent.getRoom().getUserSet();

		for (UserData userData : userDataSet) {
			for (MessageData messageData : userData.getUsersMessages()) {
				messageData.setMessage(userData.getUserName() + ":" + messageData.getMessage());
			}
		}
	}

	/**
	 * �濡�ִ� ������� ��� �޽����� ������ �޼ҵ�
	 * 
	 * @param giveChattingInfoEvent
	 * @return conversation
	 */
	private HashSet<MessageData> gatherAllMessages(GiveChattingInfoEvent giveChattingInfoEvent) {
		HashSet<MessageData> conversation = new HashSet<MessageData>();

		for (UserData userData : giveChattingInfoEvent.getRoom().getUserSet()) {
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
	void updateUserList(GiveChattingInfoEvent giveChattingInfoEvent) {
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
