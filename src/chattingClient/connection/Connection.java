package chattingClient.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingClient.view.ViewController;
import chattingServer.serverSideEvent.ServerSideEvent;

/**
 * 
 * 서버와의 연결이 이루어지는 주 클라이언트 클래스 및 현재 연결에 대한 정보 저장
 */
public class Connection {
	/** socket */
	private Socket socket;
	/** outputStream */
	private ObjectOutputStream outputStream;
	/** inputStream */
	private ObjectInputStream inputStream;

	/**
	 * 클라이언트가 서버에서 대상으로 추가되는 블로킹 큐
	 */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** viewController */
	private final ViewController viewController;

	/**
	 * 주어진 인수에 기반하여 클라이언트를 생성하는 생성자
	 * 
	 * @param eventQueue
	 */
	public Connection(final BlockingQueue<ClientSideEvent> eventQueue, final String ipAddress, final int port, final ViewController viewController) {
		this.eventQueue = eventQueue;
		this.viewController = viewController;

		try {
			this.socket = new Socket(ipAddress, port);
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			ListenThread listenThread = new ListenThread();
			listenThread.start();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * 서버의 이벤트를 수신하는 내부 클래스
	 */
	private class ListenThread extends Thread {
		public void run() {
			System.out.println("서버에서 이벤트 수신이 시작되었습니다.");
			
			while (true) {
				try {
					ServerSideEvent serverSideEvent = (ServerSideEvent) inputStream.readObject();
					viewController.executeServerSideEvent(serverSideEvent);
				} catch (IOException e) {
					closeConnection();
					System.exit(1);
				} catch (ClassNotFoundException e) {
					System.err.println(e);
				}
			}
		}

		/**
		 * 연결을 안전하게 닫는 메소드
		 */
		private void closeConnection() {
			try {
				System.out.println("소켓을 닫습니다.");
				socket.close();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
	
	/**
	 * 서버의 이벤트를 수신하고 응답하는 메소드
	 */
	public void listenAndSendEvent() {
		
		while (true) {
			try {
				ClientSideEvent clientSideEvent = eventQueue.take();
				
				try {
					outputStream.writeObject(clientSideEvent);
				} catch (IOException ex) {
					System.err.println("IOException : " + ex);
					System.exit(1);
				}
			} catch (InterruptedException ex) {
				System.err.print("InterruptedException : " + ex);
			}
			
		}
	}
}
