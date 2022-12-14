package live.secnet.secchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.hardware.lights.LightState;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Pair;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private RecyclerView chatRv;
    private CircleImageView userImage;
    private List dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setupHooks();
        populateDateList();
        updateChatRV();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Chat.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void setupHooks()
    {
        chatRv = findViewById(R.id.chatRv);
        userImage = findViewById(R.id.userPicture);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, ProfileActivity.class);
                Pair animElement = new Pair(userImage, "userPictureTr");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Chat.this, animElement);
                startActivity(intent, options.toBundle());
            }
        });
    }
    private void populateDateList()
    {
        Glide.with(this).asBitmap().load("https://i.redd.it/qn7f9oqu7o501.jpg").into(userImage);

        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d, yyyy ", d.getTime());

        dataList.add(new SentChatMessage(
                "This is a test Sent Text Message Capitalized On Letters",
                s.toString()
        ));

        dataList.add(new SentChatMessage(
                "This is another test Sent Text Message Capitalized On Letters",
                s.toString()
        ));

        dataList.add(new ReceivedChatMessage(
                "https://i.redd.it/qn7f9oqu7o501.jpg",
                "This is a received test message Non capitalized on letters",
                s.toString()
        ));

        dataList.add(new SentChatMessage(
                "End of test ok.",
                s.toString()
        ));
    }
    private void updateChatRV()
    {
        chatRv.setHasFixedSize(true);
        chatRv.setLayoutManager(new LinearLayoutManager(this));
        chatRv.setAdapter(new ChatMessageAdapter(this, dataList));
    }
}