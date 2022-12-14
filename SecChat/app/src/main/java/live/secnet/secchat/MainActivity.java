package live.secnet.secchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.pgpainless.key.selection.keyring.impl.XMPP;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_LOAD_TIME = 3000;

    private Animation topAnim;
    private Animation bottomAnim;

    private ImageView logoImg;
    private TextView sloganTxt;

    private static final String XMPP_HOST = "xmpp.secnet.live";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#000000"));

        initXMPP();
        setupHooks();
        loadLogin();
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    private void initXMPP()
    {
        XMPPThread.ConnectionRequest connReq = new XMPPThread.ConnectionRequest(XMPP_HOST, null);
        connReq.start();
    }
    private void setupHooks()
    {
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        logoImg = findViewById(R.id.logoImg);

        logoImg.setAnimation(topAnim);
    }

    private void loadLogin()
    {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                Pair animElement = new Pair<View, String>(logoImg, "logo_image");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, animElement);
                startActivity(intent, options.toBundle());
            }
        }, SPLASH_SCREEN_LOAD_TIME);
    }


}