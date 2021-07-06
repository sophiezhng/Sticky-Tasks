package edu.harvard.cs50.stickyTasks.ui.tasks;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.harvard.cs50.stickyTasks.MainActivity;
import edu.harvard.cs50.stickyTasks.R;
import edu.harvard.cs50.stickyTasks.TasksAdapter;

public class TasksFragment extends Fragment {

    private TasksViewModel tasksViewModel;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    public static ProgressBar progressBar;

    public static TasksAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tasksViewModel =
                ViewModelProviders.of(this).get(TasksViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        progressBar = root.findViewById(R.id.progressBar);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new TasksAdapter();

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        updateBar(adapter.percentDone());

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            boolean mOrderChanged;
            int initialPos;
            int targetPos;
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                initialPos = viewHolder.getAdapterPosition();
                targetPos = target.getAdapterPosition();
                if (initialPos != targetPos) {
                    adapter.onRowMoved(initialPos, targetPos);
                    mOrderChanged = true;
                }
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                final int position = viewHolder.getAdapterPosition();
                adapter.deleteTaskAtPosition(position);
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && mOrderChanged) {
                    adapter.onRowDropped(initialPos, targetPos);
                    mOrderChanged = false;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

//        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                Toast.makeText(getActivity(), "onDoubleTap", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                Toast.makeText(getActivity(), "onLongPress", Toast.LENGTH_SHORT).show();
//            }
//        };
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        adapter.reload();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateBar(int progress) {
        progressBar.setProgress(progress, true);

    }
}