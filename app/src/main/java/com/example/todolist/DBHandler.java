package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static com.example.todolist.Const.*;

public class DBHandler extends SQLiteOpenHelper {
    public static final String DB_NAME = "ToDoList";
    public static final int DB_VERSION = 1;

    public DBHandler(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        upgrade(db,0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgrade(db,oldVersion);
    }

    public void upgrade(SQLiteDatabase db,int oldVersion){
        if(oldVersion<1){
        String CreateTableToDo = "CREATE TABLE " + TABLE_TODO + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_CREATED_AT + " NUMERIC DEFAULT CURRENT_TIMESTAMP,"+ COL_NAME+
                " TEXT)";

/* String x = "CREATE TABLE text(test INTEGER PRIMARY KEY AUTOINCREMENT test NUMERIC DEFAULT CURRENT TIMESTAMP
test TEXT. replace test with appropriate word
 */
            String CreateTableToDoItem = "CREATE TABLE " + TABLE_TODO_ITEM + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                 COL_CREATED_AT+" NUMERIC DEFAULT CURRENT_TIMESTAMP,"+ COL_TODO_ID+
                " INTEGER," + COL_ITEM_NAME + " TEXT,"+ COL_IS_COMPLETED + " INTEGER)";
            // listen up you fucker never ever change this pattern.if you do.then go die
        db.execSQL(CreateTableToDo);
        db.execSQL(CreateTableToDoItem);
        }
    }

    public boolean addToDo(ToDo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME,todo.name);
        long result = db.insert(TABLE_TODO,null,cv);
        db.close();
        return result !=(-1) ;
    }

    public void deleteToDoItem(long itemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM, COL_ID + "=?", new String[]{String.valueOf(itemId)});

    }
    public void updateToDo(ToDo todo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, todo.name);
        db.update(TABLE_TODO,cv,COL_ID+ "=?",new String[]{Integer.toString(todo.id)});
        db.close();
    }

    public ArrayList<ToDo> getToDo()
    {
        ArrayList<ToDo> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO, new String[]{COL_ID,COL_CREATED_AT,COL_NAME},
           null,null, null, null,
            null);
        if(cursor.moveToFirst()){
            do{
                ToDo todo = new ToDo();
                todo.id= cursor.getInt(0);
                todo.name = cursor.getString(2);
                result.add(todo);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean addToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_NAME, item.itemName);
        cv.put(COL_TODO_ID, item.ToDoId);
        cv.put(COL_IS_COMPLETED,item.isCompleted);
        long result = db.insert(TABLE_TODO_ITEM, null, cv);
        db.close();
        return result != -1;
    }


    public ArrayList<ToDoItem> getToDoItems(int todoId) {
        ArrayList<ToDoItem> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO_ITEM, new String[]{COL_ID,COL_CREATED_AT,COL_TODO_ID,COL_ITEM_NAME,
                        COL_IS_COMPLETED}, COL_TODO_ID +"= ?",new String[]{Integer.toString(todoId)},
                null, null, null);
        //here we are specifying which name is associated with which item.by using the default position given by
        //onBindView in dashboard adapter
        if (cursor.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.id = cursor.getInt(0);
                item.ToDoId= cursor.getInt(2);
                item.itemName=cursor.getString(3);
                item.isCompleted=(cursor.getInt(4)==1);
                result.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result ;
    }

    public void deleteToDo(int todoId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM,COL_TODO_ID+"=?",new String[]{Integer.toString(todoId)});
        db.delete(TABLE_TODO,COL_ID+"=?",new String[]{Integer.toString(todoId)});
    }

    public void UpdateToDoItemCompletedStatus(int todoId,boolean isCompleted){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_TODO_ITEM, new String[]{COL_ID,COL_CREATED_AT,COL_TODO_ID,COL_ITEM_NAME,
                        COL_IS_COMPLETED}, COL_TODO_ID +"= ?",new String[]{Integer.toString(todoId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.id = cursor.getInt(0);
                item.ToDoId= cursor.getInt(2);
                item.itemName=cursor.getString(3);
                item.isCompleted=isCompleted;
                updateToDoItem(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
    public void updateToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_NAME, item.itemName);
        cv.put(COL_IS_COMPLETED, item.isCompleted);
        //        cv.put(COL_TODO_ID, item.ToDoId); and  this is not needed
        db.update(TABLE_TODO_ITEM, cv, COL_ID + "=?", new String[]{Integer.toString(item.id)});
    }// update item is


}
