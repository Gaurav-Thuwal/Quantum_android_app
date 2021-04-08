package com.addy.quantum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton add_button;

    // DB variable stuff
    private DatabaseHelper databaseHelper;
    private ArrayList<String> expense_id, expense_name, expense_amount, expense_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize variables
        add_button = findViewById(R.id.add_button);

        // Click listener for floating button
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        // DB instance init and Lists (which will hold data come from db)
        databaseHelper = new DatabaseHelper(MainActivity.this);
        expense_id = new ArrayList<>();
        expense_name = new ArrayList<>();
        expense_amount = new ArrayList<>();
        expense_date = new ArrayList<>();
    }
}