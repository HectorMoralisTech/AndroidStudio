package live.secnet.secchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.InetSocketAddress;

public class AddContact extends AppCompatActivity {

    private ImageView saveButton;
    private ImageView backButton;
    private EditText nameField;
    private EditText xmppField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        setupHooks();
    }

    @Override
    public void onBackPressed()
    {
        loadDashboard();
    }

    private void setupHooks()
    {
        nameField = findViewById(R.id.nameEditText);
        xmppField = findViewById(R.id.xmppEditText);

        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChatContact contact = new ChatContact();
                contact.setContactName(nameField.getText().toString());
                contact.setContactAddress(xmppField.getText().toString());

                DBHelper db = new DBHelper(AddContact.this);
                boolean success = db.write(contact);

                if(!success) {
                    Toast.makeText(AddContact.this, "Error Saving Contact On Database!", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(AddContact.this, "Contact Was Added Successfully!", Toast.LENGTH_LONG).show();
                loadDashboard();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadDashboard();
            }
        });
    }
    private void loadDashboard()
    {
        Intent intent = new Intent(AddContact.this, Dashboard.class);
        startActivity(intent);
        finish();
    }
}