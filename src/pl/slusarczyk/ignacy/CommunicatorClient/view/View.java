package pl.slusarczyk.ignacy.CommunicatorClient.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ClientHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConnectionEstablishedServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConversationServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.InfoServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.MessageData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;

/**
 * Główna klasa widoku odpowiedzialna za odpowiednie wyświetlanie okien i otrzymanych konwersacji
 * 
 * @author Ignacy Ślusarczyk
 */
public class View 
{
	/**Okno wyboru dołączenia lub stworzenia nowego pokoju*/
	private CreateJoinRoomWindow createJoinRoomView;
	/**Główne okno chatu*/
	private MainChatWindow mainChatView;
	/**Kolejka blokująca do której wrzucamy zdarzenia obsługiwane przez kontroler*/
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	
	private final Map<Class<? extends ClientHandledEvent>, ClientHandeledEventStrategy> strategyMap;
	
	/**
	 * Konstruktor tworzący widok na podstawie zadanego parametru
	 * 
	 * @param eventQueue kolejka blokująca
	 */
	public View(BlockingQueue<ServerHandledEvent> eventQueue)
	{
		this.createJoinRoomView = new CreateJoinRoomWindow(eventQueue);
		this.eventQueue = eventQueue;
		
		
		/**Tworze mapę strategii obsługi makiet*/
		this.strategyMap = new HashMap<Class<? extends ClientHandledEvent>, ClientHandeledEventStrategy>();
		this.strategyMap.put(ConnectionEstablishedServerEvent.class, new ConnectionEstablishedStrategy());
		this.strategyMap.put(ConversationServerEvent.class, new ConversationServerEventStrategy());
		this.strategyMap.put(InfoServerEvent.class, new MessageServerEventStrategy());
	}
	
	/**Metoda odpowiedzialna za wykonanie strategii odpowiadającej dostarczonej makiety
	 * 
	 * @param clientHandeledEventObject obiekt wysłany przez serwer
	 */
	public void executeClientHandeledEvent(ClientHandledEvent clientHandeledEventObject) 
	{
		ClientHandeledEventStrategy clientHandeledEventStrategy = strategyMap.get(clientHandeledEventObject.getClass());
		clientHandeledEventStrategy.execute((ClientHandledEvent)clientHandeledEventObject);
	}
	
	/**
	 * Abstrakcyjna klasa bazowa dla klas strategii obsługujących zdarzenia.
	 * 
	 * @author Ignacy Ślusarczyk
	 */
	abstract class ClientHandeledEventStrategy 
	{
		/**
		 * Abstrakcyjna metoda opisująca obsługę danego zdarzenia.
		 * 
		 * @param ClientHandledEvent makieta od serwera, która musi zostać poprawnie obsłużona
		 */
		abstract void execute(final ClientHandledEvent clientHandeledEventObject);
	}
	
	/**
	 * Klasa wewnętrzna opisująca strategię obsługi przyjścia informacji od serwera, że dołączenie lub utworzenie pokoju zakończyło się pomyślnie
	 *
	 * @author Ignacy Ślusarczyk
	 */
	class ConnectionEstablishedStrategy extends ClientHandeledEventStrategy
	{
		
		@Override
		void execute(ClientHandledEvent clientHandeledEventObject) 
		{
			ConnectionEstablishedServerEvent connectionEstablished = (ConnectionEstablishedServerEvent) clientHandeledEventObject;
			mainChatView = new MainChatWindow(eventQueue,connectionEstablished.getUserIDData(), connectionEstablished.getRoomName());
			createJoinRoomView.closeCreateRoomWindow();
			createJoinRoomView = null;
		}
	}
	
	/**
	 * Klasa wewnętrzna opisująca strategię obsługi przyjścia rozmowy oraz listy użytkowników
	 *
	 * @author Ignacy Ślusarczyk
	 */
	class ConversationServerEventStrategy extends ClientHandeledEventStrategy
	{
		@Override
		void execute(ClientHandledEvent clientHandeledEventObject) 
		{
			ConversationServerEvent conversationObject = (ConversationServerEvent) clientHandeledEventObject;
			updateUserConversationAndList(conversationObject);
		}
	}
	
	/**
	 * Klasa wewnętrzna opisująca strategię obsługi przyjścia od serwera informacji do wyświetlenia 
	 * 
	 * @author Ignacy Ślusarczyk
	 */
	class MessageServerEventStrategy extends ClientHandeledEventStrategy
	{
		@Override
		void execute(ClientHandledEvent clientHandeledEventObject)
		{
			InfoServerEvent messageObject = (InfoServerEvent) clientHandeledEventObject;
			createJoinRoomView.displayInfoMessage(messageObject);
		}
	}
	
