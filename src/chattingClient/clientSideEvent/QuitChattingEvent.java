package chattingClient.clientSideEvent;

import java.io.Serializable;

/**
 * 사용자가 방을 나갔을 때 발생하는 이벤트
 */
public class QuitChattingEvent extends ClientSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 채팅을 떠난 사용자의 이름 */
	private final String userName;
	/** 사용자를 찾을 방의 이름 */
	private final String roomName;

	/**
	 * 주어진 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param userName
	 * @param roomName
	 */
	public QuitChattingEvent(final String userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
	}

	/**
	 * 사용자의 이름을 반환
	 *
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 방 이름을 반환
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}
}
