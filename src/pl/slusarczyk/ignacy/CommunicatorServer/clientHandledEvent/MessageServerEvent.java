package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * 이 클래스는 사용자에게 정보를 보내는 데 사용됩니다.
 */
public class MessageServerEvent extends ClientHandledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**사용자에게 표시 될 메시지*/
	private final String message;
	/**메시지가 표시 될 사용자의 사전 패키지 이름이 표시되어야합니다.*/
	private final UserIdData userIDData;
	
	public MessageServerEvent(final String message, final UserIdData userIDData)
	{
		this.message = message;
		this.userIDData = userIDData;
	}

	/**
	 * 메시지 내용을 반환
	 * 
	 * @return the message
	 */
	public String getMessage() 
	{
		return message;
	}

	/**
	 * 보낸 사람의 userId를 반환
	 * 
	 * @return the userID
	 */
	public UserIdData getUserIDData()
	{
		return userIDData;
	}
}
