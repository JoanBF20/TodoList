package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class addTask extends AppCompatActivity {

    private DBInterface bd;
    private EditText titleText, descriptionText;
    private Button btnAfegir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        titleText = (EditText) this.findViewById(R.id.editTextTextPersonName);
        descriptionText = (EditText) this.findViewById(R.id.editTextTextMultiLine);

        btnAfegir = (Button) findViewById(R.id.addButton);
        btnAfegir.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                bd = new DBInterface(getApplicationContext());
                bd.obre();
                if (bd.insereixTasca(titleText.getText().toString(), descriptionText.getText().toString(), 1) != -1) {
                    Toast.makeText(getApplicationContext(), "Afegit correctament", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error a l’afegir", Toast.LENGTH_SHORT).show();
                }
                bd.tanca();
                finish();
            }
        });
    }
}