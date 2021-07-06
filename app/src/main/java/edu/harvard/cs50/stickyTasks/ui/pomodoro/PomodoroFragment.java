package edu.harvard.cs50.stickyTasks.ui.pomodoro;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import edu.harvard.cs50.stickyTasks.R;
import edu.harvard.cs50.stickyTasks.Timer;

public class PomodoroFragment extends Fragment {

    private PomodoroViewModel pomodoroViewModel;
    private TimePicker picker;
    private int[] time;
    private Timer work;
    private Timer breakFrom;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pomodoroViewModel =
                ViewModelProviders.of(this).get(PomodoroViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pomodoro, container, false);
        picker = root.findViewById(R.id.time_picker);
        picker.setIs24HourView(true);
        setTime(0,52);
        final Button set = root.findViewById(R.id.button);
        time = new int[2];
        set.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                time = getTime();
                work = new Timer(time[0], time [1]);
                PomodoroBreakFragment nextFrag= new PomodoroBreakFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFrag, "findBreakFragment")
                        .addToBackStack(null)
                        .commit();

            }
        });
        return root;
    }

    private void setTime(int hour, int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.setHour(hour);
            picker.setMinute(minute);
        }
        else {
            picker.setCurrentHour(hour);
            picker.setCurrentMinute(minute);
        }
    }

    private int[] getTime() {
        int hour;
        int min;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = picker.getHour();
            min = picker.getMinute();
        }
        else {
            hour = picker.getCurrentHour();
            min = picker.getCurrentMinute();
        }
        return new int[]{hour, min};
    }
}