package chattingClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.connection.Connection;
import chattingClient.clientEvent.ClientEvent;
import chattingClient.view.ViewController;

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
		BlockingQueue<ClientEvent> eventQueue = new LinkedBlockingQueue<ClientEvent>();
		ViewController view = new ViewController(eventQueue);
		Connection client = new Connection(eventQueue,"localhost", 5000, view);
		client.listenEventAndSend();
	}
}
