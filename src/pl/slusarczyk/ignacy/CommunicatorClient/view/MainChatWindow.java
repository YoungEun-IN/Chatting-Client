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

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**Klasa odpowiedzialna za wyświetlanie głównego okna chatu**/

class MainChatWindow 
{
	/**Główna ramka*/
	private JFrame frame;
	/**Obszar gdzie wyświetlana jest rozmowa pomiedzy użytkownikami */
	private JTextArea usersConversation;
	/**Obszar gdzie użytkownik wpisuje swoją wiadomość */
	private JTextArea userTextfield;
	/**Obszar gdzie wyświetlani są obecni użytkownicy chatu*/
	private JTextArea onlineUsers;
	/**Przycisk sygnalizujący wysłanie wiadomości*/
	private JButton sendButton;
	/**Etykieta wskazująca miejsce wyświetlania listy użytkowników*/
	private JLabel lblUsersInRoom;
	/**Scrollery poszczególnych obszarów tekstowych*/
	private JScrollPane userConversationScroll;
	private JScrollPane userTextMessageScroll;
	private JScrollPane onlineUsersScroll;
	/**Kolejka blokujaca do ktorej sa dodawane nowe eventy*/
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/**Opakowana nazwa użytkownika za pomocą, której serwer identyfikuje jego eventy*/
	private final UserIdData userIdData;
	/**Nazwa pokoju, w którym użytkownika się znajduje*/
	private final String roomName;
	
	/**Konstruktro inicjulizujący i wyświetlający ramkę*/
	public MainChatWindow( final BlockingQueue<ServerHandledEvent> eventQueue, final UserIdData userIdData, final String roomName)
	{	
		this.userIdData = userIdData;
		this.roomName = roomName;
		this.eventQueue = eventQueue;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		/**Inicjalizowanie głównej ramki*/
		frame = new JFrame("ChatRoom");
		frame.setBounds(100, 100, 450, 320);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		/**Przeciążam kliknięcie zamknięcia okna chatu*/
		WindowAdapter exitListener = new WindowAdapter() 
		{
		    @Override
		    public void windowClosing(WindowEvent e) 
		    {
		        	eventQueue.offer(new ClientLeftRoom(userIdData, roomName));
		           System.exit(0);
		    }
		};
		frame.addWindowListener(exitListener);
		
		
		/**Inicjalizowanie obszaru rozmowy użytkowników wraz ze scrollerem*/
		usersConversation = new JTextArea();
		usersConversation.setBounds(12, 12, 260, 189);
		usersConversation.setEditable(false);
		userConversationScroll = new JScrollPane(usersConversation);
		userConversationScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userConversationScroll.setBounds(12, 12, 260, 189);
		frame.getContentPane().add(userConversationScroll);	
		
		/**Inicjalizowanie obszaru, w którym użytkownik wpisuje swoją wiadomość wraz ze scrollerem*/
		userTextfield = new JTextArea();
		userTextfield.setBounds(12, 213, 260, 56);
		userTextMessageScroll = new JScrollPane(userTextfield);
		userTextMessageScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		userTextMessageScroll.setBounds(12, 213, 260, 56);
		frame.getContentPane().add(userTextMessageScroll);	
		
		/**Inicjalizowanie obszaru, w którym wyświetlana jest lista uzytkowników wraz ze scrollerem*/
		onlineUsers = new JTextArea();
		onlineUsers.setBounds(284, 34, 154, 184);
		onlineUsers.setEditable(false);
		onlineUsersScroll = new JScrollPane(onlineUsers);
		onlineUsersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineUsersScroll.setBounds(284, 34, 154, 184);
		frame.getContentPane().add(onlineUsersScroll);
		
		/**Inicjalizowanie przycisku wyślij*/
		sendButton = new JButton("Send message");
		sendButton.setBounds(288, 224, 117, 25);
		sendButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				eventQueue.offer(new NewMessage(roomName,userIdData,userTextfield.getText()));
				userTextfield.setText("");
			}
		});
		
		frame.getContentPane().add(sendButton);
		
		/**Inicjalizowanie etykiety wkazującej listę obecnych użytkowników*/
		lblUsersInRoom = new JLabel("Users in room");
		lblUsersInRoom.setBounds(300, 4, 126, 30);
		frame.getContentPane().add(lblUsersInRoom);
	}

	/**
	 * Metoda uaktualniająca okno, w którym wyświetlana jest rozmowa użytkowników
	 * 
	 * @param usersConversationText
	 */
	public void updateUsersConversation(final String usersConversationText)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				usersConversation.setText("");
				usersConversation.append(usersConversationText);
			}
		});
	}
	
	/**
	 * Metoda uaktualniająca wyświetlaną listę aktywnych użytkowników
	 * 
	 * @param userList
	 */
	public void updateUsersList(final String userList)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				onlineUsers.setText("");
				onlineUsers.append(userList);
			}
		});
	}
}