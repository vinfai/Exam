
package org.tang.exam.entity;

public class ChatMsgEntity {
    private static final String TAG = ChatMsgEntity.class.getSimpleName();
    
    private String fromUserId;
    
    private String fromUserName;
    
    private String toUserName;

    private String date;

    private String text;
    
    private String time;
    
    private String toUserId;
    

    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private boolean isComMeg = true;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
    	isComMeg = isComMsg;
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
