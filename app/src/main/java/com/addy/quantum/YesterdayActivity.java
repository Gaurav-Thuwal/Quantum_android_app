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

public class YesterdayActivity extends AppCompatActivity {

    // UI components
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView totalAmount;

    // Database helper
    private DatabaseHelper databaseHelper;
    private ArrayList<String> expense_id, expense_name, expense_amount, expense_date;
    private static final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yesterday);

        // init var
        recyclerView = findViewById(R.id.recycler_view);
        toolbar = findViewById(R.id.toolbar);
        totalAmount = findViewById(R.id.total_amount);

        // Set our custom toolbar as action bar
        setSupportActionBar(toolbar);

        // DB instance init and Lists (which will hold data come from db)
        databaseHelper = new DatabaseHelper(YesterdayActivity.this);
        expense_id = new ArrayList<>();
        expense_name = new ArrayList<>();
        expense_amount = new ArrayList<>();
        expense_date = new ArrayList<>();
        getDataInArrayLists();
        totalAmount.setText(databaseHelper.getTotalAmount(gePreviousDate()));      // get and set total amount from database

        // show data on recycler view using custom adapter and my_row layout
        CustomAdapter customAdapter = new CustomAdapter(YesterdayActivity.this, this, expense_id, expense_name,
                expense_amount, expense_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(YesterdayActivity.this));  // my_row layout is LinearLayout
    }

    private void getDataInArrayLists(){
        Cursor cursor = databaseHelper.getAllData(gePreviousDate());

        // insert data in Lists until full data read from cursor
        while(cursor.moveToNext()){
            expense_id.add(cursor.getString(0));
            expense_name.add(cursor.getString(1));
            expense_amount.add(cursor.getString(2));
            expense_date.add(cursor.getString(3));
        }
    }

    // method to get previous date in formatted string
    private String gePreviousDate(){
        Date date = new Date(new Date().getTime() - MILLIS_IN_A_DAY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }
}