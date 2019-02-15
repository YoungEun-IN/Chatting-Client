package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * 사용자가 방에 연결하기 위해 버튼을 누르는 이벤트를 설명하는 클래스
 */
public class JoinExistingRoom extends ServerHandledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**roomName*/
	private final String roomName;
	/**userIDData*/
	private final UserIdData userIDData;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param roomName 
	 * @param userId ID
	 */
	public JoinExistingRoom (final String roomName,final UserIdData userIdData)
	{
		this.roomName = roomName;
		this.userIDData = userIdData;
	}
		
	/**
	 * 방 이름을 반환
	 * 
	 * @return roomName
	 */
	public String getRoomName()
	{
		return roomName;
	}

	/**
	 * 사용자의 패키지 이름을 반환
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIdData()
	{
		return userIDData;
	}
}


