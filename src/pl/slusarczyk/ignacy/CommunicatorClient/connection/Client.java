package pl.slusarczyk.ignacy.CommunicatorClient.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.view.View;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ClientHandledEvent;

/**
 * 
 * 서버와의 연결이 이루어지는 주 클라이언트 클래스 및 현재 연결에 대한 정보 저장
 */
public class Client {
	/** socket */
	private Socket socket;
	/** outputStream */
	private ObjectOutputStream outputStream;
	/** inputStream */
	private ObjectInputStream inputStream;

	/**
	 * 클라이언트가 서버에서 대상으로 추가되는 블로킹 큐
	 */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** view */
	private final View view;

	/**
	 * 주어진 인수에 기반하여 클라이언트를 생성하는 생성자
	 * 
	 * @param eventQueue
	 */
	public Client(final BlockingQueue<ServerHandledEvent> eventQueue, final String ipAdress, final int port, final View view) {

		this.eventQueue = eventQueue;
		this.view = view;

		try {
			this.socket = new Socket(ipAdress, port);
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			ListenFromServer listenFromServer = new ListenFromServer();
			listenFromServer.start();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public void listenEventAndSend() {

		while (true) {
			try {
				ServerHandledEvent serverHandeledEvent = eventQueue.take();

				try {
					outputStream.writeObject(serverHandeledEvent);
				} catch (IOException ex) {
					System.exit(1);
				}
			} catch (InterruptedException ex) {
				System.err.print(ex);
			}

		}
	}

	/**
	 * 연결을 안전하게 닫는 메소드
	 */
	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * 서버의 이벤트를 수신하는 내부 클래스
	 */
	public class ListenFromServer extends Thread {
		public void run() {
			System.out.println("서버에서 이벤트 수신이 시작되었습니다.");
			while (true) {
				try {
					ClientHandledEvent serverEvent = (ClientHandledEvent) inputStream.readObject();
					view.executeClientHandeledEvent(serverEvent);
				} catch (IOException e) {
					closeConnection();
					System.exit(1);
				} catch (ClassNotFoundException e) {
					System.err.println(e);
				}
			}
		}
	}
}
