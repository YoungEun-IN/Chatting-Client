package chattingClient.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import chattingClient.clientEvent.QuitChattingEvent;
import chattingClient.clientEvent.SendMessageEvent;
import chattingClient.clientEvent.ClientEvent;

/** �⺻ ä�� â�� ǥ���ϴ� Ŭ���� **/

class ChatRoomView {
	/** ���� ������ */
	private JFrame frame;
	/** ����� ���� ��ȭ�� ǥ�õǴ� ���� */
	private JTextArea conversationField;
	/** ����ڰ� �޽����� �Է��ϴ� ���� */
	private JTextArea userTextField;
	/** ä�� ����ڰ��ִ� ���� */
	private JTextArea onlineUsersField;
	/** �޽��� ���� ��ư */
	private JButton sendButton;
	/** ����ڰ� ����� ǥ���ϴ� ��ġ�� ��Ÿ���� ���̺� */
	private JLabel lblUsersInRoom;
	/** �ؽ�Ʈ�� ���� ���� ��ũ�ѷ� */
	private JScrollPane userConversationScroll;
	private JScrollPane userTextMessageScroll;
	private JScrollPane onlineUsersScroll;
	/** �� �̺�Ʈ�� �߰� �� ���ŷ ť */
	private final BlockingQueue<ClientEvent> eventQueue;
	/** ������ �ڽ��� �̺�Ʈ�� �ĺ��ϴ� �� ������ �� ������� ���� �� �̸� */
	private final String userName;
	/** ����ڰ��ִ� ���� �̸� */
	private final String roomName;

	/** ������ ���� �� ǥ�� */
	public ChatRoomView(final BlockingQueue<ClientEvent> eventQueue, final String userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
		this.eventQueue = eventQueue;

		initialize();
		frame.setVisible(true);
	}

	/**
	 * ������ ������ �ʱ�ȭ
	 */
	private void initialize() {
		/** ���� ������ �ʱ�ȭ */
		frame = new JFrame("ChatRoom");
		frame.setBounds(100, 100, 450, 320);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/** ä�� â�� Ŭ�� */
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				eventQueue.offer(new QuitChattingEvent(userName, roomName));
				System.out.println("����ڰ� ä�� â�� �ݾҽ��ϴ�");
				System.exit(0);
			}
		});

		/** ��ũ�ѷ��� �Բ� ����� ������ ����Ͽ� ��ȭ ������ �ʱ�ȭ */
		conversationField = new JTextArea();
		conversationField.setBounds(12, 12, 260, 189);
		conversationField.setEditable(false);
		userConversationScroll = new JScrollPane(conversationField);
		userConversationScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userConversationScroll.setBounds(12, 12, 260, 189);
		frame.getContentPane().add(userConversationScroll);

		/** ����ڰ� ��ũ�ѷ��� �޽����� �Է��ϴ� ���� �ʱ�ȭ */
		userTextField = new JTextArea();
		userTextField.setBounds(12, 213, 260, 56);
		userTextMessageScroll = new JScrollPane(userTextField);
		userTextMessageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userTextMessageScroll.setBounds(12, 213, 260, 56);
		frame.getContentPane().add(userTextMessageScroll);

		/** ����� ����� ��ũ�ѷ��� �Բ� ǥ���ϴ� ���� �ʱ�ȭ */
		onlineUsersField = new JTextArea();
		onlineUsersField.setBounds(284, 34, 154, 184);
		onlineUsersField.setEditable(false);
		onlineUsersScroll = new JScrollPane(onlineUsersField);
		onlineUsersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineUsersScroll.setBounds(284, 34, 154, 184);
		frame.getContentPane().add(onlineUsersScroll);

		/** ��ư �ʱ�ȭ */
		sendButton = new JButton("Send message");
		sendButton.setBounds(288, 224, 117, 25);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventQueue.offer(new SendMessageEvent(roomName, userName, userTextField.getText()));
				userTextField.setText("");
			}
		});

		frame.getContentPane().add(sendButton);

		/** �޽����� ���̺� �ʱ�ȭ */
		lblUsersInRoom = new JLabel("Users in room");
		lblUsersInRoom.setBounds(300, 4, 126, 30);
		frame.getContentPane().add(lblUsersInRoom);
	}

	/**
	 * ������� ��ȭ�� ǥ�õǴ� â�� ������Ʈ�ϴ� �޼���
	 * 
	 * @param conversationToDisplay
	 */
	public void updateConversation(final String conversationToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				conversationField.setText("");
				conversationField.append(conversationToDisplay);
			}
		});
	}

	/**
	 * Ȱ�� ������� ǥ�õ� ����� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param userListToDisplay
	 */
	public void updateUsersList(final String userListToDisplay) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				onlineUsersField.setText("");
				onlineUsersField.append(userListToDisplay);
			}
		});
	}
}