package live.secnet.secchat;

public class ChatMessage {

    private String senderId;
    private String receiverId;

    private String messageTimestamp;
    private String messageContent;

    public ChatMessage() {

    }

    public ChatMessage(String senderId, String receiverId, String messageTimestamp, String messageContent) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageTimestamp = messageTimestamp;
        this.messageContent = messageContent;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(String messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
