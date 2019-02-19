package chattingServer.serverEvent;

import java.io.Serializable;

import chattingServer.model.data.RoomData;

/**
 * 서버의 메시지 수신을 나타내는 클래스와 클라이언트 윈도우에서 대화를 업데이트해야하는 필요성, 새 메시지가 추가 된 방에 대한 압축 된 데이터를 보냅니다.
 */
public class ConversationBuildEvent extends ServerEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 방에 대한 감싸인 정보 */
	private final RoomData roomData;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param roomData
	 */
	public ConversationBuildEvent(final RoomData roomData) {
		this.roomData = roomData;
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
