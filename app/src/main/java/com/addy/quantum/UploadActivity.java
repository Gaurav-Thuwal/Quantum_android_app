package com.addy.quantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    // UI stuff
    private Button upload;
    private Toolbar toolbar;
    private TextView lastUpdated;

    // other stuff
    private ArrayList<Expense> cloudExpenses; // to store data from cloud

    // Firebase stuff
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    boolean uploadSuccessful = true;  // flag to check whether upload is successful or not, set as true by default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Init vars
        upload = findViewById(R.id.upload_button);
        toolbar = findViewById(R.id.toolbar);
        lastUpdated = findViewById(R.id.last_updated);
        firebaseAuth = FirebaseAuth.getInstance();
        cloudExpenses = new ArrayList<>();

        // Get firebase database reference as "expenses/user_uid", because we'll save user expenses there
        databaseReference = FirebaseDatabase.getInstance().getReference("expenses/"+firebaseAuth.getCurrentUser().getUid());

        // Set our custom toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check for latest info about last updated from database
        getLastUpdated();

        // upload button functionality
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the method which have logic to get expense data from local database and push them to cloud
                Toast.makeText(UploadActivity.this, "This may take a while :)", Toast.LENGTH_LONG).show();
                uploadExpense();

                // cal an method to get data from cloud to local
                insertInto();
            }
        });
    }

    // Method to take care data insertion
    private void insertInto(){
        // call database to get value from
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cloudExpenses.clear(); // clear any previous data in list
                // get the expenses value from
                for (DataSnapshot snap: snapshot.getChildren()){
                    // insert data in cloudMessages list, here Expense is our custom model class for ease
                    cloudExpenses.add(snap.getValue(Expense.class));
                }

                // Once all data is fetched in list, its time to insert them into our database
                insertInLocal(cloudExpenses); // Method to insert data in local database
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

    // Method to insert the cloud fetched data into our local data
    private void insertInLocal(ArrayList cloudExpenses){
        // Code to implement
        DatabaseHelper helper = new DatabaseHelper(UploadActivity.this);
        helper.addCloudExpenses(cloudExpenses);
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
            databaseReference.child(cursor.getString(0)).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    setLatestUpdated();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // This method gets trigger when data is failed to save in firebase database, show toast accordingly
                    Toast.makeText(UploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

                    // update wasSuccessful to false, to show that upload was not successful
                    uploadSuccessful = false;
                }
            });
        }

        // Show toast according to upload status
        if(uploadSuccessful){
            Toast.makeText(UploadActivity.this, "Successfully saved data to cloud", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(UploadActivity.this, "Failed to save the data to cloud", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get last updated status of firebase database and update last_updated textView
    private void getLastUpdated(){
        FirebaseDatabase.getInstance().getReference("users/"+firebaseAuth.getCurrentUser().getUid())
                .child("last_updated").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String data = snapshot.getValue(String.class);
                if(data != null){
                    // get the datetime from snapshot and set into the lastUpdated TextView
                    lastUpdated.setText(getFormattedTime(data));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // Method to set last_updated datetime in database
    private void setLatestUpdated(){
        String currentTime = String.valueOf(System.currentTimeMillis());
        FirebaseDatabase.getInstance().getReference("users/"+firebaseAuth.getCurrentUser().getUid())
                .child("last_updated").setValue(currentTime).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()) Toast.makeText(UploadActivity.this, "Failed to update latest time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to return formatted datetime string
    private String getFormattedTime(String dateTime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a");
        return String.valueOf(dateFormat.format(new Date(Long.parseLong(dateTime))));
    }
}