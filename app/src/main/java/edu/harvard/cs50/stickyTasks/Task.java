package edu.harvard.cs50.stickyTasks;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks") //represents a table
public class Task {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "contents") // a column in the table
    public String contents;

    @ColumnInfo(name = "color")
    public String color;

    @ColumnInfo(name = "done")
    public int done;

    @ColumnInfo(name = "viewOrder")
    public int viewOrder;
}
