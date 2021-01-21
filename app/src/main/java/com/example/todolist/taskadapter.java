package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        //agafam el la posicio
        final Task task = tasks.get(position);
        //agafam el layout per omplir-lo
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_detail, null);
        //instanciam cada element
        final TextView titol = (TextView) view.findViewById(R.id.title);
        final CheckBox complete = (CheckBox) view.findViewById(R.id.complete);

        //omplim dades
        titol.setText(task.getTitle());
        complete.setChecked(task.isComplete());
        if (task.isComplete())
            titol.setPaintFlags(titol.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        Task task = tasks.get(position);
                        String checkTitol = task.getTitle();
                        if (complete.isChecked()){
                            titol.setPaintFlags(titol.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                            titol.setText(checkTitol);
                            task.setComplete(true);

                            tasks.remove(task);
                            tasks.add(task);
                            Toast toast = Toast.makeText(getContext(), "Tasca completada", Toast.LENGTH_SHORT);
                            toast.show();
                            notifyDataSetChanged();

                        } else {
                            if ((titol.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                                titol.setPaintFlags( titol.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                                titol.setText(checkTitol);
                                task.setComplete(false);

                                tasks.remove(task);
                                tasks.add(0, task);
                                Toast toast = Toast.makeText(getContext(), "Tasca desmarcada", Toast.LENGTH_SHORT);
                                toast.show();
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

        return view;
    }

}
