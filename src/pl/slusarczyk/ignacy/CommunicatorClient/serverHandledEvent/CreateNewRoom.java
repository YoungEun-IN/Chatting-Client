package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * Klasa reprezentująca zdarzenie naciśnięcia przez użytkownika przycisku utworzenia nowego pokoju
 * 
 * @author Ignacy Ślusarczyk
 */
public class CreateNewRoom extends ServerHandeledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Nazwa pokoju*/
	private final String roomName;
	/**Opakowana nazwa użytkownika*/
	private final UserIdData userIDData;

	/**
	 * Konstruktor tworzący zdarzenie na podstawie zadanych parametrów
	 * 
	 * @param roomName nazwa pokoju
	 * @param userID ID użytkownika
	 */
	public CreateNewRoom(final String roomName,final UserIdData userIDData)
	{
		this.roomName = roomName;
		this.userIDData = userIDData;
	}
	
	/**
	 * Metoda zwracająca nazwę pokoju
	 * 
	 * @return nazwa pokoju
	 */
	public String getRoomName()
	{
		return roomName;
	}

	/**
	 * Metoda zwracająca ID użytkownika
	 * 
	 * @return nazwa użytkownika
	 */
	public UserIdData getUserIdData()
	{
		return userIDData;
	}
}


