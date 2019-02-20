package chattingServer.serverSideEvent;

import java.io.Serializable;

import chattingServer.model.data.RoomData;

/**
 * 대화를 구성할 때 발생하는 이벤트
 */
public class ConversationBuildEvent extends ServerSideEvent implements Serializable {
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
