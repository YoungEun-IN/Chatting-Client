package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;

/**
 * 고객에게 보낸 사용자 이름을 래핑하는 클래스
 */
public class UserName implements Serializable {
	private static final long serialVersionUID = 1L;

	/** userName */
	private final String userName;

	/**
	 * 지정된 파라미터에 근거해 오브젝트를 생성하는 생성자
	 * 
	 * @param userName
	 */
	public UserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * 사용자가 보낸 메시지와 일치하도록 사용자의 이름을 반환
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
}
