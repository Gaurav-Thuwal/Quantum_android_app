package com.addy.quantum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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

    // Method to get All data depending on given date or month
    public Cursor getAllData(String date, boolean forMonthOnly){
        // Check if method is called to get data for month or "yesterday", prepare query accordingly
        String query = "";
        if(!forMonthOnly){
            // Prepare query to give data of "yesterday's" date
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = \"" + date + "\"";
        } else {
            // Prepare query to give data of current month
            query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " LIKE \"%/" + date + "\"";
        }
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = null;

        //check whether db is null or not, insert only if not empty
        if (sqLiteDatabase != null){
            cursor = sqLiteDatabase.rawQuery(query,null);
        }
        return cursor;
    }

    public boolean updateExpense(String id, String name, String amount, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " +
                COLUMN_NAME + " = '" + name + "', " +
                COLUMN_AMOUNT + " = '" + amount + "', " +
                COLUMN_DATE + " = '" + date + "' " +
                " WHERE " + COLUMN_ID + " = " + id + ";";

        // If something gets wrong return false, else return true
        try{
            sqLiteDatabase.execSQL(query);
            return true;
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    // Method to delete a row by given id
    public boolean deleteExpense(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        try{
            sqLiteDatabase.execSQL(query);
            return true;
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();
            return false;
        }
    }

    // Method to get total amount from table
    public String getTotalAmount(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") as Total FROM " + TABLE_NAME ;
        if(sqLiteDatabase != null){
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();       // let's move cursor for first value
            String total_amount = cursor.getString((cursor.getColumnIndex("Total")));
            cursor.close();
            return "Rs. " + total_amount;
        }
        return "Rs. 0";
    }

    // Method to get total amount from table for specific date
    public String getTotalAmount(String date){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        // Get values for specific date using where clause
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") as Total FROM " + TABLE_NAME +
                " WHERE " + COLUMN_DATE + " = \"" + date + "\"";
        if(sqLiteDatabase != null){
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();       // let's move cursor for first value
            String total_amount = cursor.getString((cursor.getColumnIndex("Total")));
            cursor.close();
            return "Rs. " + total_amount;
        }
        return "Rs. 0";
    }

    // Method to insert data in local storage from list data from cloud
    public void addCloudExpenses(ArrayList<Expense> cloudExpenses){
        // COde to implement
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        // iterate over list and insert one by one
        for(Expense expense : cloudExpenses){
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ID, expense.get_id());
            contentValues.put(COLUMN_NAME, expense.getExpense_name());
            contentValues.put(COLUMN_AMOUNT, expense.getExpense_amount());
            contentValues.put(COLUMN_DATE, expense.getExpense_date());

            try {
                long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            } catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
