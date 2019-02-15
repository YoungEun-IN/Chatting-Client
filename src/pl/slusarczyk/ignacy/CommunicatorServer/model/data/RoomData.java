package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Klasa opakowująca klasę Room, która jest wysyłana do klienta
 * 
 * @author Ignacy ŚLusarczyk
 */
public class RoomData implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Zbiór użytkowników*/
	private  final HashSet<UserData> userSet;
	
	/**
	 * Konstruktor tworzący obiekt na podstawie zadanych parametrów
	 * 
	 * @param userSet
	 */
	public RoomData(final HashSet<UserData> userSet)
	{
		this.userSet = userSet;
	}
	
	/**
	 * Metoda zwracająca zbiór użytkowników
	 * 
	 * @return set użytkowników
	 */
	public HashSet<UserData> getUserSet()
	{
		return userSet;
	}
}
