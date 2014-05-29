
package org.tang.exam.entity;

public class ChatMsgEntity {
    private static final String TAG = ChatMsgEntity.class.getSimpleName();
    private String id;
    private String fromUserId;
    private String fromUserName;
    private String toUserName;
    private String createTime;
    private String msgText;
    private String toUserId;
	private boolean msgType = true;
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
    public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public boolean getMsgType() {
        return msgType;
    }

    public void setMsgType(boolean msgType) {
    	this.msgType = msgType;
    }

    public ChatMsgEntity() {
    }


	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	
}
