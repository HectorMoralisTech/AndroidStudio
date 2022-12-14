package live.secnet.secchat;

public class ReceivedChatMessage {

    private String picUrl;
    private String messageText;
    private String dateTimeText;

    public ReceivedChatMessage(String picUrl, String messageText, String dateTimeText) {
        this.picUrl = picUrl;
        this.messageText = messageText;
        this.dateTimeText = dateTimeText;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
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
