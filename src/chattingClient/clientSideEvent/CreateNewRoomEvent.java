package chattingClient.clientSideEvent;

import java.io.Serializable;

/**
 * 유저가 새로운 방을 작성하기 위해서 버튼을 누르는 이벤트를 나타내는 클래스
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
