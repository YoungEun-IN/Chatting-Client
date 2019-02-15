package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;


/**
 * Klasa opakowująca nazwę użytkownika, wysyłana do klienta
 * 
 * @author Ignacy Ślusarczyk
 */
public class UserIdData implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Nazwa użytkownika*/
	private final String userNameToDisplay;
	
	/**
	 * Konstruktor tworzący obiekt na podstawie zadanego parametru
	 * 
	 * @param userName Nazwa użytownika
	 */
	public UserIdData(final String userName)
	{
		this.userNameToDisplay = userName;
	}
		
	/**
	 * Metoda zwracająca nazwę użytkownika w celu dopasowania ich do wysłanych przez nich wiadomości
	 * 
	 * @return user name
	 */
	public String getUserName()
	{
		return userNameToDisplay;
	}
}

