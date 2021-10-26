package com.addy.quantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    // UI stuff
    private Button upload;
    private Toolbar toolbar;

    // Firebase stuff
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Init vars
        upload = findViewById(R.id.upload_button);
        toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        // Get firebase database reference as "expenses/user_uid", because we'll save user expenses there
        databaseReference = FirebaseDatabase.getInstance().getReference("expenses/"+firebaseAuth.getCurrentUser().getUid());

        // Set our custom toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // upload button functionality
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method which have logic to get expense data from local database and push them to cloud
                Toast.makeText(UploadActivity.this, "This may take a while :)", Toast.LENGTH_LONG).show();
                uploadExpense();
            }
        });
    }

    // Method to get data from local database and upload them to firebase database
    private void uploadExpense(){
        // get a cursor to our local database
        DatabaseHelper databaseHelper = new DatabaseHelper(UploadActivity.this);
        Cursor cursor = databaseHelper.getAllData();

        // if moveToNext return false means we've read all the data
        while(cursor.moveToNext()){
            // Create a HashMap which will have the keys as column name and value will be the data from cursor
            Map<String, String> data = new HashMap<>();
            data.put(DatabaseHelper.COLUMN_ID, cursor.getString(0));        // _id column
            data.put(DatabaseHelper.COLUMN_NAME, cursor.getString(1));      // expense_name column
            data.put(DatabaseHelper.COLUMN_AMOUNT, cursor.getString(2));    // expense_amount column
            data.put(DatabaseHelper.COLUMN_DATE, cursor.getString(3));      // expense_date column

            // Create node name as _id value and save the data (as child)
            databaseReference.child(cursor.getString(0)).setValue(data).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // This method gets trigger when data is failed to save in firebase database, show toast accordingly
                    Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}