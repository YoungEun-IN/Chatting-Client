package pl.slusarczyk.ignacy.CommunicatorClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.connection.Client;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandeledEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.view.View;

/**
 * Główna klasa applikacji odpowiada za odpowiednie zainicjalizowanie wszystkich komponentów
 * 
 * @author Ignacy Ślusarczyk
 */
public class CommunicatorClient 
{
	/**
	 * Głowna metoda aplikacjitworzy widok, kolejkę zdarzeń oraz kontroler.
	 * 
	 * @param args argumenty wywołania programu
	 */
	public static void main(String args[])
	{
		BlockingQueue<ServerHandeledEvent> eventQueue = new LinkedBlockingQueue<ServerHandeledEvent>();
		View view = new View(eventQueue);
		Client client = new Client(eventQueue,"localhost", 5000, view);
		client.listenEventAndSend();
	}
}
