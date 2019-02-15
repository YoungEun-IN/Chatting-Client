package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * Klasa opisująca zdarzenie naciśnięcia przez użytkownika przycisku wysłania wiadomości
 * 
 * @author Ignacy Śłusarczyk
 */
public class NewMessage extends ServerHandeledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Nazwa pokoju*/
	private final String roomName;
	/**Opakowana nazwa użytkownika*/
	private final UserIdData userIDData;
	/**Wiadomość, którą użytkownik chce wysłać*/
	private final String message;
	
	/**
	 * Konstruktor tworzący zdarzenie na podstawie podancych parametrów
	 * 
	 * @param roomName nazwa pokoju
	 * @param userID ID użytkownika
	 * @param message wiadomość
	 */
	public NewMessage (final String roomName,final UserIdData userIdData,final String message)
	{
		this.roomName = roomName;
		this.userIDData = userIdData;
		this.message = message;
	}
	
	/**
	 * Metoda zwracająca nazwę pokoju, w którym dany użytkownik się znajduje 
	 * 
	 * @return nazwa pkokju
	 */
	public String getRoomName()
	{
		return roomName;
	}
	
	/**
	 * Metoda zwracająca ID użytkownika, który wysłał wiadomość
	 * 
	 * @return nazwa użytkownika
	 */
	public UserIdData getUserIdData ()
	{
		return userIDData;
	}
	
	/**
	 * Metoda zwracająca wiadomość, którą użytkownik wysłał
	 * 
	 * @return wiadomość
	 */
	public String getMessage ()
	{
		return message;
	}
}
