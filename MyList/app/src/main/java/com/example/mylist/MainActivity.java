package com.example.mylist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mylist.Adaptor.ToDoAdaptor;
import com.example.mylist.Model.ToDoModel;
import com.example.mylist.Utils.DatabaseHandler;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.*;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private DatabaseHandler db;
    private RecyclerView taskRecyclerView;
    private ToDoAdaptor taskAdaptor;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskRecyclerView = findViewById(R.id.taskRecycleView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdaptor = new ToDoAdaptor(db,MainActivity.this);
        taskRecyclerView.setAdapter(taskAdaptor);

        ItemTouchHelper ItemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(taskAdaptor));
        ItemTouchHelper.attachToRecyclerView(taskRecyclerView);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdaptor.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse((taskList));
        taskAdaptor.setTasks(taskList);
        taskAdaptor.notifyDataSetChanged();
    }
}