package com.addy.quantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // UI elements
    private Button login_button, signup_button;
    private EditText email_input, password_input;
    private Toolbar toolbar;

    // Firebase authentication stuff
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init vars
        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.password_input);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);
        toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        // Set our custom toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // login button functionality
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login firebase authenticated account
                firebaseAuth.signInWithEmailAndPassword(email_input.getText().toString(), password_input.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If task is done means account is logged in successfully, so show toasts accordingly
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Account logged in successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // signup button functionality
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open SignUpActivity to create new user
                Intent signUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });
    }
}