package chattingClient;

import chattingClient.connection.Connection;

/**
 * ��� ���� ����� ������ �ʱ�ȭ�� ���
 */
public class ChattingClient {
	/**
	 * ��, �̺�Ʈ ť �� ��Ʈ�ѷ��� �����մϴ�.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Connection connection = Connection.createInst();
		connection.listenAndSendEvent();
	}
}
