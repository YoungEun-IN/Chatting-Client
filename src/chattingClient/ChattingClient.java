package chattingClient;

import chattingClient.connection.Connection;

/**
 * Connection 객체를 싱글톤으로 생성한다.
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
