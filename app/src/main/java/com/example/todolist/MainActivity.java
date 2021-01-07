package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static List<Task> tasks = new ArrayList<Task>();
    taskadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks.add(new Task(1,"Task 1", "Description of Task 1", false));
        tasks.add(new Task(2,"Task 2", "Description of Task 2", false));

        ListView llista = (ListView)findViewById(R.id.listview);
        adapter = new taskadapter(getApplicationContext(),R.layout.activity_main,tasks);
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

                Gson json = new Gson();
                String tasca_seleccionada = json.toJson(tasks.get(position));
                Intent nou = new Intent(getApplicationContext(), EditionTask.class);
                nou.putExtra("Tasca",tasca_seleccionada);

                Log.d("OJson",tasca_seleccionada);
                startActivity(nou);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                tasks.add(new Task(2, title, description, false));

                Toast toast = Toast.makeText(getApplicationContext(), "Tasca afegida", Toast.LENGTH_SHORT);
                toast.show();
                adapter.notifyDataSetChanged();

                if(getIntent().getExtras() != null) {
                    Task tasca = (Task) getIntent().getSerializableExtra("Modificat");
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        if (tasca.getCodi() == task.getCodi()){
                            task.setTitle(tasca.getTitle());
                            task.setDescription(tasca.getDescription());
                        }
                    }
                }
                if(getIntent().getExtras() != null) {
                    String code = data.getStringExtra("Borrar");
                    int cod = Integer.parseInt(code);
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        if (cod == task.getCodi()){
                            tasks.remove(i);
                        }
                    }
                }
            }
        }
    }
}