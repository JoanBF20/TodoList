package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;

public class EditionTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition_task);

        //Recollir informació del Intent i mostrar-la
        Bundle extras = getIntent().getExtras();
        final int position = Integer.parseInt(extras.getString("Position"));

        Task tasca = (Task) getIntent().getSerializableExtra("Tasca");
        final String titolGeneral = tasca.getTitle();
        final String descripcio = tasca.getDescription();
        final boolean completat = tasca.isComplete();

        //instanciam cada element del layout a utilitzar
        EditText title = (EditText) findViewById(R.id.title);
        EditText description = (EditText) findViewById(R.id.description);

        //omplim les dades
        title.setText(tasca.getTitle());
        description.setText(tasca.getDescription());

        Button guardar = (Button) findViewById(R.id.save);
        guardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText titol = findViewById(R.id.title);
                String boto = "Editar";
                Task tasca = new Task(titolGeneral,descripcio,completat);
                Intent data = new Intent();
                data.putExtra("ModificatObjecte",  tasca);
                data.putExtra("ModificatString", boto);
                data.putExtra("ModificatPosicio", position);
                setResult(RESULT_OK,data);
                finish();
            }
        });

        Button borrar = (Button) findViewById(R.id.delete);
        borrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent data = new Intent();
                String boto2 = "Borrar";
                data.putExtra("Borrar", position);
                data.putExtra("BorrarString", boto2);
                setResult(RESULT_OK,data);
                finish();
            }
        });


    }


}