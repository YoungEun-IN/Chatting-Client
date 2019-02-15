package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;

/**
 * 고객에게 보낸 사용자 이름을 래핑하는 클래스
 */
public class UserIdData implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userNameToDisplay */
	private final String userNameToDisplay;

	/**
	 * 지정된 파라미터에 근거 해 오브젝트를 생성하는 생성자
	 * 
	 * @param userName
	 */
	public UserIdData(final String userName) {
		this.userNameToDisplay = userName;
	}

	/**
	 * 사용자가 보낸 메시지와 일치하도록 사용자의 이름을 반환
	 * 
	 * @return userNameToDisplay
	 */
	public String getUserName() {
		return userNameToDisplay;
	}
}
