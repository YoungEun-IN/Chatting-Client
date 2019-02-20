package chattingServer.model.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * 방 정보를 담고 있다.
 */
public class RoomData implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**userSet*/
	private  final HashSet<UserData> userSet;
	
	/**
	 * 주어진 매개 변수를 기반으로 객체를 생성하는 생성자
	 * 
	 * @param userSet
	 */
	public RoomData(final HashSet<UserData> userSet)
	{
		this.userSet = userSet;
	}
	
	/**
	 * 사용자의 집합을 반환
	 * 
	 * @return userSet
	 */
	public HashSet<UserData> getUserSet()
	{
		return userSet;
	}
}
