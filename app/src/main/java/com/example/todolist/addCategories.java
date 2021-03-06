package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class addCategories extends AppCompatActivity {

    private DBInterface bd;
    private EditText titleText;
    private ImageView img;
    private Bitmap bitmap;
    private Button btnAfegir;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);

        titleText = (EditText) this.findViewById(R.id.titol);
        img = (ImageView) this.findViewById(R.id.imatge);

        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        bitmap = Bitmap.createBitmap(img.getDrawingCache());

        btnAfegir = (Button) findViewById(R.id.afegir);
        btnAfegir.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                bd = new DBInterface(getApplicationContext());
                bd.obre();
                if (bd.insereixCategoria(titleText.toString(), bitmap) != -1) {
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