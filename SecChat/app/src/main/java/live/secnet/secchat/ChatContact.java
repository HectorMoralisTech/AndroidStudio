package live.secnet.secchat;

public class ChatContact {

    private int contactId;
    private String contactName;
    private String contactAddress;
    private String contactImageUrl;

    private byte[] imageBytes;

    public ChatContact() { }
    public ChatContact(int contactId, String contactName, String contactAddress, String contactImageUrl) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactAddress = contactAddress;
        this.contactImageUrl = contactImageUrl;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactImageUrl() {
        return contactImageUrl;
    }

    public void setContactImageUrl(String contactImageUrl) {
        this.contactImageUrl = contactImageUrl;
    }
}
