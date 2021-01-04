package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks.add(new Task(1,"Task 1", false));

        ListView llista = (ListView)findViewById(R.id.listview);
        taskadapter llocsadapter = new taskadapter(getApplicationContext(),R.layout.activity_main,tasks);
        llista.setAdapter((ListAdapter) llocsadapter);


    }
}