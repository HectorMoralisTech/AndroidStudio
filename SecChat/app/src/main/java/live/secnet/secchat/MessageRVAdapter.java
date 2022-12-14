package live.secnet.secchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRVAdapter extends RecyclerView.Adapter<MessageRVAdapter.ViewHolder> {

    private List<ChatMessage> messagesList = new ArrayList<>();

    private Context mContext;

    public MessageRVAdapter(Context mContext, List<ChatMessage> messages) {
        this.mContext = mContext;
        this.messagesList = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ChatContact contact = XMPPThread.getChatContact(messagesList.get(position).getSenderId());

        holder.messageTitleText.setText(contact.getContactName());
        holder.messageDescText.setText(messagesList.get(position).getMessageContent());
        holder.messageTimeText.setText(messagesList.get(position).getMessageTimestamp());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext.getApplicationContext(), Chat.class);
                mContext.startActivity(intent);
            }
        });

        if(contact.getImageBytes() != null) {
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(contact.getImageBytes(), 0, contact.getImageBytes().length);
            holder.messageImageView.setImageBitmap(imageBitmap);
        }

        if(contact.getContactName() == "Anonymous") {
            holder.messageImageView.setPadding(15, 15, 15,15);
        }
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView messageImageView;
        private TextView messageTitleText;
        private TextView messageDescText;
        private TextView messageTimeText;
        private CardView container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageImageView = itemView.findViewById(R.id.userPictureIv);
            messageTitleText = itemView.findViewById(R.id.usernameIv);
            messageDescText = itemView.findViewById(R.id.messageTv);
            messageTimeText = itemView.findViewById(R.id.timeTv);
            container = itemView.findViewById(R.id.messageContainer);
        }
    }
}
