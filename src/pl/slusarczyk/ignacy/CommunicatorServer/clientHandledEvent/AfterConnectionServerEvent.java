package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * 클라이언트 응용 프로그램에서 기본 대화창을 열 가능성을 알리는 클래스
 */
public class AfterConnectionServerEvent extends ClientHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 감싸인 사용자 이름 */
	private final UserIdData userIDData;
	/** 가입한 방의 이름 */
	private final String roomName;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param userIdData
	 * @param roomName
	 */
	public AfterConnectionServerEvent(final UserIdData userIdData, final String roomName) {
		this.userIDData = userIdData;
		this.roomName = roomName;
	}

	/**
	 * userIDData를 반환
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIDData() {
		return userIDData;
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
