package common;

import java.io.Serializable;

 public class Message implements Serializable{
    private final MessageType messageType;
    private String text;

    public Message(MessageType messageType)
    {
        this.messageType = messageType;
    }

    public Message(MessageType messageType, String text)
    {
        this.messageType = messageType;
        this.text = text;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getText() {
        return text;
    }
}
