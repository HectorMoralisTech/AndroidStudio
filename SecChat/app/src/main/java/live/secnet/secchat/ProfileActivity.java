package live.secnet.secchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private CircleImageView userPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupHooks();
        loadProfile();
    }

    @Override
    public void onBackPressed()
    {
        goBack();
    }

    private void setupHooks()
    {
        backButton = findViewById(R.id.backButton);
        userPicture = findViewById(R.id.imageProfile);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
    }
    private void loadProfile()
    {
        Glide.with(this).asBitmap().load("https://i.redd.it/qn7f9oqu7o501.jpg").into(userPicture);
    }

    private void goBack()
    {
        Intent intent = new Intent(ProfileActivity.this, Chat.class);
        Pair animElement = new Pair(userPicture, "userPictureTr");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this, animElement);
        startActivity(intent, options.toBundle());
    }
}