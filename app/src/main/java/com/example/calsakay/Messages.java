package com.example.calsakay;

import java.io.Serializable;
import java.util.Date;

public class Messages implements Serializable {

    private int senderId;
    private int recieverId;
    private Date timestamp;
    private String message;
    private int id;
    private String threadName;
    private String messageType;
    private boolean isRead;
    private int chatMateId;
    private int passengerId;
    private String chatmateImage;

    public Messages(int senderId, int recieverId, Date timestamp, String message, int id, String threadName, String messageType, boolean isRead, int chatMateId, int passengerId, String chatmateImage) {
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.timestamp = timestamp;
        this.message = message;
        this.id = id;
        this.threadName = threadName;
        this.messageType = messageType;
        this.isRead = isRead;
        this.chatMateId = chatMateId;
        this.passengerId = passengerId;
        this.chatmateImage = chatmateImage;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "senderId=" + senderId +
                ", recieverId=" + recieverId +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", id=" + id +
                ", threadName='" + threadName + '\'' +
                ", messageType='" + messageType + '\'' +
                ", isRead=" + isRead +
                ", chatMateId=" + chatMateId +
                '}';
    }

    public int getSenderId() {
        return senderId;
    }

    public int getRecieverId() {
        return recieverId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getMessageType() {
        return messageType;
    }

    public boolean isRead() {
        return isRead;
    }

    public int getChatMateId() {
        return chatMateId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public String getChatmateImage() {
        return chatmateImage;
    }
}
