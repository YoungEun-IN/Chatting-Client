package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;

/**
 * 서버의 메시지 수신을 나타내는 클래스와 클라이언트 윈도우에서 대화를 업데이트해야하는 필요성, 새 메시지가 추가 된 방에 대한 압축 된 데이터를 보냅니다.
 */
public class ConversationServerEvent extends ClientHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 방에 대한 감싸인 정보 */
	private final RoomData roomData;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param userConversation 사용자 간의 대화
	 * @param listOfUsers      현재 채팅중인 사용자 목록
	 */
	public ConversationServerEvent(final RoomData room) {
		this.roomData = room;
	}

	/**
	 * 방에 대한 패키지 데이터를 반환
	 * 
	 * @return roomData
	 */
	public RoomData getRoom() {
		return roomData;
	}
}
