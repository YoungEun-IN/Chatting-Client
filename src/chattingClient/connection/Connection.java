package chattingClient.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import chattingClient.serverHandleEvent.ServerHandleEvent;
import chattingClient.view.ViewController;
import chattingServer.clientHandleEvent.ClientHandleEvent;

/**
 * 
 * �������� ������ �̷������ �� Ŭ���̾�Ʈ Ŭ���� �� ���� ���ῡ ���� ���� ����
 */
public class Connection {
	/** socket */
	private Socket socket;
	/** outputStream */
	private ObjectOutputStream outputStream;
	/** inputStream */
	private ObjectInputStream inputStream;

	/**
	 * Ŭ���̾�Ʈ�� �������� ������� �߰��Ǵ� ���ŷ ť
	 */
	private final BlockingQueue<ServerHandleEvent> eventQueue;
	/** view */
	private final ViewController view;

	/**
	 * �־��� �μ��� ����Ͽ� Ŭ���̾�Ʈ�� �����ϴ� ������
	 * 
	 * @param eventQueue
	 */
	public Connection(final BlockingQueue<ServerHandleEvent> eventQueue, final String ipAdress, final int port, final ViewController view) {

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
				ServerHandleEvent serverHandleEvent = eventQueue.take();

				try {
					outputStream.writeObject(serverHandleEvent);
				} catch (IOException ex) {
					System.exit(1);
				}
			} catch (InterruptedException ex) {
				System.err.print(ex);
			}

		}
	}

	/**
	 * ������ �����ϰ� �ݴ� �޼ҵ�
	 */
	public void closeConnection() {
		try {
			socket.close();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * ������ �̺�Ʈ�� �����ϴ� ���� Ŭ����
	 */
	public class ListenFromServer extends Thread {
		public void run() {
			System.out.println("�������� �̺�Ʈ ������ ���۵Ǿ����ϴ�.");
			while (true) {
				try {
					ClientHandleEvent serverEvent = (ClientHandleEvent) inputStream.readObject();
					view.executeClientHandleEvent(serverEvent);
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
