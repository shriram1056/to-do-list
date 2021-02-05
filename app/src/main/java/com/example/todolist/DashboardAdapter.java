package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
// DashboardAdapter.ViewHolder - this return type no problem but to make object outside this class
// see head first java inner class.
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    ArrayList<ToDo> list;
   DashboardActivity activity;

    public DashboardAdapter(DashboardActivity activity, ArrayList<ToDo> list){
        this.list= list;
        this.activity=activity;
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new ViewHolder(LayoutInflater.from(activity)
                .inflate(R.layout.rv_child_dashboard,parent,false));
      // inside outer class we can make object without creating outer class object first
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String p = list.get(position).name;
        holder.toDoName.setText(p);
        holder.toDoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,ItemActivity.class);
                intent.putExtra(Const.INTENT_TODO_ID,list.get(position).id);
                intent.putExtra(Const.INTENT_TODO_NAME,list.get(position).name);
                activity.startActivity(intent);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(activity, holder.menu);
                popup.inflate(R.menu.dashboard_child);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.menu_edit:{
                                activity.updateToDo(list.get(position));
                                break;
                            }
                            case R.id.menu_delete:{
                                activity.dbHandler.deleteToDo(list.get(position).id);
                                activity.refresh();
                                break;
                            }
                            case R.id.menu_mark_as_completed:{
                                activity.dbHandler.UpdateToDoItemCompletedStatus(list.get(position).id,true);
                                break;
                            }
                            case R.id.menu_reset:{
                                activity.dbHandler.UpdateToDoItemCompletedStatus(list.get(position).id,false);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView toDoName;
        ImageView menu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoName = itemView.findViewById(R.id.tv_text);
            menu = itemView.findViewById(R.id.iv_menu);
        }
    }
}
