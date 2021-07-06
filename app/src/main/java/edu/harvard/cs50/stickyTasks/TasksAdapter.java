package edu.harvard.cs50.stickyTasks;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.harvard.cs50.stickyTasks.ui.tasks.TasksFragment;

import static edu.harvard.cs50.stickyTasks.MainActivity.database;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        LinearLayout containerView;
        CheckBox checkBox;

        TaskViewHolder(View view) {
            super(view);
            containerView = view.findViewById(R.id.task_row);
            checkBox = view.findViewById(R.id.task_row_check);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            final Task current = (Task) containerView.getTag();
                            if (current.done == 0) {
                                database.taskDao().done(current.id, 1);
                                checkBox.setChecked(true);
                            }
                            else {
                                database.taskDao().done(current.id, 0);
                                checkBox.setChecked(false);
                            }
                            reload();
                        }
                    }, 500);
                }
            });

        }
    }

    private List<Task> tasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task current = tasks.get(position);
        holder.checkBox.setText(current.contents);
        if (current.done == 0) {
            holder.checkBox.setChecked(false);
        }
        else {
            holder.checkBox.setChecked(true);
        }
        if (current.color.equals("yellow")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FDD835"));
        }
        else if (current.color.equals("light_blue")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#3CC1FF"));
        }
        else if (current.color.equals("pink")) {
            holder.containerView.setBackgroundColor(Color.parseColor("#FF6FA4"));
        }
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void reload() {
        tasks = database.taskDao().getAllTasks();
        TasksFragment.updateBar(percentDone());
        notifyDataSetChanged(); // tell RecyclerView we've just updated, reload self
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteTaskAtPosition(int position) {
        database.taskDao().delete(tasks.get(position));
        tasks.remove(position);
        notifyDataSetChanged();
        reload();
    }

    public int percentDone() {
        if (database.taskDao().totalTasks() != 0) {
            return database.taskDao().numCompleted()*100/database.taskDao().totalTasks();
        }
        else {
            return 0;
        }
    }

    public void onRowMoved(int initial, int target) {
        if (initial < target) {
            for (int i = initial; i < target; i++) {
                Collections.swap(tasks, i, i + 1);
            }
        }
        else {
            for (int i = initial; i > target; i--) {
                Collections.swap(tasks, i, i - 1);
            }
        }
        notifyItemMoved(initial, target);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRowDropped(int initial, int target) {
        Log.i("TasksFragment", "positions: "+initial+", "+target);
        Task current;
        database.taskDao().deleteAllTasks();
        for (int i = 0; i < tasks.size(); i++) {
            current = tasks.get(i);
            database.taskDao().save(current.contents, current.color, current.done, i+1);
        }
        notifyDataSetChanged();
        reload();
    }
}

