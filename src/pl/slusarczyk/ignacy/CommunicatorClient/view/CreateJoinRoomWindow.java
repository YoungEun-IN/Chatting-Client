package pl.slusarczyk.ignacy.CommunicatorClient.view;

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

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandeledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.MessageServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**Klasa odpowiedzialna za okno tworzenia lub dolaczania do pokoju**/
class CreateJoinRoomWindow
{
	/**Ramka aplikacji*/
	final JFrame frame;

	/**Pole wpisywania nazwy użytkownika*/
	final JTextField userNameField;
	/**Pole wpisywania nazwy pokoju*/
	final JTextField roomNameField;
	/**Przycisk potwierdzający wpisane informacje i tworzący nowy pokój*/
	JButton submitButtonAndJoinRoom;
	/**Przycisk potwierdzający wpisane informacje i dołączanie do nowego pokoju*/
	JButton submitButtonAndCreateRoom;
	/**Kolejka blokujaca do ktorej sa dodawane nowe eventy*/
	private final BlockingQueue<ServerHandeledEvent> eventQueue;

	public CreateJoinRoomWindow(final BlockingQueue<ServerHandeledEvent> eventQueueObject)
	{
		this.eventQueue = eventQueueObject;
		
		/**Tworzymy główne okno*/
		frame = new JFrame("Create or join room");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400, 150));
	
		/**Ustawiamy odpowiedni Layout*/
		JPanel container = new JPanel();	
		BoxLayout layout = new BoxLayout(container, BoxLayout.Y_AXIS);
		container.setLayout(layout);
			
		/**Inicjalizujemy przycisk tworzenia nowego pokoju*/
		submitButtonAndJoinRoom = new JButton("Create");
		submitButtonAndJoinRoom.addActionListener(new ActionListener() 
		{	
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				eventQueue.offer(new CreateNewRoom(roomNameField.getText(), new UserIdData(userNameField.getText())));
			}
		});
		
		/**Inicjalizujemy przycisk dołączania do istniejącego pokoju*/
		submitButtonAndCreateRoom = new JButton("Join");
		submitButtonAndCreateRoom.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				eventQueue.offer(new JoinExistingRoom(roomNameField.getText(), new UserIdData(userNameField.getText())));
			}
		});
		
		userNameField = new JTextField();
		roomNameField = new JTextField();
			
			
		/**Ustawiamy położenie wszystkich elementów*/
		userNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		roomNameField.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitButtonAndJoinRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
		submitButtonAndCreateRoom.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		/**Ustawiamy tekst domyślnie wpisany w poszczególne pola, pełniący funkcję informacyjną*/
		userNameField.setText("Test");
		roomNameField.setText("Projekt");
			
		/**Dodajemy wszystkie elemenety do kontenera*/
		 container.add(userNameField);
		 container.add(roomNameField);
		 container.add(submitButtonAndJoinRoom);
		 container.add(submitButtonAndCreateRoom);
			 
		 /**Dodajemy kontener do głównej ramki oraz wyświeltamy główna ramkę*/
		 frame.add(container);
		 frame.setVisible(true);
	}
	
	/**
	 * Metoda odpowiedzialna za zamknięcie okna
	 */
	public void closeCreateRoomWindow()
	{
		frame.setVisible(false);
		frame.dispose();
	}

	/**
	 * Metoda odpowiedzialna za wyświetlanie informacji przychodzacych z serwera
	 * 
	 * @param messageObject Obiekt InformationMessage zawierającyc informację do wyswietlenia 
	 */
	public void displayInfoMessage(final MessageServerEvent messageObject)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				JOptionPane.showMessageDialog(frame, messageObject.getMessage());
			}
		});
	}
}
