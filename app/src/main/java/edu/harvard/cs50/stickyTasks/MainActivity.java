package edu.harvard.cs50.stickyTasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import edu.harvard.cs50.stickyTasks.ui.tasks.TasksFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    boolean isRotate = false;
    private AlertDialog.Builder builder;
    public AlertDialog dialog;
    public static TaskDatabase database;
    private int id;
    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "tasks")
                .allowMainThreadQueries()
                .build();

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating action buttons
        final FloatingActionButton fab = findViewById(R.id.fab);
        final ExtendedFloatingActionButton addTaskFab = findViewById(R.id.add_task);
        final ExtendedFloatingActionButton addRemindFab = findViewById(R.id.add_remind);

        // Layout alert dialog
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final EditText field = findViewById(R.id.edit_task);
        final View view = layoutInflater.inflate(R.layout.popup_form, null);
        ViewAnimation.init(addTaskFab);
        ViewAnimation.init(addRemindFab);

        // Click listeners
        addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Loading Task...", Toast.LENGTH_SHORT).show();
                options(fab, addTaskFab, addRemindFab);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTask();
                    }
                }, 500);

            }
        });
        addRemindFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the options
                options(fab, addTaskFab, addRemindFab);
                Toast.makeText(MainActivity.this, "Loading Reminder...", Toast.LENGTH_SHORT).show();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the options
                options(view, addTaskFab, addRemindFab);
            }
        });
        // Navigation drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void options(View v, ExtendedFloatingActionButton a, ExtendedFloatingActionButton b) {
        isRotate = ViewAnimation.rotateFab(v, !isRotate);
        if(isRotate){
            ViewAnimation.showIn(a);
            ViewAnimation.showIn(b);
        }
        else{
            ViewAnimation.showOut(a);
            ViewAnimation.showOut(b);
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.yellow_color:
                if (checked)
                    changeColor(R.color.yellow);
                    color = "yellow";
                break;
            case R.id.blue_color:
                if (checked)
                    changeColor(R.color.light_blue);
                    color = "light_blue";
                break;
            case R.id.pink_color:
                if (checked)
                    changeColor(R.color.pink);
                    color = "pink";
                break;
        }
        Log.i("MainActivity", "The button color: "+color);
    }

    public void changeColor(int c) {
        dialog.getWindow().setBackgroundDrawableResource(c);
    }

    public void startTask() {
        color = "yellow";
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.popup_form, null);
        final EditText field = view.findViewById(R.id.edit_task);
        builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface diIn, int theId) {
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                database.taskDao().save(field.getText().toString(), color, 0, 0);
                TasksFragment.adapter.reload();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                field.getText().clear();
                dialog.cancel();
                TasksFragment.adapter.reload();
            }
        });
        dialog = builder.create();
        dialog.setView(view);
        changeColor(R.color.yellow);
        dialog.show();
    }
}