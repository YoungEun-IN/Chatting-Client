package chattingClient.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingClient.view.ViewController;
import chattingServer.serverSideEvent.ServerSideEvent;

/**
 * 
 * Socekt �� ���� ������ ȣ��Ʈ���� ��θ� �����. ��ü�� ��ȯ�ϹǷ� ObjectIn(Out)putStream �� �����ش�.
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
	private final BlockingQueue<ClientSideEvent> eventQueue = new LinkedBlockingQueue<ClientSideEvent>();
	/** viewController */
	private final ViewController viewController = new ViewController(eventQueue);
	
	static Connection inst = null;

	public static Connection createInst() {
		if (inst == null)
			inst = new Connection();

		return inst;
	}

	/**
	 * �־��� �μ��� ����Ͽ� Ŭ���̾�Ʈ�� �����ϴ� ������
	 */
	public Connection() {
		try {
			this.socket = new Socket("localhost", 5000);
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			ListenThread listenThread = new ListenThread();
			listenThread.start();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	/**
	 * ������ �̺�Ʈ�� �����ϴ� ���� Ŭ����
	 */
	private class ListenThread extends Thread {
		public void run() {
			System.out.println("�������� Ŭ���̾�Ʈ �̺�Ʈ ������ �����߽��ϴ�.");

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

	/**
	 * ������ �̺�Ʈ�� �����ϰ� �����ϴ� �޼ҵ�
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
