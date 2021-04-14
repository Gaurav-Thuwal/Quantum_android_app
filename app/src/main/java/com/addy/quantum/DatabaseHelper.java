package com.addy.quantum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Expenses.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "my_expense";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "expense_name";
    public static final String COLUMN_AMOUNT = "expense_amount";
    public static final String COLUMN_DATE = "expense_date";

    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AMOUNT + " INTEGER, " +
                COLUMN_DATE + " TEXT );" ;

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_query = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        db.execSQL(drop_query);
        onCreate(db);
    }

    public void addExpense(String name, int amount , String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // put values using Content Values
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_AMOUNT,amount);
        contentValues.put(COLUMN_DATE,date);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            Toast.makeText(context,"Failed to update !",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Successfully updated",Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        //check whether db is null or not, insert only if not empty
        if (sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }
    public void updateExpense(String id, String name, String amount, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " +
                COLUMN_NAME + " = '" + name + "', " +
                COLUMN_AMOUNT + " = '" + amount + "', " +
                COLUMN_DATE + " = '" + date + "' " +
                " WHERE " + COLUMN_ID + " = " + id + ";";

        sqLiteDatabase.execSQL(query);
    }
}
