package live.secnet.taskmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private Button btnAddTask;
    private List<Task> taskList = new ArrayList<Task>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddTask = findViewById(R.id.btnAddTask);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEdit.class);
                startActivity(intent);
            }
        });

        initRecyclerView();
        getWindow().setStatusBarColor(Color.rgb(15,15,15));
    }

    private void initRecyclerView()
    {
        recyclerView = findViewById(R.id.lvTasksList);
        recyclerView.setHasFixedSize(true);

        recyclerLayout = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(recyclerLayout);

        recyclerAdapter = new RecyclerViewAdapter(taskList, MainActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        taskList.add(new Task(0, "Lavar Louça", "Lavar a Louça Que Está Na Cozinha Hoje!", "12-10-2022"));
        taskList.add(new Task(1, "Lavar Louça", "Lavar a Louça Que Está Na Cozinha Hoje!", "11-10-2022"));
        taskList.add(new Task(2, "Lavar Louça", "Lavar a Louça Que Está Na Cozinha Hoje!", "10-10-2022"));
        taskList.add(new Task(3, "Lavar Louça", "Lavar a Louça Que Está Na Cozinha Hoje!", "09-10-2022"));
        taskList.add(new Task(4, "Lavar Louça", "Lavar a Louça Que Está Na Cozinha Hoje!", "11-10-2022"));
        taskList.add(new Task(5, "Lavar Louça", "Lavar a Louça Que Está Na Cozinha Hoje!", "10-10-2022"));
        taskList.add(new Task(6, "Lavar Louça", "Lavar a Louça Que Está Na Cozinha Hoje!", "09-10-2022"));
    }
}