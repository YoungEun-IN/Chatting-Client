package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;
import java.util.Date;

/**
 * 메시지 집합을 포함하는 클래스
 */
public class MessageData implements Comparable<MessageData>, Serializable {
	private static final long serialVersionUID = 1L;
	/** message */
	private String message;
	/** timeStamp */
	private final Date timeStamp;

	/**
	 * 지정된 파라미터에 근거 해 오브젝트를 생성하는 생성자
	 * 
	 * @param message
	 * @param timeStamp
	 */
	public MessageData(final String message, final Date timeStamp) {
		this.message = message;
		this.timeStamp = timeStamp;
	}

	/**
	 * 메시지 내용을 반환
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 타임 스탬프를 반환
	 * 
	 * @return timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 클라이언트의 정렬 및 표시 전에 사용자에게 닉네임을 추가하는 데 필요한 메시지의 내용을 변경하는 메소드
	 * 
	 * @param newMessage
	 */
	public void setUserMessage(final String newMessage) {
		message = newMessage;
	}

	/**
	 * 메시지를 정렬 할 수있는 메소드
	 */
	public int compareTo(final MessageData o) {
		return getTimeStamp().compareTo(o.getTimeStamp());
	}
}
