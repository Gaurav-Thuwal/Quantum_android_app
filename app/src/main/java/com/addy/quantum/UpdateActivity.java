package com.addy.quantum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class UpdateActivity extends AppCompatActivity {

    private EditText expense_name_input, expense_amount_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // init vars
        expense_name_input = findViewById(R.id.expense_name_input);
        expense_amount_input = findViewById(R.id.expense_amount_input);
    }
}