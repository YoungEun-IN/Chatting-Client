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
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** viewController */
	private final ViewController viewController;

	/**
	 * �־��� �μ��� ����Ͽ� Ŭ���̾�Ʈ�� �����ϴ� ������
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
	 * ������ �̺�Ʈ�� �����ϴ� ���� Ŭ����
	 */
	private class ListenThread extends Thread {
		public void run() {
			System.out.println("�������� �̺�Ʈ ������ ���۵Ǿ����ϴ�.");
			
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
