package chattingClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.connection.Connection;
import chattingClient.clientEvent.ClientEvent;
import chattingClient.view.ViewController;

/**
 * ��� ���� ����� ������ �ʱ�ȭ�� ���
 */
public class CommunicatorClient 
{
	/**
	 * ��, �̺�Ʈ ť �� ��Ʈ�ѷ��� �����մϴ�.
	 * 
	 * @param args 
	 */
	public static void main(String args[])
	{
		BlockingQueue<ClientEvent> eventQueue = new LinkedBlockingQueue<ClientEvent>();
		ViewController view = new ViewController(eventQueue);
		Connection client = new Connection(eventQueue,"localhost", 5000, view);
		client.listenEventAndSend();
	}
}
