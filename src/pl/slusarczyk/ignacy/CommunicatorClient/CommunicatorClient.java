package pl.slusarczyk.ignacy.CommunicatorClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.connection.Connection;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.ServerHandleEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.view.View;

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
		View view = new View(eventQueue);
		Connection client = new Connection(eventQueue,"localhost", 5000, view);
		client.listenEventAndSend();
	}
}
