package edu.harvard.cs50.stickyTasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    // Getters
    @Query("SELECT * FROM tasks ORDER BY viewOrder ASC, id DESC")
    List<Task> getAllTasks();

    @Query("SELECT contents FROM tasks WHERE id = :id")
    String getTask(int id);

    @Query("SELECT COUNT(done) FROM tasks WHERE done = 1")
    int numCompleted();

    @Query("SELECT COUNT(*) FROM tasks")
    int totalTasks();

    @Delete
    void delete(Task task);

    // Setters
    @Query("INSERT INTO tasks (contents, color, done, viewOrder) VALUES (:contents, :color, :done, :viewOrder)")
    void save(String contents, String color, int done, int viewOrder);

    @Query("DELETE FROM tasks")
    void deleteAllTasks();

    @Query("UPDATE tasks SET viewOrder = :viewOrder WHERE id = :id")
    void saveOrder(int id, int viewOrder);

    @Query("UPDATE tasks SET done = :isDone WHERE id = :id")
    void done(int id, int isDone);

    @Query("UPDATE tasks SET contents = :contents AND color = :color WHERE id = :id")
    void edit(int id, String contents, String color);


}
