package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;

/**
 * ������ ���� ����� �̸��� �����ϴ� Ŭ����
 */
public class UserIdData implements Serializable {
	private static final long serialVersionUID = 1L;

	/** userName */
	private final String userName;

	/**
	 * ������ �Ķ���Ϳ� �ٰ��� ������Ʈ�� �����ϴ� ������
	 * 
	 * @param userName
	 */
	public UserIdData(final String userName) {
		this.userName = userName;
	}

	/**
	 * ����ڰ� ���� �޽����� ��ġ�ϵ��� ������� �̸��� ��ȯ
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
}
