package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class addCategories extends AppCompatActivity{

    private int GALLERY_REQUEST_CODE = 1;
    private int APP_PERMISSION_READ_STORAGE = 1;
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

        img.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                if (v == img) {
                    if (ContextCompat.checkSelfPermission(addCategories.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(addCategories.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, APP_PERMISSION_READ_STORAGE);
                    }
                    recullDeGaleria();
                }
            }
        });


    }

    private void recullDeGaleria(){
        //Cream l'Intent amb l'acció ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Establim tipus d'imatges, per tant només s'acceptaran els tipus imagtge
        intent.setType("image/*");
        //Establim uns tipus de format de fotografia per assegurar-nos d'acceptar només aquest tipus de format jpg i png
       /*String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);*/
        // Llançam l'Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {//data.getData return the content URI for the selected Image
               Uri selectedImage = data.getData();
               img.setImageURI(selectedImage);
               bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();

                //aixo es per quan pitgen
                btnAfegir = (Button) findViewById(R.id.afegir);
                btnAfegir.setOnClickListener(new View.OnClickListener(){

                    public void onClick(View view){
                        bd = new DBInterface(getApplicationContext());
                        bd.obre();
                        String title = titleText.getText().toString();
                        if (bd.insereixCategoria(title, bitmap) != -1) {
                            Toast.makeText(getApplicationContext(), "Afegit correctament", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error a l’afegir", Toast.LENGTH_SHORT).show();
                        }

                        MainActivity.tasks.clear();
                        MainActivity.tasks.addAll(bd.obtenirTotesLesTasques());
                        MainActivity.categories.clear();
                        MainActivity.categories.addAll(bd.obtenirTotesLesCategories());
                        bd.tanca();
                        finish();
                    }
                });

            }
        }
    }
}