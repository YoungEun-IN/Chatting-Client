package pl.slusarczyk.ignacy.CommunicatorClient.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandeledEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.view.View;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ClientHandeledEvent;

/**
 * Główna klasa klienta, w której następuje połączenie z serwerem oraz przechowywanie informacji o aktualnym połączeniu
 * 
 * @author Ignacy Ślusarczyk
 */
public class Client 
{

	/**Socket klienta*/
	private Socket socket;
	/**Strumień wyjściowy*/
	private ObjectOutputStream outputStream;
	/**Strumień wejściowy*/
	private ObjectInputStream inputStream;
	/**Kolejka blokująca do której client dodaje zdarzenia z serwera w celu obsłużenia ich w kontrolerze*/
	private final BlockingQueue<ServerHandeledEvent> eventQueue;
	/**Referencja do widoku*/
	private final View view;
	
	/**
	 * Konstruktor tworzący klienta na podstawie zadanych argumentów
	 * 
	 * @param eventQueue kolejka blokująca
	 */
	public Client(final BlockingQueue<ServerHandeledEvent> eventQueue, final String ipAdress, final int port, final View view)
	{

		this.eventQueue = eventQueue;
		this.view = view;
		
		try
		{
			this.socket = new Socket(ipAdress, port);
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			ListenFromServer listenFromServer = new ListenFromServer();
			listenFromServer.start();
		}
		catch (IOException ex)
		{
			System.err.println(ex);
		}
	}

	
	public void listenEventAndSend()
	{
		
		while (true)
		{
			try
			{
				ServerHandeledEvent serverHandeledEvent = eventQueue.take();
				
				try
				{
					outputStream.writeObject(serverHandeledEvent);
				}
				catch(IOException ex)
				{
					System.exit(1);
				}
			}
			catch (InterruptedException ex)
			{
				System.err.print(ex);
			}
			
		}
	}
	
	/**
	 * Metoda bezpiecznie zamykająca połączenie
	 */
	public void closeConnection()
	{
		try
		{

			socket.close();
		}
		catch(IOException ex)
		{
			System.err.println(ex);
		}
	}
	
	/**
	 * Klasa wewnętrzna służąca do nasłuchiwania zdarzeń od serwera. W przypadku zdarzeń od serwera, dodawane są one do mapy
	 * 
	 * @author Ignacy Ślusarczyk 
	 */
	public class ListenFromServer extends Thread 
	{
		public void run() 
		{
			System.out.println("Rozpoczeto nasluchiwanie zdarzeń od serwera");
			while (true) 
			{
				try 
				{	
					ClientHandeledEvent serverEvent = (ClientHandeledEvent)inputStream.readObject();
					view.executeClientHandeledEvent(serverEvent);
				}	
				catch(IOException e) 
				{
					closeConnection();
					System.exit(1);
				}
				catch(ClassNotFoundException e) 
				{
					System.err.println(e);
				}
			}				
		}
	}
}
