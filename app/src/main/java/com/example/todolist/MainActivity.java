package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static List<Task> tasks = new ArrayList<Task>();
    public static List<Category> categories = new ArrayList<Category>();
    public taskadapter adapter;
    public  Context context;
    DBInterface db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        db = new DBInterface(context);
        db.obre();
        tasks = db.obtenirTotesLesTasques();
        categories = db.obtenirTotesLesCategories();
        if (categories.size() < 1){
            db.insereixCategoria("Negocis",BitmapFactory.decodeResource(getResources(),R.drawable.business));
            db.insereixCategoria("Casa",BitmapFactory.decodeResource(getResources(),R.drawable.home));
            db.insereixCategoria("Estudis",BitmapFactory.decodeResource(getResources(),R.drawable.study));
            categories = db.obtenirTotesLesCategories();
        }
        final ListView llista = (ListView) findViewById(R.id.listview);
        adapter = new taskadapter(context, R.layout.activity_main, tasks);
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
                nou.putExtra("Id", tasks.get(position).getId());
                startActivityForResult(nou, 2);

            }
        });
    }

    public static Category getCategory(int categoryId){
        for (Category category : categories)
            if (category.getId() == categoryId)
                return category;

        return null;
    }

/*    private void saveData() {
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
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                int categoryPosition = data.getIntExtra("categoryPosition",0);
                db.insereixTasca(title, description, categories.get(categoryPosition).getId(), false);
                tasks.clear();
                tasks.addAll(db.obtenirTotesLesTasques());
                adapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(getApplicationContext(), "Tasca afegida", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        if (resultCode == RESULT_OK && requestCode == 2) {
            Bundle extras = data.getExtras();
            int id = extras.getInt("Id");
            int accio = extras.getInt("Accio");

            if (accio == 1) {

                String recepcio = extras.getString("ModificatObjecte");

                Task tasca = (Task) extras.getSerializable("Tasca");
                Toast toast = Toast.makeText(getApplicationContext(), "Tasca modificada", Toast.LENGTH_SHORT);
                toast.show();
                db.actualitzarTasca(tasca.getId(),tasca.getTitle(),tasca.getDescription(),tasca.getIdCategoria(),tasca.isComplete());
            }

            if (accio == 2) {
                db.esborraTasca(id);
                Toast toast = Toast.makeText(getApplicationContext(), "Tasca eliminada", Toast.LENGTH_SHORT);
                toast.show();
            }

            tasks.clear();
            tasks.addAll(db.obtenirTotesLesTasques());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //saveData();
    }

}