	/****************************Metody widoku*********************88*/
	
	/**
	 * Metoda uaktualniająca wyświetlaną rozmowę oraz aktywnych użytkowników
	 * 
	 * @param conversationServerEvent opakowane informacje do wyświetlenia
	 */
	public void updateUserConversationAndList(ConversationServerEvent conversationServerEvent)
	{
		updateConversation(conversationServerEvent);
		updateUserList(conversationServerEvent);
	}
	
	/**
	 * Metoda odpowiedzialna bezpośrednio na uaktualnienie wyświetlanej rozmowy
	 * 
	 * @param conversationServerEvent opakowane informacje potrzebne do uaktualnieneia wyświetlanej rozmowy
	 */
	public void updateConversation(ConversationServerEvent conversationServerEvent)
	{
		addUsersNicksToMessage(conversationServerEvent);
		HashSet<MessageData> allMessages = gatherAllMessages(conversationServerEvent);
		ArrayList<MessageData> sortedMessages = sortAllMessages(allMessages);
		String conversationToDisplay = sortedMessagesToString(sortedMessages);
		
		mainChatView.updateUsersConversation(conversationToDisplay);
	}
	
	/**
	 * Metoda odpowiedzialna za dodanie nazwy użytkownika do każdej wiadomości w celu możliwości ich identyfikacji po wyświetleniu
	 * 
	 * @param conversationInfo opakowane informacje
	 */
	void addUsersNicksToMessage(ConversationServerEvent conversationInfo)
	{
		HashSet<UserData> userDataSet = conversationInfo.getRoom().getUserSet();
		
		for(UserData userData : userDataSet)
		{
			for(MessageData messageData : userData.getUsersMessages())
			{
				messageData.setUserMessage(userData.getUserIdData().getUserName() +":"+ messageData.getMessage());
			}
		}
	}
	
	/**
	 * Metoda zbierająca wszystkie wiadomości użytkowników z danego pokoju
	 * 
	 * @param conversationInfo opakowane informacje
	 * @return zbiór wszystkich wiadomości
	 */
	HashSet<MessageData> gatherAllMessages (ConversationServerEvent conversationInfo)
	{
		HashSet<MessageData> conversation = new HashSet<MessageData>();
		
		for(UserData userData: conversationInfo.getRoom().getUserSet())
		{
			conversation.addAll(userData.getUsersMessages());
		}
		return conversation;
	}
	
	/**
	 * Metoda sortująca wszystkie wiadomości użytkowników wg daty ich powstania
	 * 
	 * @param usersMessages zbior wiadomości użytkowników
	 * @return
	 */
	ArrayList<MessageData> sortAllMessages(HashSet<MessageData> usersMessages)
	{
		ArrayList<MessageData> sortedMessages = new ArrayList<MessageData>();
		sortedMessages.addAll(usersMessages);
		Collections.sort(sortedMessages);
		return sortedMessages;
	}
	
	/**
	 * Metoda zamieniająca posortowany zbiór wiadomości w string gotowy do wyświetlenia w oknie chatu
	 * 
	 * @param sortedMessages lista wiadomości
	 * @return
	 */
	String sortedMessagesToString(ArrayList<MessageData> sortedMessages)
	{
		String conversationToDisplay = new String("");
		for(MessageData messageData : sortedMessages)
		{
			conversationToDisplay = conversationToDisplay + messageData.getMessage() + "\n";
		}
		return conversationToDisplay;
	}
	
	/**
	 * Metoda uaktualniająca listę użytkowników chatu
	 * 
	 * @param conversationServerEvent opakowane dane
	 */
	void updateUserList(ConversationServerEvent conversationServerEvent)
	{
		String usersListToDisplay = new String("");
		
		List<String> userListToSort = new ArrayList<String>();
		
		/**Przechodzimy po wszystkich użytkownikach i sprawdzamy czy dany użytkownik jest aktywny, jeśli jest to dodajemymy go do listy*/
		for(UserData userData: conversationServerEvent.getRoom().getUserSet())
		{
			if(userData.isActive() == true)
			{
				userListToSort.add(userData.getUserIdData().getUserName());
			}
		}
		
		Collections.sort(userListToSort);
		for (String imie:userListToSort)
		{
			usersListToDisplay = usersListToDisplay + imie + "\n";
		}
		mainChatView.updateUsersList(usersListToDisplay);
	}
}
