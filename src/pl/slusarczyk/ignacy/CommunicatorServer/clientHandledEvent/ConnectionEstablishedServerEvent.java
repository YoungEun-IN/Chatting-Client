package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * Klasa reprezentująca zaakceptowanie przez serwer nowego połączenia, sygnalizująca możliwość otworzenia głównego okna chatu w aplikacji klienckiej
 * 
 * @author Ignacy Ślusarczyk
 */
public class ConnectionEstablishedServerEvent extends ClientHandledEvent implements Serializable
{
	 private static final long serialVersionUID = 1L;
	/**Invormacja o nawiązaniu połączenia*/
	 private  final boolean isEstablished;
	 /**Opakowana nazwa użytkownika*/
	 private final UserIdData userIDData;
	 /**Nazwa pokoju do którego został dołączony*/
	 private final String roomName;
	 
	/**
	 * Konstruktor tworzący zdarzenie na podstawie zadanych parametrów
	 * 
	 * @param isEstablished czy połączenie przyjęte
	 */
	public ConnectionEstablishedServerEvent(final boolean isEstablished, final UserIdData userIdData, final String roomName)
	{
		this.isEstablished = isEstablished;
		this.userIDData = userIdData;
		this.roomName = roomName;
	}
	
	/**
	 * Metoda zwracająca informację o zaakceptowaniu połączenia
	 * 
	 * @return
	 */
	public boolean getConnectionInfrmation()
	{
		return this.isEstablished;
	}
	
	/**
	 * Metoda zwracająca userId
	 * 
	 * @return
	 */
	public UserIdData getUserIDData()
	{
		return userIDData;
	}
	
	/**
	 * Metoda zwracająca nazwę pokoju
	 * 
	 * @return
	 */
	public String getRoomName()
	{
		return roomName;
	}
}
