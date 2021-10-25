package com.addy.quantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // UI elements
    private Button login_button, signup_button, logout_button;
    private EditText email_input, password_input;
    private TextView loginAccountText, orText, logged_in_text;
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
        logout_button = findViewById(R.id.logout_button);
        loginAccountText = findViewById(R.id.textView);
        orText = findViewById(R.id.textView2);
        logged_in_text = findViewById(R.id.logged_in_text);
        firebaseAuth = FirebaseAuth.getInstance();

        // Set our custom toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ALso check whether a account is already signed in or not, if it is then make all login items invisible
        if(firebaseAuth.getCurrentUser() != null){
            loginAccountText.setVisibility(View.INVISIBLE);
            orText.setVisibility(View.INVISIBLE);
            login_button.setVisibility(View.INVISIBLE);
            signup_button.setVisibility(View.INVISIBLE);
            email_input.setVisibility(View.INVISIBLE);
            password_input.setVisibility(View.INVISIBLE);
            logged_in_text.setVisibility(View.VISIBLE);
            logout_button.setVisibility(View.VISIBLE);

            // Show email of signed account
            String email_string = firebaseAuth.getCurrentUser().getEmail()+" : Signed in";
            logged_in_text.setText(email_string);
        }

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
                            // Hide all the login items once account is logged-in, make logout button and textview visible
                            loginAccountText.setVisibility(View.INVISIBLE);
                            orText.setVisibility(View.INVISIBLE);
                            login_button.setVisibility(View.INVISIBLE);
                            signup_button.setVisibility(View.INVISIBLE);
                            email_input.setVisibility(View.INVISIBLE);
                            password_input.setVisibility(View.INVISIBLE);
                            logged_in_text.setVisibility(View.VISIBLE);
                            logout_button.setVisibility(View.VISIBLE);
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

        // logout button functionality
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log out the current user on tap of logout button
                String currentUserEmail = firebaseAuth.getCurrentUser().getEmail();
                firebaseAuth.signOut();
                Toast.makeText(LoginActivity.this, currentUserEmail + " signed out", Toast.LENGTH_LONG).show();

                // And make all login items visible and logout items invisible
                loginAccountText.setVisibility(View.VISIBLE);
                orText.setVisibility(View.VISIBLE);
                login_button.setVisibility(View.VISIBLE);
                signup_button.setVisibility(View.VISIBLE);
                email_input.setVisibility(View.VISIBLE);
                password_input.setVisibility(View.VISIBLE);
                logged_in_text.setVisibility(View.INVISIBLE);
                logout_button.setVisibility(View.INVISIBLE);
            }
        });
    }
}