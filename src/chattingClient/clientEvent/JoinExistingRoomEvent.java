package chattingClient.serverHandleEvent;

import java.io.Serializable;

/**
 * 사용자가 방에 연결하기 위해 버튼을 누르는 이벤트를 설명하는 클래스
 */
public class JoinExistingRoomEvent extends ServerHandleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** roomName */
	private final String roomName;
	/** userName */
	private final String userName;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param roomName
	 * @param userId   ID
	 */
	public JoinExistingRoomEvent(final String roomName, final String userName) {
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
	 * 사용자의 패키지 이름을 반환
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
}
