package pl.slusarczyk.ignacy.CommunicatorClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.connection.Client;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
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
		BlockingQueue<ServerHandledEvent> eventQueue = new LinkedBlockingQueue<ServerHandledEvent>();
		View view = new View(eventQueue);
		Client client = new Client(eventQueue,"localhost", 5000, view);
		client.listenEventAndSend();
	}
}
