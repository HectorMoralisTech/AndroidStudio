package live.secnet.secchat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Signup extends AppCompatActivity {

    private Button loginButton;
    private Button createButton;

    private TextView logoNameText;
    private TextView logoDescText;

    private ImageView logoImage;

    private TextInputLayout usernameField;
    private TextInputLayout passwordField;
    private TextInputLayout cPasswordField;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText cPasswordEditText;

    private String username;
    private String password;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setStatusBarColor(Color.rgb(0,0,0));

        setupHooks();
        setupEvents();
    }

    @Override
    public void onBackPressed()
    {
        onLoginClick();
    }

    private void setupHooks()
    {
        loginButton = findViewById(R.id.loginButton);
        createButton = findViewById(R.id.createButton);

        logoNameText = findViewById(R.id.logoNameText);
        logoDescText = findViewById(R.id.logoDescText);

        logoImage = findViewById(R.id.logoImage);

        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        cPasswordField = findViewById(R.id.cPasswordField);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        cPasswordEditText = findViewById(R.id.cPasswordEditText);
    }
    private void setupEvents()
    {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClick();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateAccountClick(view);
            }
        });
    }

    private void onLoginClick()
    {
        Intent intent = new Intent(Signup.this, Login.class);
        Pair[] animElements = new Pair[7];

        animElements[0] = new Pair<View, String>(logoImage, "logo_image");
        animElements[1] = new Pair<View, String>(logoNameText, "logo_name");
        animElements[2] = new Pair<View, String>(logoDescText, "logo_desc");
        animElements[3] = new Pair<View, String>(usernameField, "field_username");
        animElements[4] = new Pair<View, String>(passwordField, "field_password");
        animElements[6] = new Pair<View, String>(createButton, "button_create");
        animElements[5] = new Pair<View, String>(loginButton, "button_login");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup.this, animElements);
        startActivity(intent, options.toBundle());
    }
    private void blockInputs(boolean _val) {

        usernameEditText.setEnabled(!_val);
        passwordEditText.setEnabled(!_val);
        cPasswordEditText.setEnabled(!_val);

        loginButton.setEnabled(!_val);
        createButton.setEnabled(!_val);
    }
    private boolean validateFields() {

        blockInputs(true);

        String tmpUsername = usernameEditText.getText().toString();
        String tmpPassword = passwordEditText.getText().toString();
        String tmpCPassword = cPasswordEditText.getText().toString();

        System.out.println(tmpCPassword + " : " + tmpPassword);

        if(tmpUsername.length() <= 1) {
            Snackbar.make(usernameField, "Username Field Is Empty!", Snackbar.LENGTH_LONG).show();
            blockInputs(false);
            return false;
        }

        if(tmpUsername.length() >= 15) {
            Snackbar.make(usernameField, "Username Is Too Large!", Snackbar.LENGTH_LONG).show();
            blockInputs(false);
            return false;
        }

        for(int n = 0; n < tmpUsername.length(); n++) {
            int asciiVal = (int)tmpUsername.charAt(n);

            if((asciiVal >= 32 && asciiVal <= 47) || (asciiVal >= 58 && asciiVal <= 64) || (asciiVal >= 91 && asciiVal <= 96) || (asciiVal >= 123 && asciiVal <= 126)) {
                Snackbar.make(usernameField, "Username Must Not Contain Special Characters", Snackbar.LENGTH_LONG).show();
                blockInputs(false);
                return false;
            }
        }

        if(tmpPassword.length() <= 1) {
            Snackbar.make(usernameField, "Password Field Is Empty!", Snackbar.LENGTH_LONG).show();
            blockInputs(false);
            return false;
        }

        if(tmpCPassword.length() <= 1) {
            Snackbar.make(usernameField, "Confirm Password Field Is Empty!", Snackbar.LENGTH_LONG).show();
            blockInputs(false);
            return false;
        }

        if(!tmpPassword.equals(tmpCPassword)) {
            Snackbar.make(usernameField, "Passwords Do Not Match!", Snackbar.LENGTH_LONG).show();
            blockInputs(false);
            return false;
        }

        username = usernameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        email = username + "@xmpp.secnet.live";

        return true;
    }
    private void onCreateAccountClick(View view) {

        if(!validateFields()) {
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                blockInputs(false);
            }
        },2000);

        XMPPThread.RegisterRequest regReq = new XMPPThread.RegisterRequest(username, password, email, new XMPPResult() {
            @Override
            public void onResultCallback(XMPPThread.RESULT rst, XMPPThread.REQUEST_TYPE req) {
                if(rst == XMPPThread.RESULT.SUCCESS && req == XMPPThread.REQUEST_TYPE.REGISTER) {

                    Snackbar.make(view, "Account Created Successfully!", Snackbar.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onLoginClick();
                        }
                    }, 3000);
                }
                else {
                    Snackbar.make(view, "Something Went Wrong!", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        regReq.start();
    }
}