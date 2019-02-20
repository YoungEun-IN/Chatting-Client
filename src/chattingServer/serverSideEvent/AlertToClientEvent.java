package chattingServer.serverSideEvent;

import java.io.Serializable;

/**
 * 사용자에게 알림을 줄 때 발생하는 이벤트
 */
public class AlertToClientEvent extends ServerSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 사용자에게 표시 될 메시지 */
	private final String message;
	/** 메시지가 표시 될 사용자의 사전 패키지 이름이 표시되어야합니다. */
	private final String userName;

	public AlertToClientEvent(final String message, final String userName) {
		this.message = message;
		this.userName = userName;
	}

	/**
	 * 메시지 내용을 반환
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 보낸 사람의 userId를 반환
	 * 
	 * @return the userID
	 */
	public String getUserName() {
		return userName;
	}
}
