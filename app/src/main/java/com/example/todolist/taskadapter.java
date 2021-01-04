package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class taskadapter extends ArrayAdapter {

    private Context context;
    private List<Task> tasks;

    public taskadapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context,resource,objects);
        this.context = context;
        this.tasks = objects;
    }

    /*Per col·locar cada un dels elements al layout*/
    public View getView(int position, View convertView, ViewGroup parent) {
        //agafam el la posicio
        Task task = tasks.get(position);
        //agafam el layout per omplir-lo
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_detail, null);
        //instanciam cada element
        TextView titol = (TextView) view.findViewById(R.id.title);
        CheckBox complete = (CheckBox) view.findViewById(R.id.complete);

        //omplim dades
        titol.setText(task.getTitle());
        complete.setChecked(task.isComplete());

        return view;
    }
}
