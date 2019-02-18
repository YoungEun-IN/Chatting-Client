package pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserName;

/**
 * 유저가 새로운 방을 작성하기 위해서 버튼을 누르는 이벤트를 나타내는 클래스
 */
public class CreateNewRoomEvent extends ServerHandleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 방의 이름 */
	private final String roomName;
	/** userName */
	private final UserName userName;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param roomName
	 * @param userID
	 */
	public CreateNewRoomEvent(final String roomName, final UserName userName) {
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
	public UserName getUserName() {
		return userName;
	}
}
