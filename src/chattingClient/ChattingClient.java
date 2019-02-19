package chattingClient;

import chattingClient.connection.Connection;

/**
 * 모든 구성 요소의 적절한 초기화를 담당
 */
public class ChattingClient {
	/**
	 * 모델, 이벤트 큐 및 컨트롤러를 생성합니다.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		Connection connection = Connection.createInst();
		connection.listenAndSendEvent();
	}
}
