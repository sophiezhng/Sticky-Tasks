package edu.harvard.cs50.stickyTasks;

public class Timer {

    //Instance Variables
    private int hour;
    private int min;

    // Constructors
    public Timer(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    //Behavioural Methods
    // Nothing yet

    // Getters/Setters
    public int getHour() {
        return hour;
    }
    public int getMin() {
        return min;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public void setMin(int min) {
        this.min = min;
    }
}
