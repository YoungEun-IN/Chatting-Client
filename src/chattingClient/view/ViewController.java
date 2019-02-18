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
 * 창 표시 및 대화 수신을 담당
 */
public class ViewController {
	/** 새 룸 가입 또는 생성을위한 선택 창 */
	private CreateOrJoinRoomView createOrJoinRoomView;
	/** 기본 채팅 창 */
	private ChatRoomView chatRoomView;
	/** 컨트롤러가 처리 한 이벤트 이벤트를 던지는 블로킹 큐 */
	private final BlockingQueue<ServerHandleEvent> eventQueue;

	private final Map<Class<? extends ClientHandleEvent>, ClientHandledEventStrategy> strategyMap;

	/**
	 * 지정된 파라미터에 근거하는 뷰를 작성하는 생성자
	 * 
	 * @param eventQueue
	 */
	public ViewController(BlockingQueue<ServerHandleEvent> eventQueue) {
		this.createOrJoinRoomView = new CreateOrJoinRoomView(eventQueue);
		this.eventQueue = eventQueue;

		/** 전략 맵 생성 및 할당 */
		this.strategyMap = new HashMap<Class<? extends ClientHandleEvent>, ClientHandledEventStrategy>();
		this.strategyMap.put(ChatRoomViewBuildEvent.class, new AfterConnectionStrategy());
		this.strategyMap.put(GiveChattingInfoEvent.class, new ConversationServerEventStrategy());
		this.strategyMap.put(AlertToClientEvent.class, new MessageServerEventStrategy());
	}

	/**
	 * 상응하는 모형의 전략을 구현하는 책임을 지는 메소드
	 * 
	 * @param clientHandleEventObject
	 */
	public void executeClientHandleEvent(ClientHandleEvent clientHandleEventObject) {
		ClientHandledEventStrategy clientHandleEventStrategy = strategyMap.get(clientHandleEventObject.getClass());
		clientHandleEventStrategy.execute((ClientHandleEvent) clientHandleEventObject);
	}

	/**
	 * 이벤트를 처리하는 전략 클래스의 추상 기본 클래스
	 */
	abstract class ClientHandledEventStrategy {
		/**
		 * 주어진 이벤트의 서비스를 기술하는 추상 메소드.
		 * 
		 * @param ClientHandleEvent
		 */
		abstract void execute(final ClientHandleEvent clientHandleEvent);
	}

	/**
	 * 방의 연결 또는 생성이 성공적으로 완료된 서버의 정보를 처리하는 전략을 설명하는 내부 클래스
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
	 * 대화 수락 및 사용자 목록 처리 방법을 설명하는 내부 클래스
	 */
	class ConversationServerEventStrategy extends ClientHandledEventStrategy {
		@Override
		void execute(ClientHandleEvent clientHandledObject) {
			GiveChattingInfoEvent conversationObject = (GiveChattingInfoEvent) clientHandledObject;
			updateUserConversationAndList(conversationObject);
		}
	}

	/**
	 * 서버로부터 수신을 처리하여 표시 할 수있는 전략을 설명하는 내부 클래스
	 */
	class MessageServerEventStrategy extends ClientHandledEventStrategy {
		@Override
		void execute(ClientHandleEvent clientHandledObject) {
			AlertToClientEvent messageObject = (AlertToClientEvent) clientHandledObject;
			createOrJoinRoomView.displayMessage(messageObject);
		}
	}

	/**
	 * 표시된 대화 및 활성 사용자를 업데이트하는 메소드
	 * 
	 * @param giveChattingInfoEvent
	 */
	private void updateUserConversationAndList(GiveChattingInfoEvent giveChattingInfoEvent) {
		updateConversation(giveChattingInfoEvent);
		updateUserList(giveChattingInfoEvent);
	}

	/**
	 * 표시된 호출을 직접 업데이트하는 메소드
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
	 * 표시 한 후에 메시지를 식별 할 수 있도록 각 메시지에 사용자의 이름을 추가하는 메소드
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
	 * 방에있는 사용자의 모든 메시지를 모으는 메소드
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
	 * 사용자가 만든 날짜의 모든 메시지를 정렬하는 메소드
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
	 * 정렬 된 메시지 집합을 채팅 창에 표시 할 준비가 된 문자열로 변환하는 메소드
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
	 * 채팅에서 사용자 목록을 업데이트하는 메소드
	 * 
	 * @param giveChattingInfoEvent
	 */
	void updateUserList(GiveChattingInfoEvent giveChattingInfoEvent) {
		String usersListToDisplay = new String("");

		List<String> userListToSort = new ArrayList<String>();

		/** 모든 사용자를 거쳐 목록에 추가 된 사용자가 활성 상태인지 확인 */
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
