package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * 새로운 연결의 서버에 의한 수용을 나타내는 클래스로서, 클라이언트 응용 프로그램에서 기본 대화창을 열 가능성을 알립니다.
 */
public class ConnectionEstablishedServerEvent extends ClientHandledEvent implements Serializable
{
	 private static final long serialVersionUID = 1L;
	/**연결 설정에 대한 표시*/
	 private  final boolean isEstablished;
	 /**감싸인 사용자 이름*/
	 private final UserIdData userIDData;
	 /**가입한 방의 이름*/
	 private final String roomName;
	 
	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param isEstablished 연결이 수락되었는지 여부
	 * @param userIdData
	 * @param roomName
	 */
	public ConnectionEstablishedServerEvent(final boolean isEstablished, final UserIdData userIdData, final String roomName)
	{
		this.isEstablished = isEstablished;
		this.userIDData = userIdData;
		this.roomName = roomName;
	}
	
	/**
	 * 연결 수락에 대한 정보를 반환하는 메서드
	 * 
	 * @return isEstablished
	 */
	public boolean isEstablished()
	{
		return this.isEstablished;
	}
	
	/**
	 * userIDData를 반환
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIDData()
	{
		return userIDData;
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
}
