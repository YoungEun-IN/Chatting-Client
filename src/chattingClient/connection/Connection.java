package chattingClient.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientEvent.ClientEvent;
import chattingClient.view.ViewController;
import chattingServer.serverEvent.ServerEvent;

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
	private final BlockingQueue<ClientEvent> eventQueue;
	/** viewController */
	private final ViewController viewController;

	/**
	 * �־��� �μ��� ����Ͽ� Ŭ���̾�Ʈ�� �����ϴ� ������
	 * 
	 * @param eventQueue
	 */
	public Connection(final BlockingQueue<ClientEvent> eventQueue, final String ipAddress, final int port, final ViewController viewController) {

		this.eventQueue = eventQueue;
		this.viewController = viewController;

		try {
			this.socket = new Socket(ipAddress, port);
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
				ClientEvent clientEvent = eventQueue.take();

				try {
					outputStream.writeObject(clientEvent);
				} catch (IOException ex) {
					System.exit(1);
				}
			} catch (InterruptedException ex) {
				System.err.print(ex);
			}

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
					ServerEvent serverEvent = (ServerEvent) inputStream.readObject();
					viewController.executeServerEvent(serverEvent);
				} catch (IOException e) {
					closeConnection();
					System.exit(1);
				} catch (ClassNotFoundException e) {
					System.err.println(e);
				}
			}
		}
		
		/**
		 * ������ �����ϰ� �ݴ� �޼ҵ�
		 */
		private void closeConnection() {
			try {
				System.out.println("������ �ݽ��ϴ�.");
				socket.close();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
