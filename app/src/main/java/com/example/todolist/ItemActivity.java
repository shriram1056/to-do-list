package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.todolist.Const.INTENT_TODO_ID;
import static com.example.todolist.Const.INTENT_TODO_NAME;

public class ItemActivity extends AppCompatActivity {
private DBHandler dbHandler;

int todoId;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(INTENT_TODO_NAME));
        FloatingActionButton fab = findViewById(R.id.item_floatbutton);
        RecyclerView view = findViewById(R.id.rv_item);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        view.setLayoutManager(manager);
        todoId= getIntent().getIntExtra(INTENT_TODO_ID,-1);
        dbHandler = new DBHandler(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = new ContextThemeWrapper(ItemActivity.this, R.style.AppTheme2);
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);

                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard,null);
                final EditText ToDoName = view.findViewById(R.id.Et_text);
                // the problem with new edittext is that we can't customize it.like padding and text appearence
                dialog.setView(view);
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(!(ToDoName.getText().toString().equals(""))){
                            ToDoItem item = new ToDoItem();
                            item.itemName= ToDoName.getText().toString();
                            item.ToDoId=todoId;
                            item.isCompleted=false;
                            dbHandler.addToDoItem(item);
                            refreshList();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        if(menu.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(menu);
        }
    }

    private void refreshList(){
        RecyclerView view = findViewById(R.id.rv_item);
        ItemAdapter adapter = new ItemAdapter(this,dbHandler.getToDoItems(todoId));
        view.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

   public void updateItem(final ToDoItem item){
       Context context = new ContextThemeWrapper(ItemActivity.this, R.style.AppTheme2);
       MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
       dialog.setTitle("Update to do item");
       View view = getLayoutInflater().inflate(R.layout.dialog_dashboard,null);
       final EditText ToDoName = view.findViewById(R.id.Et_text); // you can also get it
       // from view.findViewById() and set view as view above or like this.
       ToDoName.setText(item.itemName);
       dialog.setView(view);
       dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

               if(!(ToDoName.getText().toString().equals(""))){
                   item.itemName= ToDoName.getText().toString();
                   item.isCompleted=false;
                   //   item.ToDoId=todoId;
                   dbHandler.updateToDoItem(item);
                   refreshList();
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
    public static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        ArrayList<ToDoItem> list;
        ItemActivity activity;

        public ItemAdapter(ItemActivity activity,ArrayList<ToDoItem> list){
            this.list= list;
            this.activity = activity;
        }

        @NonNull
        @Override
        public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(activity)
                    .inflate(R.layout.rv_child_item, parent, false));
            // inside outer class we can make object without creating outer class object first
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.itemName.setText(list.get(position).itemName);
            holder.itemName.setChecked(list.get(position).isCompleted);
            holder.itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).isCompleted= !list.get(position).isCompleted;
                    activity.dbHandler.updateToDoItem(list.get(position));
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = new ContextThemeWrapper(activity, R.style.AppTheme2);
                    MaterialAlertDialogBuilder dialog=new MaterialAlertDialogBuilder(context);
                    dialog.setTitle("Are you sure");
                    dialog.setMessage("do you want to delete the item?");
                    dialog.setPositiveButton("continue",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.dbHandler.deleteToDoItem(list.get(position).id);
                            activity.refreshList();
                        }
                    });
                    dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                }
            }); // we are not using any android resources.so it is not a problem
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.updateItem(list.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            MaterialCheckBox itemName;
            ImageView delete;
            ImageView edit;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemName = itemView.findViewById(R.id.cb_item);
                delete = itemView.findViewById(R.id.iv_delete);
                edit=itemView.findViewById(R.id.iv_edit);
            }
        }
    }

}