package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;

/**
 * ������ �޽��� ������ ��Ÿ���� Ŭ������ Ŭ���̾�Ʈ �����쿡�� ��ȭ�� ������Ʈ�ؾ��ϴ� �ʿ伺, �� �޽����� �߰� �� �濡 ���� ���� �� �����͸� �����ϴ�.
 */
public class ConversationServerEvent extends ClientHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** �濡 ���� ������ ���� */
	private final RoomData roomData;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomData
	 */
	public ConversationServerEvent(final RoomData roomData) {
		this.roomData = roomData;
	}

	/**
	 * �濡 ���� ��Ű�� �����͸� ��ȯ
	 * 
	 * @return roomData
	 */
	public RoomData getRoom() {
		return roomData;
	}
}
