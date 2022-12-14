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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class Login extends AppCompatActivity {

    private ImageView logoImage;

    private TextView logoTitleText;
    private TextView logoDescText;
    private volatile EditText usernameEditText;
    private volatile EditText passwordEditText;
    private TextInputLayout usernameField;
    private TextInputLayout passwordField;
    private volatile Button signupButton;
    private volatile Button loginButton;

    private String username;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setStatusBarColor(Color.rgb(0,0,0));
        setupHooks();
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    private void setupHooks()
    {
        logoImage = findViewById(R.id.logoImg);

        logoTitleText = findViewById(R.id.logoNameTxt);
        logoDescText = findViewById(R.id.logoDescTxt);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        usernameField = findViewById(R.id.usernameInputField);
        passwordField = findViewById(R.id.passwordInputField);

        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);

                Pair[] elements = new Pair[7];
                elements[0] = new Pair<View, String>(logoImage, "logo_image");
                elements[1] = new Pair<View, String>(logoTitleText, "logo_name");
                elements[2] = new Pair<View, String>(logoDescText, "logo_desc");
                elements[3] = new Pair<View, String>(usernameField, "field_username");
                elements[4] = new Pair<View, String>(passwordField, "field_password");
                elements[5] = new Pair<View, String>(signupButton, "button_create");
                elements[6] = new Pair<View, String>(loginButton, "button_login");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, elements);
                startActivity(intent, options.toBundle());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateFields()) {
                    return;
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blockInputs(false);
                    }
                },2000);

                XMPPThread.LoginRequest loginReq = new XMPPThread.LoginRequest(username, password, (rst, req) -> onXMPPResult(rst, req));
                loginReq.start();
            }
        });
    }

    private void loadChat()
    {
        Intent intent = new Intent(Login.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
    private void blockInputs(boolean _val)
    {
        usernameEditText.setEnabled(!_val);
        passwordEditText.setEnabled(!_val);

        loginButton.setEnabled(!_val);
        signupButton.setEnabled(!_val);
    }
    private boolean validateFields()
    {
        blockInputs(true);

        String tmpUsername = usernameEditText.getText().toString();
        String tmpPassword = passwordEditText.getText().toString();

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

        username = tmpUsername;
        password = tmpPassword;

        return true;
    }

    private void onXMPPResult(XMPPThread.RESULT rst, XMPPThread.REQUEST_TYPE req)
    {
        switch(rst) {

            case SUCCESS:
                if(req == XMPPThread.REQUEST_TYPE.LOGIN)
                    loadChat();
                break;

            case ERROR_EXCEPTION:
                if(req == XMPPThread.REQUEST_TYPE.LOGIN)
                    Snackbar.make(usernameField, "Login Denied, Please Check Credentials.", Snackbar.LENGTH_LONG).show();
                break;

            default: break;
        }
    }
}