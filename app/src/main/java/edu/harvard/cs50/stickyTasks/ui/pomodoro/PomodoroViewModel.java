package edu.harvard.cs50.stickyTasks.ui.pomodoro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PomodoroViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PomodoroViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}