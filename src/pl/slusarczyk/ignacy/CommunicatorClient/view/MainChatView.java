package pl.slusarczyk.ignacy.CommunicatorClient.view;

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

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.QuitChattingEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.SendMessageEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.ServerHandleEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserName;

/** �⺻ ä�� â�� ǥ���ϴ� Ŭ���� **/

class MainChatView {
	/** ���� ������ */
	private JFrame frame;
	/** ����� ���� ��ȭ�� ǥ�õǴ� ���� */
	private JTextArea usersConversation;
	/** ����ڰ� �޽����� �Է��ϴ� ���� */
	private JTextArea userTextfield;
	/** ä�� ����ڰ��ִ� ���� */
	private JTextArea onlineUsers;
	/** �޽��� ���� ��ư */
	private JButton sendButton;
	/** ����ڰ� ����� ǥ���ϴ� ��ġ�� ��Ÿ���� ���̺� */
	private JLabel lblUsersInRoom;
	/** �ؽ�Ʈ�� ���� ���� ��ũ�ѷ� */
	private JScrollPane userConversationScroll;
	private JScrollPane userTextMessageScroll;
	private JScrollPane onlineUsersScroll;
	/** �� �̺�Ʈ�� �߰� �� ���ŷ ť */
	private final BlockingQueue<ServerHandleEvent> eventQueue;
	/** ������ �ڽ��� �̺�Ʈ�� �ĺ��ϴ� �� ������ �� ������� ���� �� �̸� */
	private final UserName userName;
	/** ����ڰ��ִ� ���� �̸� */
	private final String roomName;

	/** ������ ���� �� ǥ�� */
	public MainChatView(final BlockingQueue<ServerHandleEvent> eventQueue, final UserName userName, final String roomName) {
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
		WindowAdapter exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				eventQueue.offer(new QuitChattingEvent(userName, roomName));
				System.exit(0);
			}
		};
		frame.addWindowListener(exitListener);

		/** ��ũ�ѷ��� �Բ� ����� ������ ����Ͽ� ��ȭ ������ �ʱ�ȭ */
		usersConversation = new JTextArea();
		usersConversation.setBounds(12, 12, 260, 189);
		usersConversation.setEditable(false);
		userConversationScroll = new JScrollPane(usersConversation);
		userConversationScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userConversationScroll.setBounds(12, 12, 260, 189);
		frame.getContentPane().add(userConversationScroll);

		/** ����ڰ� ��ũ�ѷ��� �޽����� �Է��ϴ� ���� �ʱ�ȭ */
		userTextfield = new JTextArea();
		userTextfield.setBounds(12, 213, 260, 56);
		userTextMessageScroll = new JScrollPane(userTextfield);
		userTextMessageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userTextMessageScroll.setBounds(12, 213, 260, 56);
		frame.getContentPane().add(userTextMessageScroll);

		/** ����� ����� ��ũ�ѷ��� �Բ� ǥ���ϴ� ���� �ʱ�ȭ */
		onlineUsers = new JTextArea();
		onlineUsers.setBounds(284, 34, 154, 184);
		onlineUsers.setEditable(false);
		onlineUsersScroll = new JScrollPane(onlineUsers);
		onlineUsersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineUsersScroll.setBounds(284, 34, 154, 184);
		frame.getContentPane().add(onlineUsersScroll);

		/** ��ư �ʱ�ȭ */
		sendButton = new JButton("Send message");
		sendButton.setBounds(288, 224, 117, 25);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventQueue.offer(new SendMessageEvent(roomName, userName, userTextfield.getText()));
				userTextfield.setText("");
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
	 * @param usersConversationText
	 */
	public void updateUsersConversation(final String usersConversationText) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				usersConversation.setText("");
				usersConversation.append(usersConversationText);
			}
		});
	}

	/**
	 * Ȱ�� ������� ǥ�õ� ����� ������Ʈ�ϴ� �޼ҵ�
	 * 
	 * @param userList
	 */
	public void updateUsersList(final String userList) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				onlineUsers.setText("");
				onlineUsers.append(userList);
			}
		});
	}
}