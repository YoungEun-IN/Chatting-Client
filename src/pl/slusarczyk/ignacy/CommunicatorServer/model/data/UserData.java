package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * 사용자 패키지 클래스
 */
public class UserData implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userName */
	private final String userName;
	/** 사용자의 패키지 메시지 세트 */
	private final HashSet<MessageData> usersMessages;
	/** 사용자가 활성 상태인지 여부를 나타내는 플래그 */
	private final boolean isActive;

	public UserData(final String userName, final HashSet<MessageData> userMessages, final boolean isActive) {
		this.userName = userName;
		this.usersMessages = userMessages;
		this.isActive = isActive;
	}

	/**
	 * 사용자의 userID를 반환
	 * 
	 * @return userId ID
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * 사용자의 패키지 메시지 집합을 반환
	 * 
	 * @return usersMessages
	 */
	public HashSet<MessageData> getUsersMessages() {
		return usersMessages;
	}

	/**
	 * 사용자가 활성 상태인지 여부에 대한 정보를 반환
	 * 
	 * @return isActive
	 */
	public boolean isActive() {
		return isActive;
	}
}
