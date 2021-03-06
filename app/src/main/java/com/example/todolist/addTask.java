package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class addTask extends AppCompatActivity {

    private EditText titleText, descriptionText;
    private Button btnAfegir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        final EditText titleText = (EditText) this.findViewById(R.id.editTextTextPersonName);
        final EditText descriptionText = (EditText) this.findViewById(R.id.editTextTextMultiLine);
        final Spinner categoriaSpinner = (Spinner) this.findViewById(R.id.categorySpinner);

        List<String> spinnerArray = new ArrayList<String>();
        for (Category category: MainActivity.categories)
            spinnerArray.add(category.title);

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray);

        categoriaSpinner.setAdapter(spinnerArrayAdapter);

        Button myFab = (Button) this.findViewById(R.id.addButton);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                String title = titleText.getText().toString();
                resultIntent.putExtra("title", title);
                int categoryPosition = categoriaSpinner.getSelectedItemPosition();
                resultIntent.putExtra("categoryPosition", categoryPosition);
                String description = descriptionText.getText().toString();
                resultIntent.putExtra("description", description);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}