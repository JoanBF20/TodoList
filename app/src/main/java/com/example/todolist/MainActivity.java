package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Task> tasks = new ArrayList<Task>();
    taskadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        final ListView llista = (ListView) findViewById(R.id.listview);
        adapter = new taskadapter(getApplicationContext(), R.layout.activity_main, tasks);
        llista.setAdapter((ListAdapter) adapter);

        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.floatingAdd);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addTask.class);
                startActivityForResult(intent, 1);
            }
        });

        llista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent nou = new Intent(getApplicationContext(), EditionTask.class);
                nou.putExtra("Tasca", tasks.get(position));
                nou.putExtra("Position", position);
                startActivityForResult(nou, 2);

            }
        });

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();
        tasks = gson.fromJson(json, type);
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                tasks.add(0, new Task(title, description, false));
                Toast toast = Toast.makeText(getApplicationContext(), "Tasca afegida", Toast.LENGTH_SHORT);
                toast.show();

                adapter.notifyDataSetChanged();
            }
        }

        if (resultCode == RESULT_OK && requestCode == 2) {
            Bundle extras = data.getExtras();
            int position = extras.getInt("Posicio");
            int accio = extras.getInt("Accio");

            if (accio == 1) {

                String recepcio = extras.getString("ModificatObjecte");

                Task tasca = (Task) extras.getSerializable("Tasca");
                Toast toast = Toast.makeText(getApplicationContext(), "Tasca modificada", Toast.LENGTH_SHORT);
                toast.show();
                tasks.set(position, tasca);
            }

            if (accio == 2) {
                tasks.remove(position);
                Toast toast = Toast.makeText(getApplicationContext(), "Tasca eliminada", Toast.LENGTH_SHORT);
                toast.show();
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }
}

