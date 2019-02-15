package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;
import java.util.HashSet;


/**
 * Klasa opakowująca klasę User, wysyłana do klienta
 * 
 * @author Ignacy Ślusarczyk
 */
public class UserData implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Id użytkownika*/
	private final UserIdData userIdData;
	/**Zbiór opakowanych wiadomości użytkownika*/
	private final HashSet<MessageData> usersMessages;
	/**Flaga określająca czy użytkownik jest aktywny*/
	private final boolean isActive;
	
	public UserData(final UserIdData userIdData, final HashSet<MessageData> userMessages, final boolean isActive)
	{
		this.userIdData = userIdData;
		this.usersMessages = userMessages;
		this.isActive = isActive;
	}

	/**
	 * Metoda zwracająca userID użytkownika
	 * 
	 * @return userId ID użytkownika
	 */
	public UserIdData getUserIdData() 
	{
		return userIdData;
	}

	/**
	 * Metoda zwracająca zbiór opakowanych wiadomości użytkownika
	 * 
	 * @return usersMessages set opakowanych widomości
	 */
	public HashSet<MessageData> getUsersMessages() 
	{
		return usersMessages;
	}
	
	/**
	 * Metoda zwracająca informację czy dany użytkownik jest aktywny
	 * 
	 * @return
	 */
	public boolean isUserActive()
	{
		return isActive;
	}
}
