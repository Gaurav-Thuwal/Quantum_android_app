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
        databaseReference = FirebaseDatabase.getInstance().getReference("expenses/"+firebaseAuth.getCurrentUser().getUid());

        // Set our custom toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // upload button functionality
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UploadActivity.this, "Upload button", Toast.LENGTH_SHORT).show();
                uploadExpense();
            }
        });
    }
    private void uploadExpense(){
        DatabaseHelper databaseHelper = new DatabaseHelper(UploadActivity.this);
        // get a cursor to all the data from db helper
        Cursor cursor = databaseHelper.getAllData();

        while(cursor.moveToNext()){
            Map<String, String> data = new HashMap<>();
            data.put(DatabaseHelper.COLUMN_ID, cursor.getString(0));
            data.put(DatabaseHelper.COLUMN_NAME, cursor.getString(1));
            data.put(DatabaseHelper.COLUMN_AMOUNT, cursor.getString(2));
            data.put(DatabaseHelper.COLUMN_DATE, cursor.getString(3));

            // firebase real time database
            databaseReference.child(cursor.getString(0)).setValue(data).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}