package live.secnet.secchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String CONTACTS_TABLE = "contacts";
    public static final String CONTACT_ID = "ContactID";
    public static final String CONTACT_NAME = "ContactName";
    public static final String CONTACT_ADDRESS = "ContactAddress";
    public static final String CONTACT_IMAGE_URL = "ContactImageUrl";

    public static final String MESSAGES_TABLE = "messages";
    public static final String MESSAGE_ID = "MessageID";
    public static final String MESSAGE_SENDER = "MessageSenderADR";
    public static final String MESSAGE_RECEIVER = "MessageReceiverADR";
    public static final String MESSAGE_CONTENT = "MessageContent";
    public static final String MESSAGE_TIMESTAMP = "MessageTimestamp";

    public DBHelper(@Nullable Context context) {
        super(context, "secchat.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String genContacts = "CREATE TABLE IF NOT EXISTS " + CONTACTS_TABLE + " (" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTACT_NAME + " TEXT, " + CONTACT_ADDRESS + " TEXT, " + CONTACT_IMAGE_URL + " TEXT)";
        sqLiteDatabase.execSQL(genContacts);

        String genMessages = "CREATE TABLE IF NOT EXISTS " + MESSAGES_TABLE + " (" + MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MESSAGE_SENDER + " TEXT, " + MESSAGE_RECEIVER + " TEXT, " + MESSAGE_CONTENT + " TEXT, " + MESSAGE_TIMESTAMP + " TEXT)";
        sqLiteDatabase.execSQL(genMessages);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }

    public boolean write(ChatContact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CONTACT_NAME, contact.getContactName());
        cv.put(CONTACT_ADDRESS, contact.getContactAddress());
        cv.put(CONTACT_IMAGE_URL, contact.getContactImageUrl());

        //TODO: Get Contact Profile Picture From XMPP Instance
        long result = db.insert(CONTACTS_TABLE, null, cv);

        if(result == -1){
            return false;
        }

        return true;
    }

    public boolean write(ChatMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MESSAGE_SENDER, message.getSenderId());
        cv.put(MESSAGE_RECEIVER, message.getReceiverId());
        cv.put(MESSAGE_CONTENT, message.getMessageContent());
        cv.put(MESSAGE_TIMESTAMP, message.getMessageTimestamp());

        long result = db.insert(MESSAGES_TABLE,null, cv);

        if(result == -1) {
            return false;
        }

        return true;
    }

    public List<ChatContact> getContacts()
    {
        List<ChatContact> contacts = new ArrayList<>();
        String qry = "SELECT * FROM " + CONTACTS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(qry, null);

        if(cursor.moveToFirst()) {

            do {

                ChatContact contact = new ChatContact();

                contact.setContactId(cursor.getInt(0));
                contact.setContactName(cursor.getString(1));
                contact.setContactAddress(cursor.getString(2));
                contact.setContactImageUrl(cursor.getString(3));
                contacts.add(contact);

            } while(cursor.moveToNext());
        }
        else
        {
            //TODO: Cas SQLite Does NO Have Any Contact Saved
        }

        cursor.close();
        db.close();

        return contacts;
    }

    public List<ChatMessage> getMessages(String contactName, int messagesAmount) {

        List<ChatMessage> lastMessages = new ArrayList<>();
        String qry = "SELECT * FROM " + MESSAGES_TABLE + " WHERE " + MESSAGE_SENDER + "=" + contactName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(qry, null);

        if(cursor.moveToFirst()) {

            do {

                ChatMessage message = new ChatMessage();
                message.setSenderId(cursor.getString(0));
                message.setReceiverId(cursor.getString(1));
                message.setMessageContent(cursor.getString(2));
                message.setMessageTimestamp(cursor.getString(3));

                lastMessages.add(message);

            }while(cursor.moveToNext());
        }

        return lastMessages;
    }
}
