package live.secnet.secchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatContact contact = contactsList.get(position);

        holder.contactName.setText(contact.getContactName());
        holder.contactAddress.setText(contact.getContactAddress());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Deleting User From Contacts List", Snackbar.LENGTH_LONG).show();
                //TODO: DELETE USER FROM CONTACT LIST

                ChatMessage message = new ChatMessage();
                message.setMessageTimestamp("NULL DATA");
                message.setMessageContent("Hello From SecChat App!");
                message.setReceiverId(contact.getContactAddress());

                //TODO: Save Message To Local Database

                XMPPThread.sendMessage(message, contact, new IncomingChatMessageListener() {
                    @Override
                    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                        Snackbar.make(holder.deleteButton, "Message Received " + from.toString() + ": " + message.getBody().toString(), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });

        if(contact.getImageBytes() != null) {
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(contact.getImageBytes(), 0, contact.getImageBytes().length);
            holder.contactImage.setImageBitmap(imageBitmap);
        }

        if(contact.getContactName() == "Anonymous") {
            holder.contactImage.setPadding(15, 15, 15,15);
        }

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    private List<ChatContact> contactsList = new ArrayList<>();

    public ContactsAdapter(Context mContext, List<ChatContact> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        TextView contactAddress;

        ImageView deleteButton;
        CircleImageView contactImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contactName = itemView.findViewById(R.id.usernameIv);
            contactAddress = itemView.findViewById(R.id.userAddressIv);

            deleteButton = itemView.findViewById(R.id.delIv);
            contactImage = itemView.findViewById(R.id.userImageIv);
        }
    }
}
