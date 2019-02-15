package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;

/**
 * ������ ���� ����� �̸��� �����ϴ� Ŭ����
 */
public class UserIdData implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userNameToDisplay */
	private final String userNameToDisplay;

	/**
	 * ������ �Ķ���Ϳ� �ٰ� �� ������Ʈ�� �����ϴ� ������
	 * 
	 * @param userName
	 */
	public UserIdData(final String userName) {
		this.userNameToDisplay = userName;
	}

	/**
	 * ����ڰ� ���� �޽����� ��ġ�ϵ��� ������� �̸��� ��ȯ
	 * 
	 * @return userNameToDisplay
	 */
	public String getUserName() {
		return userNameToDisplay;
	}
}
