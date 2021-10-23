package com.addy.quantum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ThisMonthActivity extends AppCompatActivity {

    // UI components
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView totalAmount;

    // Database helper
    private DatabaseHelper databaseHelper;
    private ArrayList<String> expense_id, expense_name, expense_amount, expense_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_this_month);

        // init vars
        recyclerView = findViewById(R.id.recycler_view);
        totalAmount = findViewById(R.id.total_amount);
        toolbar = findViewById(R.id.toolbar);

        // Set our custom toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // DB instance init and Lists (which will hold data come from db)
        databaseHelper = new DatabaseHelper(this);
        expense_id = new ArrayList<>();
        expense_name = new ArrayList<>();
        expense_amount = new ArrayList<>();
        expense_date = new ArrayList<>();
        getDataInArrayLists();
        totalAmount.setText(databaseHelper.getTotalAmount());      // get and set total amount from database

        // show data on recycler view using custom adapter and my_row layout
        CustomAdapter customAdapter = new CustomAdapter(ThisMonthActivity.this, this, expense_id, expense_name,
                expense_amount, expense_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ThisMonthActivity.this));  // my_row layout is LinearLayout
    }

    // Method to get data from our database and put them into ArrayLists
    private void getDataInArrayLists(){
        // Set forMonthOnly argument as true, because we need data of current month
        Cursor cursor = databaseHelper.getAllData(getCurrentMonthDate(), true);

        // insert data in Lists until full data read from cursor
        while(cursor.moveToNext()){
            expense_id.add(cursor.getString(0));
            expense_name.add(cursor.getString(1));
            expense_amount.add(cursor.getString(2));
            expense_date.add(cursor.getString(3));
        }
    }

    // Method to get current month date in formatted string
    private String getCurrentMonthDate(){
        Date date = new Date(new Date().getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        return simpleDateFormat.format(date);
    }
}