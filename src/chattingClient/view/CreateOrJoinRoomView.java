package chattingClient.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import chattingClient.clientEvent.CreateNewRoomEvent;
import chattingClient.clientEvent.JoinExistingRoomEvent;
import chattingClient.clientEvent.ClientEvent;
import chattingServer.serverEvent.AlertToClientEvent;

/**
 * ���� ����ų� �����ϴ� â�� ����ϴ� Ŭ����
 **/
class CreateOrJoinRoomView {
	/** ���� ������ */
	final JFrame frame;

	/** ����� �̸� �Է� �ʵ� */
	final JTextField userNameField;
	/** �� �̸� �Է� �ʵ� */
	final JTextField roomNameField;
	/** �Է� �� ������ Ȯ���ϰ� �� ���� ����� ��ư */
	JButton submitButtonAndJoinRoom;
	/** �Է� �� ������ Ȯ���ϰ� �� �濡 �����ϴ� ��ư */
	JButton submitButtonAndCreateRoom;
	/** �� �̺�Ʈ�� �߰� �� ���ŷ ť */
	private final BlockingQueue<ClientEvent> eventQueue;

	public CreateOrJoinRoomView(final BlockingQueue<ClientEvent> eventQueueObject) {
		this.eventQueue = eventQueueObject;

		/** �⺻ â ����� */
		frame = new JFrame("Create or join room");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400, 150));

		/** �ùٸ� ���̾ƿ��� ���� */
		JPanel container = new JPanel();
		BoxLayout layout = new BoxLayout(container, BoxLayout.Y_AXIS);
		container.setLayout(layout);

		/** ���ο� ���� ��������� ��ư�� �ʱ�ȭ */
		submitButtonAndJoinRoom = new JButton("Create");
		submitButtonAndJoinRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventQueue.offer(new CreateNewRoomEvent(roomNameField.getText(), userNameField.getText()));
			}
		});

		/** ��ư�� �ʱ�ȭ�Ͽ� ���� ȸ�ǽǿ� ���� */
		submitButtonAndCreateRoom = new JButton("Join");
		submitButtonAndCreateRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventQueue.offer(new JoinExistingRoomEvent(roomNameField.getText(), userNameField.getText()));
			}
		});

		userNameField = new JTextField();
		roomNameField = new JTextField();

		/** ��� ����� ��ġ�� ���� */
		userNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		roomNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitButtonAndJoinRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitButtonAndCreateRoom.setAlignmentX(Component.CENTER_ALIGNMENT);

		/** ���� �ʵ忡 �Է� �� �ؽ�Ʈ�� ���� */
		userNameField.setText("userName");
		roomNameField.setText("roomName");

		/** ��� �׸��� �����̳ʿ� �߰� */
		container.add(userNameField);
		container.add(roomNameField);
		container.add(submitButtonAndJoinRoom);
		container.add(submitButtonAndCreateRoom);

		/** �����̳ʸ� ���� �����ӿ� �߰��ϰ� ���� �������� ���� */
		frame.add(container);
		frame.setVisible(true);
	}

	/**
	 * �����츦 �ݴ� �޼ҵ�
	 */
	public void closeCreateRoomWindow() {
		frame.setVisible(false);
		frame.dispose();
	}

	/**
	 * �������� ���� ������ ǥ���ϴ� �޼ҵ�
	 * 
	 * @param messageObject
	 */
	public void displayMessage(final AlertToClientEvent messageObject) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(frame, messageObject.getMessage());
			}
		});
	}
}
