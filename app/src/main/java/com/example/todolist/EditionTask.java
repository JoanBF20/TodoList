package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

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

        //omplim les dades
        title.setText(tasca.getTitle());
        description.setText(tasca.getDescription());

        Button guardar = (Button) findViewById(R.id.save);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent data = new Intent();
                tasca.setTitle(title.getText().toString());
                tasca.setDescription(description.getText().toString());
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