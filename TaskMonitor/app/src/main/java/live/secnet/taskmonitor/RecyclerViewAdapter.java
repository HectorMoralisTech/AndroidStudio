package live.secnet.taskmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList= new ArrayList<Task>();

    public RecyclerViewAdapter(List<Task> _tasks, Context _context)
    {
        this.context = _context;
        this.taskList = _tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        TaskViewHolder taskView = new TaskViewHolder(view);

        return taskView;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder _holder, int _pos) {

        _holder.nameText.setText(taskList.get(_pos).getTaskName());
        _holder.dateText.setText(taskList.get(_pos).getTaskDate());
        _holder.descText.setText(taskList.get(_pos).getTaskDesc());
        _holder.imageView.setImageResource(R.drawable.clock);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView nameText;
        private TextView dateText;
        private TextView descText;

        public TaskViewHolder(@NonNull View _itView) {
            super(_itView);

            imageView = _itView.findViewById(R.id.ivTaskStatus);
            nameText = _itView.findViewById(R.id.txtTaskName);
            dateText = _itView.findViewById(R.id.txtTaskDate);
            descText = _itView.findViewById(R.id.txtTaskDesc);
        }
    }
}
