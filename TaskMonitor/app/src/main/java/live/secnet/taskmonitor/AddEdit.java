package live.secnet.taskmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddEdit extends AppCompatActivity {

    Button btnBack;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        btnBack = findViewById(R.id.btnBack);
        btnCreate = findViewById(R.id.btnCreateTask);

        btnBack.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View _v)
           {
               Intent intent = new Intent(AddEdit.this, MainActivity.class);
               startActivity(intent);
           }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddEdit.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}