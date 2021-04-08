package com.addy.quantum;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_CODE = 1;
    private FloatingActionButton add_button;
    private RecyclerView recyclerView;

    // DB variable stuff
    private DatabaseHelper databaseHelper;
    private ArrayList<String> expense_id, expense_name, expense_amount, expense_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize variables
        add_button = findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recycler_view);

        // Click listener for floating button
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,ACTIVITY_REQUEST_CODE);
            }
        });

        // DB instance init and Lists (which will hold data come from db)
        databaseHelper = new DatabaseHelper(MainActivity.this);
        expense_id = new ArrayList<>();
        expense_name = new ArrayList<>();
        expense_amount = new ArrayList<>();
        expense_date = new ArrayList<>();
        getDataInArrayLists();

        // show data on recycler view using custom adapter and my_row layout
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, expense_id, expense_name,
                expense_amount, expense_date);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));  // my_row layout is LinearLayout
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            recreate();
        }
    }

    public void getDataInArrayLists(){
        Cursor cursor = databaseHelper.getAllData();

        // insert data in Lists until full data read from cursor
        while(cursor.moveToNext()){
            expense_id.add(cursor.getString(0));
            expense_name.add(cursor.getString(1));
            expense_amount.add(cursor.getString(2));
            expense_date.add(cursor.getString(3));
        }
    }
}