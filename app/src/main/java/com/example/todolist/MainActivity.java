package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.Spinner;
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
    public static taskadapter adapter;
    public  Context context;
    public static DBInterface db;
    private static Spinner categoriaSpinner;

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

        categoriaSpinner = (Spinner) this.findViewById(R.id.filtreCategories);

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Totes");
        for (Category category: MainActivity.categories)
            spinnerArray.add(category.title);

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray);

        categoriaSpinner.setAdapter(spinnerArrayAdapter);

        categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                updateAdapter();
            }

        });

        loadData();

        FloatingActionButton myFab = (FloatingActionButton) this.findViewById(R.id.floatingAdd);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addTask.class);
                startActivityForResult(intent, 1);
            }
        });

        FloatingActionButton myFabCategory = (FloatingActionButton) this.findViewById(R.id.floatAddCategory);
        myFabCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addCategories.class);
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

    private void saveData() {
        SharedPreferences sp = getSharedPreferences("todolist", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("spinnerPos", categoriaSpinner.getSelectedItemPosition());
        editor.commit();
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("todolist", Activity.MODE_PRIVATE);
        int pos = sp.getInt("spinnerPos", -1);
        categoriaSpinner.setSelection(pos);
    }

    public static void updateAdapter() {
        if (categoriaSpinner.getSelectedItemPosition() == 0){
            tasks.clear();
            tasks.addAll(db.obtenirTotesLesTasques());
        } else {
            tasks.clear();
            tasks.addAll(db.obtenirTotesLesTasquesCastegoria(categories.get(categoriaSpinner.getSelectedItemPosition()-1).getId()));
        }
        categories.clear();
        categories.addAll(db.obtenirTotesLesCategories());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                String title = data.getStringExtra("title");
                String description = data.getStringExtra("description");
                int categoryPosition = data.getIntExtra("categoryPosition",0);
                db.insereixTasca(title, description, categories.get(categoryPosition).getId(), false);
                updateAdapter();
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

            updateAdapter();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

}

