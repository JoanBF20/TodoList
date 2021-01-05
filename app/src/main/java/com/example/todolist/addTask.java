package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class addTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        final EditText titleText = (EditText) this.findViewById(R.id.editTextTextPersonName);
        final EditText descriptionText = (EditText) this.findViewById(R.id.editTextTextMultiLine);

        Button myFab = (Button) this.findViewById(R.id.addButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                String title = titleText.getText().toString();
                resultIntent.putExtra("title", title);
                String description = descriptionText.getText().toString();
                resultIntent.putExtra("description", description);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}