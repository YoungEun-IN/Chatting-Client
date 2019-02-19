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
 * 방을 만들거나 연결하는 창을 담당하는 클래스
 **/
class CreateOrJoinRoomView {
	/** 적용 프레임 */
	final JFrame frame;

	/** 사용자 이름 입력 필드 */
	final JTextField userNameField;
	/** 방 이름 입력 필드 */
	final JTextField roomNameField;
	/** 입력 된 정보를 확인하고 새 방을 만드는 버튼 */
	JButton submitButtonAndJoinRoom;
	/** 입력 된 정보를 확인하고 새 방에 참여하는 버튼 */
	JButton submitButtonAndCreateRoom;
	/** 새 이벤트가 추가 된 블로킹 큐 */
	private final BlockingQueue<ClientEvent> eventQueue;

	public CreateOrJoinRoomView(final BlockingQueue<ClientEvent> eventQueueObject) {
		this.eventQueue = eventQueueObject;

		/** 기본 창 만들기 */
		frame = new JFrame("Create or join room");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400, 150));

		/** 올바른 레이아웃을 설정 */
		JPanel container = new JPanel();
		BoxLayout layout = new BoxLayout(container, BoxLayout.Y_AXIS);
		container.setLayout(layout);

		/** 새로운 방을 만들기위한 버튼을 초기화 */
		submitButtonAndJoinRoom = new JButton("Create");
		submitButtonAndJoinRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventQueue.offer(new CreateNewRoomEvent(roomNameField.getText(), userNameField.getText()));
			}
		});

		/** 버튼을 초기화하여 기존 회의실에 연결 */
		submitButtonAndCreateRoom = new JButton("Join");
		submitButtonAndCreateRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				eventQueue.offer(new JoinExistingRoomEvent(roomNameField.getText(), userNameField.getText()));
			}
		});

		userNameField = new JTextField();
		roomNameField = new JTextField();

		/** 모든 요소의 위치를 설정 */
		userNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		roomNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitButtonAndJoinRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitButtonAndCreateRoom.setAlignmentX(Component.CENTER_ALIGNMENT);

		/** 개별 필드에 입력 된 텍스트를 설정 */
		userNameField.setText("userName");
		roomNameField.setText("roomName");

		/** 모든 항목을 컨테이너에 추가 */
		container.add(userNameField);
		container.add(roomNameField);
		container.add(submitButtonAndJoinRoom);
		container.add(submitButtonAndCreateRoom);

		/** 컨테이너를 메인 프레임에 추가하고 메인 프레임을 선택 */
		frame.add(container);
		frame.setVisible(true);
	}

	/**
	 * 윈도우를 닫는 메소드
	 */
	public void closeCreateRoomWindow() {
		frame.setVisible(false);
		frame.dispose();
	}

	/**
	 * 서버에서 오는 정보를 표시하는 메소드
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
