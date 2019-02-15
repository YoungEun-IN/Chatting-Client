package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Klasa opakowująca niepubliczną klasę Message.
 * 
 * @author Ignacy Ślusarczyk
 */
public class MessageData implements Comparable<MessageData>, Serializable 
{
	private static final long serialVersionUID = 1L;
	/**Treść wiadomości*/
	private String message;
	/**Znacznik czasowy*/
	private final Date timeStamp;
	
	/**
	 * Konstruktor tworzący obiekt na podstawei zadanych parametrów
	 * 
	 * @param message
	 * @param timeStamp
	 */
	public MessageData(final String message, final Date timeStamp)
	{
		this.message = message;
		this.timeStamp = timeStamp;
	}

	/**
	 * Metoda zwracająca treść wiadomości
	 * 
	 * @return message wiadomość
	 */
	public String getMessage() 
	{
		return message;
	}

	/**
	 * Metoda zwracająca znacznik czasowy
	 * 
	 * @return znacznik czasowy
	 */
	public Date getTimeStamp() 
	{
		return timeStamp;
	}
	
	/**
	 * Metoda zmieniająca treść wiadomości, konieczna do przy dopisywaniu nicku użytkownika do wiadomości przed ich sortowaniem i wyświetlaniem u klienta
	 * @param newMessage
	 */
	public void setUserMessage(final String newMessage)
	{
		message = newMessage;
	}
	
	/**
	 * Metoda pozwalająca sortować obiekty typu MessageData wg. daty powstania
	 */
	public int compareTo(final MessageData o) 
	{
	  return getTimeStamp().compareTo(o.getTimeStamp());
	}
}
