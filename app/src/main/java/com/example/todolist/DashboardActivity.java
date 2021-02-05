package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {
public DBHandler dbHandler=new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar bar = findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        RecyclerView view = findViewById(R.id.rv_dashboard);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        view.setLayoutManager(manager);
        FloatingActionButton button = findViewById(R.id.floatbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = new ContextThemeWrapper(DashboardActivity.this, R.style.AppTheme2);
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
                dialog.setTitle("Add ToDo");
                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard,null);// layout object
                final EditText ToDoName = view.findViewById(R.id.Et_text); // you can also get it
                // from view.findViewById() and set view as view above or like this.
                dialog.setView(view);
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!(ToDoName.getText().toString().equals(""))){
                            ToDo todo= new ToDo();
                            todo.name = ToDoName.getText().toString();
                            dbHandler.addToDo(todo);
                            refresh();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }

    public void updateToDo(final ToDo todo){
        Context context = new ContextThemeWrapper(DashboardActivity.this, R.style.AppTheme2);
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
        dialog.setTitle("update to do");
        View view = getLayoutInflater().inflate(R.layout.dialog_dashboard,null);
        final EditText ToDoName = view.findViewById(R.id.Et_text); // you can also get it
        // from view.findViewById() and set view as view above or like this.
        ToDoName.setText(todo.name);
        dialog.setView(view);
        dialog.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!(ToDoName.getText().toString().equals(""))){
                    todo.name = ToDoName.getText().toString();
                    dbHandler.updateToDo(todo);
                    refresh();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }
    public void refresh(){
        RecyclerView view = findViewById(R.id.rv_dashboard);
        DashboardAdapter adapter = new DashboardAdapter(this,dbHandler.getToDo());/* we are creating a new adapter
        every time. we want to update the view. */
        view.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    /*this for when to get the task name when opening the app. when installed first time, we don't have any
    entries but when we added the task and close the app and reopen it. we need to see the task.
     */

}

