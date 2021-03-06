package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditionTask extends AppCompatActivity {

    Task tasca;
    DBInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition_task);

        db = new DBInterface(getApplicationContext());
        db.obre();

        //Recollir informació del Intent i mostrar-la
        Intent intent = getIntent();
        final int id = intent.getIntExtra("Id",0);

        tasca = db.obtenirTasca(id);
        final boolean completat = tasca.isComplete();

        //instanciam cada element del layout a utilitzar
        final EditText title = (EditText) findViewById(R.id.title);
        final EditText description = (EditText) findViewById(R.id.description);

        final Spinner categoriaSpinner = (Spinner) this.findViewById(R.id.category);

        List<String> spinnerArray = new ArrayList<String>();
        for (Category category: MainActivity.categories)
            spinnerArray.add(category.title);

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray);

        categoriaSpinner.setAdapter(spinnerArrayAdapter);

        //omplim les dades
        for (Category category: MainActivity.categories)
            if (category.getId() == tasca.getIdCategoria())
                categoriaSpinner.setSelection(MainActivity.categories.indexOf(category));

        title.setText(tasca.getTitle());
        description.setText(tasca.getDescription());

        Button guardar = (Button) findViewById(R.id.save);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent data = new Intent();
                tasca.setTitle(title.getText().toString());
                tasca.setDescription(description.getText().toString());
                tasca.setIdCategoria(MainActivity.categories.get(categoriaSpinner.getSelectedItemPosition()).getId());
                data.putExtra("Tasca",tasca);
                data.putExtra("Accio", 1);
                data.putExtra("Id", id);
                setResult(RESULT_OK,data);
                finish();
            }
        });

        Button borrar = (Button) findViewById(R.id.delete);
        borrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();
                String boto2 = "Borrar";
                data.putExtra("Id", tasca.getId());
                data.putExtra("Accio", 2);
                setResult(RESULT_OK,data);
                finish();
            }
        });


    }


}