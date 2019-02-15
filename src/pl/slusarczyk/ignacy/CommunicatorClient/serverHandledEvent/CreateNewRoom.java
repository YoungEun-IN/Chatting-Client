package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * 유저가 새로운 방을 작성하기 위해서 버튼을 누르는 이벤트를 나타내는 클래스
 */
public class CreateNewRoom extends ServerHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 방의 이름 */
	private final String roomName;
	/** userIDData */
	private final UserIdData userIDData;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param roomName
	 * @param userID
	 */
	public CreateNewRoom(final String roomName, final UserIdData userIDData) {
		this.roomName = roomName;
		this.userIDData = userIDData;
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
	 * @return userIDData
	 */
	public UserIdData getUserIdData() {
		return userIDData;
	}
}
