package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;
import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * Klasa opisująca zdarzenie naciśnięcia przez użytkownika przycisku wyjścia z chatu w oknie rozmowy.
 * 
 * @author Ignacy Ślusarczyk
 */
public class ClientLeftRoom extends ServerHandeledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Nazwa użytkownika, który wyszedł z chatu*/
	private final UserIdData userIdData;
	/**Nazwa pokoju, w którym użytkownik się znajdował*/
	private final String roomName;
	
	/**
	 * Konstruktor tworzący zdarzenie na podstawie podanych parametrów
	 * 
	 * @param userName nazwa użytkownika
	 * @param roomName nazwa pokoju
	 */
	public ClientLeftRoom(final UserIdData userIDData,final String roomName)
	{
		this.userIdData=userIDData;
		this.roomName=roomName;
	}
	
	/**
	 * Metoda zwracająca nazwę użytkownika
	 *
	 * @return nazwa użytkownika
	 */
	public UserIdData getUserIDData() 
	{
		return userIdData;
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
}
