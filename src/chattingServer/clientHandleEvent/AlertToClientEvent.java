package chattingServer.clientHandleEvent;

import java.io.Serializable;

/**
 * �� Ŭ������ ����ڿ��� ������ ������ �� ���˴ϴ�.
 */
public class AlertToClientEvent extends ClientHandleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ����ڿ��� ǥ�� �� �޽��� */
	private final String message;
	/** �޽����� ǥ�� �� ������� ���� ��Ű�� �̸��� ǥ�õǾ���մϴ�. */
	private final String userName;

	public AlertToClientEvent(final String message, final String userName) {
		this.message = message;
		this.userName = userName;
	}

	/**
	 * �޽��� ������ ��ȯ
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * ���� ����� userId�� ��ȯ
	 * 
	 * @return the userID
	 */
	public String getUserName() {
		return userName;
	}
}
