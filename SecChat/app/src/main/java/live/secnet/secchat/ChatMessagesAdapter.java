package live.secnet.secchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder>{

    private Context mContext;
    private List<ChatMessage> messagesList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatMessage message = messagesList.get(position);
        ChatContact contact = null;

        for(int n = 0 ; n < XMPPThread.getCachedContacts().size(); n++) {

            if(XMPPThread.getCachedContacts().get(n).getContactAddress() == message.getSenderId()) {
                contact = XMPPThread.getCachedContacts().get(n);
                break;
            }
        }

        if(contact == null) {
            return;
        }

        holder.usernameTv.setText(contact.getContactName());
        holder.timeTv.setText(message.getMessageTimestamp());
        holder.messageTv.setText(message.getMessageContent());

        if(contact.getImageBytes() != null) {
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(contact.getImageBytes(), 0, contact.getImageBytes().length);
            holder.userPictureIv.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTv;
        private TextView messageTv;
        private TextView usernameTv;

        private CircleImageView userPictureIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTv = itemView.findViewById(R.id.timeTv);
            messageTv = itemView.findViewById(R.id.messageTv);
            usernameTv = itemView.findViewById(R.id.usernameIv);
            userPictureIv = itemView.findViewById(R.id.userPictureIv);
        }
    }
}
