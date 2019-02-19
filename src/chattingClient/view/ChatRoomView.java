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

/** 기본 채팅 창을 표시하는 클래스 **/

class ChatRoomView {
	/** 메인 프레임 */
	private JFrame frame;
	/** 사용자 간의 대화가 표시되는 영역 */
	private JTextArea conversationField;
	/** 사용자가 메시지를 입력하는 영역 */
	private JTextArea userTextField;
	/** 채팅 사용자가있는 지역 */
	private JTextArea onlineUsersField;
	/** 메시지 전송 버튼 */
	private JButton sendButton;
	/** 사용자가 목록을 표시하는 위치를 나타내는 레이블 */
	private JLabel lblUsersInRoom;
	/** 텍스트의 개별 영역 스크롤러 */
	private JScrollPane userConversationScroll;
	private JScrollPane userTextMessageScroll;
	private JScrollPane onlineUsersScroll;
	/** 새 이벤트가 추가 된 블로킹 큐 */
	private final BlockingQueue<ClientEvent> eventQueue;
	/** 서버가 자신의 이벤트를 식별하는 데 도움을 준 사용자의 래핑 된 이름 */
	private final String userName;
	/** 사용자가있는 방의 이름 */
	private final String roomName;

	/** 프레임 시작 및 표시 */
	public ChatRoomView(final BlockingQueue<ClientEvent> eventQueue, final String userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
		this.eventQueue = eventQueue;

		initialize();
		frame.setVisible(true);
	}

	/**
	 * 프레임 내용을 초기화
	 */
	private void initialize() {
		/** 메인 프레임 초기화 */
		frame = new JFrame("ChatRoom");
		frame.setBounds(100, 100, 450, 320);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/** 채팅 창을 클릭 */
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				eventQueue.offer(new QuitChattingEvent(userName, roomName));
				System.out.println("사용자가 채팅 창을 닫았습니다");
				System.exit(0);
			}
		});

		/** 스크롤러와 함께 사용자 영역을 사용하여 대화 영역을 초기화 */
		conversationField = new JTextArea();
		conversationField.setBounds(12, 12, 260, 189);
		conversationField.setEditable(false);
		userConversationScroll = new JScrollPane(conversationField);
		userConversationScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userConversationScroll.setBounds(12, 12, 260, 189);
		frame.getContentPane().add(userConversationScroll);

		/** 사용자가 스크롤러로 메시지를 입력하는 영역 초기화 */
		userTextField = new JTextArea();
		userTextField.setBounds(12, 213, 260, 56);
		userTextMessageScroll = new JScrollPane(userTextField);
		userTextMessageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userTextMessageScroll.setBounds(12, 213, 260, 56);
		frame.getContentPane().add(userTextMessageScroll);

		/** 사용자 목록을 스크롤러와 함께 표시하는 영역 초기화 */
		onlineUsersField = new JTextArea();
		onlineUsersField.setBounds(284, 34, 154, 184);
		onlineUsersField.setEditable(false);
		onlineUsersScroll = new JScrollPane(onlineUsersField);
		onlineUsersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineUsersScroll.setBounds(284, 34, 154, 184);
		frame.getContentPane().add(onlineUsersScroll);

		/** 버튼 초기화 */
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

		/** 메시지의 레이블 초기화 */
		lblUsersInRoom = new JLabel("Users in room");
		lblUsersInRoom.setBounds(300, 4, 126, 30);
		frame.getContentPane().add(lblUsersInRoom);
	}

	/**
	 * 사용자의 대화가 표시되는 창을 업데이트하는 메서드
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
	 * 활성 사용자의 표시된 목록을 업데이트하는 메소드
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