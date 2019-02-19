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
 * 창 표시 및 대화 수신을 담당
 */
public class ViewController {
	/** 새 룸 가입 또는 생성을위한 선택 창 */
	private CreateOrJoinRoomView createOrJoinRoomView;
	/** 기본 채팅 창 */
	private ChatRoomView chatRoomView;
	/** 컨트롤러가 처리 한 이벤트 이벤트를 던지는 블로킹 큐 */
	private final BlockingQueue<ClientSideEvent> eventQueue;

	private final Map<Class<? extends ServerSideEvent>, ServerSideStrategy> strategyMap;

	/**
	 * 지정된 파라미터에 근거하는 뷰를 작성하는 생성자
	 * 
	 * @param eventQueue
	 */
	public ViewController(BlockingQueue<ClientSideEvent> eventQueue) {
		this.createOrJoinRoomView = new CreateOrJoinRoomView(eventQueue);
		this.eventQueue = eventQueue;

		/** 전략 맵 생성 및 할당 */
		this.strategyMap = new HashMap<Class<? extends ServerSideEvent>, ServerSideStrategy>();
		this.strategyMap.put(ChatRoomViewBuildEvent.class, new ChatRoomViewBuildStrategy());
		this.strategyMap.put(ConversationBuildEvent.class, new ConversationBuildStrategy());
		this.strategyMap.put(AlertToClientEvent.class, new AlertToClientEventStrategy());
	}

	/**
	 * 상응하는 모형의 전략을 구현하는 책임을 지는 메소드
	 * 
	 * @param serverSideEventt
	 */
	public void executeServerSideEvent(ServerSideEvent serverSideEventt) {
		ServerSideStrategy serverSideEventStrategy = strategyMap.get(serverSideEventt.getClass());
		serverSideEventStrategy.execute(serverSideEventt);
	}

	/**
	 * 이벤트를 처리하는 전략 클래스의 추상 기본 클래스
	 */
	abstract class ServerSideStrategy {
		/**
		 * 주어진 이벤트의 서비스를 기술하는 추상 메소드.
		 * 
		 * @param ServerSideEvent
		 */
		abstract void execute(final ServerSideEvent serverSideEvent);
	}

	/**
	 * 방의 연결 또는 생성이 성공적으로 완료된 서버의 정보를 처리하는 전략을 설명하는 내부 클래스
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
	 * 대화 수락 및 사용자 목록 처리 방법을 설명하는 내부 클래스
	 */
	class ConversationBuildStrategy extends ServerSideStrategy {
		@Override
		void execute(ServerSideEvent serverObject) {
			ConversationBuildEvent conversationObject = (ConversationBuildEvent) serverObject;
			updateUserConversationAndList(conversationObject);
		}
	}
	
	/**
	 * 표시된 대화 및 활성 사용자를 업데이트하는 메소드
	 * 
	 * @param conversationBuildEvent
	 */
	private void updateUserConversationAndList(ConversationBuildEvent conversationBuildEvent) {
		updateConversation(conversationBuildEvent);
		updateUserList(conversationBuildEvent);
	}
	
	/**
	 * 표시된 호출을 직접 업데이트하는 메소드
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
	 * 표시 한 후에 메시지를 식별 할 수 있도록 각 메시지에 사용자의 이름을 추가하는 메소드
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
	 * 서버로부터 수신을 처리하여 표시 할 수있는 전략을 설명하는 내부 클래스
	 */
	class AlertToClientEventStrategy extends ServerSideStrategy {
		@Override
		void execute(ServerSideEvent serverObject) {
			AlertToClientEvent messageObject = (AlertToClientEvent) serverObject;
			createOrJoinRoomView.displayMessage(messageObject);
		}
	}

	/**
	 * 방에있는 사용자의 모든 메시지를 모으는 메소드
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
	void updateUserList(ConversationBuildEvent giveChattingInfoEvent) {
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
