package live.secnet.secchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.MessageBuilder;
import org.jxmpp.jid.EntityBareJid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private ImageView contactsIv;
    private ImageView messagesIv;
    private ImageView profileIv;
    private EditText searchEt;

    private TextView contactsAmountTv;
    private RecyclerView contactsRv;
    private RecyclerView messagesRv;
    private RecyclerView profilesRv;

    private ArrayList<String> contactNames = new ArrayList<>();
    private ArrayList<String> contactImages = new ArrayList<>();

    private ArrayList<String> messageDescs = new ArrayList<>();
    private ArrayList<String> messageTimes = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager = null;
    private ContactsAdapter contactsAdapter = null;
    private String contactsAmountStr = "";
    private boolean finishedFetchingContacts = false;

    private List<ChatContact> contactsList = new ArrayList<>();
    private List<ChatMessage> messagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setupHooks();
        loadContacts();
        loadMessages();

        XMPPThread.setChatListeners(this::onIncomingMessage, this::onOutgoingMessage);
    }
    
    @Override
    public void onBackPressed()
    {
        return;
    }

    private void loadContacts()
    {
        XMPPThread.ContactsRequest contactsReq = new XMPPThread.ContactsRequest(this::applyContactList);
        contactsReq.start();

        onContactsFetched();
    }
    private void loadMessages() {

        DBHelper db = new DBHelper(Dashboard.this);

        for(ChatContact contact : contactsList) {
            List<ChatMessage> message = db.getMessages(contact.getContactAddress(), 1);
            messagesList.add(message.get(0));
        }

        onMessagesFetched();
    }
    private void setupHooks()
    {
        searchEt = findViewById(R.id.searchEt);
        profilesRv = findViewById(R.id.profilesRv);
        messagesRv = findViewById(R.id.messagesRv);
        contactsRv =  findViewById(R.id.contactsRv);
        contactsIv =findViewById(R.id.contactsIv);
        contactsAmountTv = findViewById(R.id.contactsAmountTv);

        messagesIv = findViewById(R.id.messagesIv);
        profileIv = findViewById(R.id.profileIv);

        contactsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilesRv.setVisibility(View.INVISIBLE);
                messagesRv.setVisibility(View.INVISIBLE);
                contactsRv.setVisibility(View.VISIBLE);

                profileIv.setColorFilter(Color.rgb(0,0,0));
                messagesIv.setColorFilter(Color.rgb(0,0,0));
                contactsIv.setColorFilter(Color.rgb(241, 67, 126));
            }
        });

        messagesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsRv.setVisibility(View.INVISIBLE);
                profilesRv.setVisibility(View.INVISIBLE);
                messagesRv.setVisibility(View.VISIBLE);

                profileIv.setColorFilter(Color.rgb(0,0,0));
                messagesIv.setColorFilter(Color.rgb(241, 67, 126));
                contactsIv.setColorFilter(Color.rgb(0,0,0));
            }
        });

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactsRv.setVisibility(View.INVISIBLE);
                messagesRv.setVisibility(View.INVISIBLE);
                profilesRv.setVisibility(View.VISIBLE);

                profileIv.setColorFilter(Color.rgb(241, 67, 126));
                messagesIv.setColorFilter(Color.rgb(0,0,0));
                contactsIv.setColorFilter(Color.rgb(0,0,0));
            }
        });

        searchEt.setFocusable(false);
    }

    private void onContactsFetched()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(finishedFetchingContacts) {
                    contactsRv.setLayoutManager(layoutManager);
                    contactsRv.setAdapter(contactsAdapter);

                    contactsAmountTv.setText(contactsAmountStr);
                }
                else
                    onContactsFetched();
            }
        }, 1000);
    }
    private void onMessagesFetched() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(Dashboard.this, LinearLayoutManager.VERTICAL, false);
        messagesRv.setLayoutManager(layoutManager);
        messagesRv.setAdapter(new MessageRVAdapter(this, messagesList));
    }

    private void applyContactList(XMPPThread.RESULT rst, XMPPThread.REQUEST_TYPE req) {

        if(rst == XMPPThread.RESULT.SUCCESS && req == XMPPThread.REQUEST_TYPE.FETCH_CONTACTS) {

            contactsList = XMPPThread.getCachedContacts();

            layoutManager = new LinearLayoutManager(Dashboard.this, LinearLayoutManager.VERTICAL, false);
            contactsAdapter = new ContactsAdapter(this, contactsList);

            contactsAmountStr = "Contacts Added: " + contactsList.size();
            finishedFetchingContacts = true;
            }

    }

    private void onIncomingMessage(EntityBareJid from, Message message, Chat chat) {

        Snackbar.make(contactsRv, "Receiving Message From " + from.getLocalpart().toString(), Snackbar.LENGTH_LONG).show();
        System.out.println("Receiving Message From " + from.getLocalpart().toString());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setReceiverId(XMPPThread.getUserJid());
        chatMessage.setMessageContent(message.getBody().toString());
        chatMessage.setSenderId(from.toString());
        chatMessage.setMessageTimestamp(new Date().toString());

        DBHelper db = new DBHelper(Dashboard.this);
        db.write(chatMessage);

        loadMessages();
    }

    private void onOutgoingMessage(EntityBareJid to, MessageBuilder messageBuilder, Chat chat) {

        Snackbar.make(contactsRv, "Sending Message To " + to.getLocalpart().toString(), Snackbar.LENGTH_LONG).show();
        System.out.println("Sending Message To " + to.getLocalpart().toString());
    }
}