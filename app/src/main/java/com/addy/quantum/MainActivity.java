package com.addy.quantum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_CODE = 1;
    private FloatingActionButton add_button;
    private RecyclerView recyclerView;
    private NavigationView navigation_view;
    private Toolbar toolbar;
    private DrawerLayout drawer_main;
    private ActionBarDrawerToggle toggle;
    private TextView total_amount;

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
        toolbar = findViewById(R.id.toolbar);
        navigation_view = findViewById(R.id.navigation_view);
        drawer_main = findViewById(R.id.drawer_main);
        total_amount = findViewById(R.id.total_amount);

        // Set our custom toolbar as action bar
        setSupportActionBar(toolbar);

        // Make toggle button to respond opening and closing of Drawer
        toggle = new ActionBarDrawerToggle(this, drawer_main, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer_main.addDrawerListener(toggle);      // Set drawer listener to listen toggle
        toggle.syncState();     // Animate icon for navigation

        // Click listener for navigation items and set "Today" options as checked already
        navigation_view.setCheckedItem(R.id.menu_today);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.menu_yesterday:
                        Intent yesterday = new Intent(MainActivity.this, YesterdayActivity.class);
                        startActivityForResult(yesterday, ACTIVITY_REQUEST_CODE);
                        Toast.makeText(MainActivity.this, "Yesterday", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_this_month:
                        Intent thisMonth = new Intent(MainActivity.this, ThisMonthActivity.class);
                        startActivityForResult(thisMonth, ACTIVITY_REQUEST_CODE);
                        Toast.makeText(MainActivity.this, "This month", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_today:
                        Toast.makeText(MainActivity.this, "Today", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawer_main.closeDrawer(GravityCompat.START);
                return true;
            }
        });

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
        total_amount.setText(databaseHelper.getTotalAmount());      // get and set total amount from database

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
            // Start same activity using intents then finish them. Better than recreate
            Intent refresh_intent = new Intent(this, MainActivity.class);
            startActivity(refresh_intent);
            finish();
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