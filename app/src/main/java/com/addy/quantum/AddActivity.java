package com.addy.quantum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private EditText expense_name_input, expense_amount_input ;
    private Button add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //init vars
        expense_name_input = findViewById(R.id.expense_name_input);
        expense_amount_input = findViewById(R.id.expense_amount_input);
        add_button = findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add data into table on click of button
                DatabaseHelper databaseHelper = new DatabaseHelper(AddActivity.this);
                String name = expense_name_input.getText().toString();
                int amount = Integer.parseInt(expense_amount_input.getText().toString());
                String date = getDate();

                databaseHelper.addExpense(name,amount,date);
            }
        });

    }

    public String getDate(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }
}