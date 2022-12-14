package live.secnet.secchat;

import android.media.Image;

import androidx.annotation.Nullable;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.ox.selection_strategy.BareJidUserId;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMPPThread {

    private static ChatManager chatManager;
    private static AbstractXMPPConnection xmppConn;
    private static List<ChatContact> cachedContactsList = new ArrayList<>();
    private static Map<EntityBareJid, Chat> activeChats = new HashMap<>();

    private static IncomingChatMessageListener incomingChatListener;
    private static OutgoingChatMessageListener outgoingChatListener;

    public static class ConnectionRequest extends Thread {

        private String host;
        private XMPPResult rstCallback;

        public ConnectionRequest(String host, XMPPResult rstCallback) {

            this.host = host;
            this.rstCallback = rstCallback;
        }

        @Override
        public void run() {
            connect(rstCallback);
        }

        private void connect(XMPPResult xmppResult)
        {
            try
            {
                XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration.builder()
                        .setXmppDomain(host)
                        .setHost(host)
                        .setPort(5222)
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                        .build();

                xmppConn = new XMPPTCPConnection(conf);
                xmppConn.connect();
                chatManager = ChatManager.getInstanceFor(xmppConn);

                if(xmppResult != null)
                    xmppResult.onResultCallback(RESULT.SUCCESS, REQUEST_TYPE.CONNECTION);
            }
            catch (Exception e)
            {
                e.printStackTrace();

                if(xmppResult != null)
                    xmppResult.onResultCallback(RESULT.ERROR_EXCEPTION, REQUEST_TYPE.CONNECTION);
            }
        }
    }

    public static class LoginRequest extends Thread {

        private String username;
        private String password;
        private XMPPResult rstCallback;

        @Override
        public void run()
        {
            login(rstCallback);
        }

        public LoginRequest(String username, String password, XMPPResult rstCallback) {
            this.username = username;
            this.password = password;
            this.rstCallback = rstCallback;
        }

        private void login(XMPPResult xmppResult)
        {
            try
            {
                xmppConn.login(username, password);
                EntityBareJid mEntity = xmppConn.getUser().asEntityBareJid();
                Roster roster = Roster.getInstanceFor(xmppConn);

                VCardManager cardManager = VCardManager.getInstanceFor(xmppConn);
                VCard vCard = cardManager.loadVCard(mEntity);

               // if(vCard.getNickName() == "" || vCard.getNickName() == null) {
                    vCard.setJabberId(username + "@xmpp.secnet.live");
                    vCard.setNickName(username);
                    cardManager.saveVCard(vCard);
                //xqld}

                if(xmppResult != null)
                    xmppResult.onResultCallback(xmppConn.isAuthenticated() ? RESULT.SUCCESS : RESULT.WRONG_CREDENTIALS, REQUEST_TYPE.LOGIN);
            }
            catch(Exception e)
            {
                e.printStackTrace();

                if(xmppResult != null)
                    xmppResult.onResultCallback(RESULT.ERROR_EXCEPTION, REQUEST_TYPE.LOGIN);
            }

        }
    }

    public static class ContactsRequest extends Thread
    {
        private XMPPResult rstCallback;

        public ContactsRequest(XMPPResult rstCallback) {
            this.rstCallback = rstCallback;
        }

        @Override
        public void run() {
            fetchAll(rstCallback);
        }

        private void fetchAll(XMPPResult xmppResult)
        {
            cachedContactsList.clear();

            try
            {
                Roster roster = Roster.getInstanceFor(xmppConn);
                Collection<RosterEntry> entries = roster.getEntries();

                for (RosterEntry entry : entries) {

                    VCardManager cardManager = VCardManager.getInstanceFor(xmppConn);
                    VCard vCard = cardManager.loadVCard(entry.getJid().asEntityBareJidIfPossible());

                    ChatContact contact = new ChatContact();
                    contact.setContactId(0);
                    contact.setContactName(vCard.getNickName() == "" || vCard.getNickName() == null ? "Anonymous" : vCard.getNickName());
                    contact.setContactAddress(entry.getJid().asEntityBareJidIfPossible().toString());
                    contact.setImageBytes(vCard.getAvatar());

                    cachedContactsList.add(contact);
                }

                if(xmppResult != null)
                    xmppResult.onResultCallback(RESULT.SUCCESS, REQUEST_TYPE.FETCH_CONTACTS);

            }
            catch(Exception e)
            {
                e.printStackTrace();

                if(xmppResult != null)
                    xmppResult.onResultCallback(RESULT.ERROR_UNKNOWN, REQUEST_TYPE.FETCH_CONTACTS);
            }
        }
    }

    public static class MessagesRequest extends Thread {

        private XMPPResult rstCallback;

        public MessagesRequest(XMPPResult xmppResult) {
            fetchMessages(xmppResult);
        }

        public void fetchMessages(XMPPResult xmppResult) {

            try {

                if(xmppResult != null)
                    xmppResult.onResultCallback(RESULT.SUCCESS, REQUEST_TYPE.FETCH_MESSAGES);

            } catch(Exception e) {
                e.printStackTrace();

                if(xmppResult != null)
                    xmppResult.onResultCallback(RESULT.ERROR_EXCEPTION, REQUEST_TYPE.FETCH_MESSAGES);
            }
        }
    }

    public static class RegisterRequest extends Thread {

        private String email;
        private String username;
        private String password;

        private XMPPResult rstCallback;

        @Override
        public void run() {
            createAccount(rstCallback);
        }

        public RegisterRequest(String username, String password, String email, XMPPResult rstCallback) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.rstCallback = rstCallback;
        }

        private void createAccount(XMPPResult rst)
        {
            try
            {
                AccountManager account = AccountManager.getInstance(xmppConn);
                Map<String, String> accountData = new HashMap<String, String>();

                accountData.put("name", username);
                accountData.put("username", this.username);
                accountData.put("password", this.password);
                accountData.put("email", this.email);
                accountData.put("nickname", this.username);

                if(!account.supportsAccountCreation())
                {
                    if(xmppConn != null)
                        rst.onResultCallback(RESULT.ERROR_EXCEPTION, REQUEST_TYPE.REGISTER);

                    return;
                }

                account.createAccount(Localpart.from(username), password, accountData);

                if(xmppConn != null)
                    rst.onResultCallback(RESULT.SUCCESS, REQUEST_TYPE.REGISTER);
            }
            catch(Exception e)
            {
                e.printStackTrace();

                if(xmppConn != null)
                    rst.onResultCallback(RESULT.ERROR_EXCEPTION, REQUEST_TYPE.REGISTER);
            }
        }
    }

    public static void sendMessage(ChatMessage message, ChatContact contact, IncomingChatMessageListener incomingListener) {

        try {
            EntityBareJid jid = JidCreate.entityBareFrom(contact.getContactAddress());
            message.setSenderId(xmppConn.getUser().asEntityBareJidString());

            for(int n = 0; n < activeChats.size(); n++) {
                if(activeChats.containsKey(jid)) {
                    activeChats.get(jid).send(message.getMessageContent());
                    break;
                }
            }

            Chat chat = chatManager.chatWith(jid);
            chat.send(message.getMessageContent());
            activeChats.put(jid, chat);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected() { return xmppConn.isConnected(); }
    public static boolean isAnonymous() { return xmppConn.isAnonymous(); }
    public static boolean isAuthenticated() { return xmppConn.isAuthenticated(); }
    public static boolean hasContactsList() { return (cachedContactsList.size() != 0); }

    public static String getUserJid() { return xmppConn.getUser().asEntityBareJid().toString(); };
    public static ChatContact getChatContact(String address) {

        for(ChatContact contact : cachedContactsList) {

            if(contact.getContactAddress() == address) {
                return contact;
            }
        }

        return null;
    }
    public static List<ChatContact> getCachedContacts() { return cachedContactsList; }

    public static void setChatListeners(IncomingChatMessageListener incListener, OutgoingChatMessageListener outListener) {
        incomingChatListener = incListener;
        outgoingChatListener = outListener;
        
        chatManager.addIncomingListener(incomingChatListener);
        chatManager.addOutgoingListener(outgoingChatListener);
    }

    public enum REQUEST_TYPE {
        CONNECTION,
        LOGIN,
        REGISTER,
        DISCONNECT,
        FETCH_CONTACTS,
        FETCH_MESSAGES
    }

    public enum RESULT {
        SUCCESS,
        ERROR_UNKNOWN,
        ERROR_INTERNET,
        ERROR_SERVER_DOWN,
        ERROR_INVALID_HOST,
        WRONG_CREDENTIALS,
        ERROR_TLS,
        ERROR_EXCEPTION
    }
}
