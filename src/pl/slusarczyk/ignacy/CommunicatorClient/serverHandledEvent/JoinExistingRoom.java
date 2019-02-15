package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * Klasa opisująca zdarzenie naciśnięcia przez użytkownika przycisku dołączenia do pokoju
 * 
 * @author Ignacy Ślusarczyk
 */
public class JoinExistingRoom extends ServerHandeledEvent implements Serializable
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
	 * @param userId ID użytkownika 
	 */
	public JoinExistingRoom (final String roomName,final UserIdData userIdData)
	{
		this.roomName = roomName;
		this.userIDData = userIdData;
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
	 * Metoda zwracająca opakowaną nazwę użytkownika
	 * 
	 * @return nazwa użytkownika
	 */
	public UserIdData getUserIdData()
	{
		return userIDData;
	}
}


