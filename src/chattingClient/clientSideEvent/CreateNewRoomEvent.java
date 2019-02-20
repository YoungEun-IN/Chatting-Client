package chattingClient.clientSideEvent;

import java.io.Serializable;

/**
 * 새로 방을 생성할 때 발생하는 이벤트
 */
public class CreateNewRoomEvent extends ClientSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 방의 이름 */
	private final String roomName;
	/** userName */
	private final String userName;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param roomName
	 * @param userID
	 */
	public CreateNewRoomEvent(final String roomName, final String userName) {
		this.roomName = roomName;
		this.userName = userName;
	}

	/**
	 * 방 이름을 반환
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * 사용자의 ID를 반환
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
}
