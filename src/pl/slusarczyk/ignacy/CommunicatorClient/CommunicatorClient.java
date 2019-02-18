package pl.slusarczyk.ignacy.CommunicatorClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.connection.Connection;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.ServerHandleEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.view.View;

/**
 * 모든 구성 요소의 적절한 초기화를 담당
 */
public class CommunicatorClient 
{
	/**
	 * 모델, 이벤트 큐 및 컨트롤러를 생성합니다.
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
