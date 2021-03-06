package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class addCategories extends AppCompatActivity {

    private DBInterface bd;
    private EditText titleText;
    private ImageView img;
    private Button btnAfegir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);

        titleText = (EditText) this.findViewById(R.id.titol);
        img = (ImageView) this.findViewById(R.id.imatge);


        btnAfegir = (Button) findViewById(R.id.addButton);
        btnAfegir.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                bd = new DBInterface(getApplicationContext());
                bd.obre();
                if (bd.insereixCategoria(titleText.toString(),img)) {
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