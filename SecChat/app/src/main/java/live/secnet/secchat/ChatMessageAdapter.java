package live.secnet.secchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends RecyclerView.Adapter {

    private List dataList;
    private Context mContext;

    @Override
    public int getItemViewType(int position) {

        if(SentChatMessage.class.isInstance(dataList.get(position))) {
            return 0;
        }

        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == 0)
        {
            System.out.println("Sent Message Holder: " + viewType);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message, parent, false);
            return new SentMessageHolder(view);
        }

        System.out.println("Received Message Holder: " + viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message, parent, false);
        return new ReceivedMessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        System.out.println("Type: " + this.getItemViewType(position) + " | POsition: " + position);

        if(this.getItemViewType(position) == 0)
        {
            System.out.println("Message Sent!");
            //Sent Message Layout
            SentChatMessage sentChatMessage = (SentChatMessage)dataList.get(position);
            SentMessageHolder sentMessageHolder = (SentMessageHolder)holder;

            sentMessageHolder.messageText.setText(sentChatMessage.getMessageText());
            sentMessageHolder.dateTimeText.setText(sentChatMessage.getDateTimeText());

            sentMessageHolder.messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //OnClick Function
                }
            });
        }
        else
        {
            //Received Message Layout
            System.out.println("Message Received: " + holder.getItemViewType());
            ReceivedChatMessage receivedChatMessage = (ReceivedChatMessage)dataList.get(position);
            ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder)holder;

            Glide.with(mContext).asBitmap().load(receivedChatMessage.getPicUrl()).into(receivedMessageHolder.profileImage);
            receivedMessageHolder.messageText.setText(receivedChatMessage.getMessageText());
            receivedMessageHolder.dateTimeText.setText(receivedChatMessage.getDateTimeText());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public ChatMessageAdapter(Context mContext, List dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    public class SentMessageHolder extends RecyclerView.ViewHolder
    {
        private TextView messageText;
        private TextView dateTimeText;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.textMessage);
            dateTimeText = itemView.findViewById(R.id.dateTimeText);
        }
    }

    public class ReceivedMessageHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView profileImage;
        private TextView messageText;
        private TextView dateTimeText;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.imageProfile);
            messageText = itemView.findViewById(R.id.textMessage);
            dateTimeText = itemView.findViewById(R.id.dateTimeText);
        }
    }
}
