package chattingClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.connection.Connection;
import chattingClient.serverHandleEvent.ServerHandleEvent;
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
		BlockingQueue<ServerHandleEvent> eventQueue = new LinkedBlockingQueue<ServerHandleEvent>();
		ViewController view = new ViewController(eventQueue);
		Connection client = new Connection(eventQueue,"localhost", 5000, view);
		client.listenEventAndSend();
	}
}
