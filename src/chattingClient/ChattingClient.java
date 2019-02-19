package chattingClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.connection.Connection;
import chattingClient.clientSideEvent.ClientSideEvent;
import chattingClient.view.ViewController;

/**
 * ��� ���� ����� ������ �ʱ�ȭ�� ���
 */
public class ChattingClient 
{
	/**
	 * ��, �̺�Ʈ ť �� ��Ʈ�ѷ��� �����մϴ�.
	 * 
	 * @param args 
	 */
	public static void main(String args[])
	{
		BlockingQueue<ClientSideEvent> eventQueue = new LinkedBlockingQueue<ClientSideEvent>();
		ViewController viewController = new ViewController(eventQueue);
		Connection connection = new Connection(eventQueue,"localhost", 5000, viewController);
		connection.listenAndSendEvent();
	}
}
