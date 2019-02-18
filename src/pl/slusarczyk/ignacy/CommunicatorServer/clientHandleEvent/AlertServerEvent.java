package pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserName;

/**
 * �� Ŭ������ ����ڿ��� ������ ������ �� ���˴ϴ�.
 */
public class AlertServerEvent extends ClientHandleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ����ڿ��� ǥ�� �� �޽��� */
	private final String message;
	/** �޽����� ǥ�� �� ������� ���� ��Ű�� �̸��� ǥ�õǾ���մϴ�. */
	private final UserName userName;

	public AlertServerEvent(final String message, final UserName userName) {
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
	public UserName getUserName() {
		return userName;
	}
}
