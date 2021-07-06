package edu.harvard.cs50.stickyTasks;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1) // passing in a class + specify version database scheme
//Isn't my own implementation, instead by someone else
public abstract class TaskDatabase extends RoomDatabase { //abstract - between a class and an interface
    public abstract TaskDao taskDao();
}
