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
 * �������� ������ �̷������ �� Ŭ���̾�Ʈ Ŭ���� �� ���� ���ῡ ���� ���� ����
 */
public class Client {
	/** socket */
	private Socket socket;
	/** outputStream */
	private ObjectOutputStream outputStream;
	/** inputStream */
	private ObjectInputStream inputStream;

	/**
	 * Ŭ���̾�Ʈ�� �������� ������� �߰��Ǵ� ���ŷ ť
	 */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** view */
	private final View view;

	/**
	 * �־��� �μ��� ����Ͽ� Ŭ���̾�Ʈ�� �����ϴ� ������
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
