package chattingServer.serverSideEvent;

import java.io.Serializable;

/**
 * 채팅창을 구성할 때 발생하는 이벤트
 */
public class ChatRoomViewBuildEvent extends ServerSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 감싸인 사용자 이름 */
	private final String userName;
	/** 가입한 방의 이름 */
	private final String roomName;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param userName
	 * @param roomName
	 */
	public ChatRoomViewBuildEvent(final String userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
	}

	/**
	 * userName를 반환
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
