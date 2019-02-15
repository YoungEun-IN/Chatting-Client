package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * 유저가 메세지를 송신하기 위해서 버튼을 누르는 이벤트를 기술하는 클래스
 */
public class SendMessageEvent extends ServerHandledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**방의 이름*/
	private final String roomName;
	/**감싸인 사용자 이름*/
	private final UserIdData userIDData;
	/**사용자가 보내려는 메시지*/
	private final String message;
	
	/**
	 * 주어진 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param roomName
	 * @param userID 
	 * @param message
	 */
	public SendMessageEvent (final String roomName,final UserIdData userIdData,final String message)
	{
		this.roomName = roomName;
		this.userIDData = userIdData;
		this.message = message;
	}
	
	/**
	 * 지정된 사용자가있는 방의 이름을 반환 
	 * 
	 * @return roomName
	 */
	public String getRoomName()
	{
		return roomName;
	}
	
	/**
	 * 메시지를 보낸 사용자의 ID를 반환
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIdData ()
	{
		return userIDData;
	}
	
	/**
	 * 사용자가 보낸 메시지를 반환
	 * 
	 * @return message
	 */
	public String getMessage ()
	{
		return message;
	}
}
