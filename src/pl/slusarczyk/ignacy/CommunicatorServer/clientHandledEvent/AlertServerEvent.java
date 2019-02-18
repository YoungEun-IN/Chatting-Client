package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * �� Ŭ������ ����ڿ��� ������ ������ �� ���˴ϴ�.
 */
public class AlertServerEvent extends ClientHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ����ڿ��� ǥ�� �� �޽��� */
	private final String message;
	/** �޽����� ǥ�� �� ������� ���� ��Ű�� �̸��� ǥ�õǾ���մϴ�. */
	private final UserIdData userIDData;

	public AlertServerEvent(final String message, final UserIdData userIDData) {
		this.message = message;
		this.userIDData = userIDData;
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
	public UserIdData getUserIDData() {
		return userIDData;
	}
}
