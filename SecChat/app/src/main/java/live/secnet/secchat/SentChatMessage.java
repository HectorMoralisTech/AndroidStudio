package live.secnet.secchat;

public class SentChatMessage {

    private String messageText;
    private String dateTimeText;

    public SentChatMessage(String messageText, String dateTimeText) {
        this.messageText = messageText;
        this.dateTimeText = dateTimeText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getDateTimeText() {
        return dateTimeText;
    }

    public void setDateTimeText(String dateTimeText) {
        this.dateTimeText = dateTimeText;
    }
}
