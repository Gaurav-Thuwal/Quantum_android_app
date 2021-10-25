package com.addy.quantum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // UI stuff
    private Button createAccount;
    private EditText emailInput, passwordInput, confirmPasswordInput;
    private Toolbar toolbar;

    // Firebase Authentication stuff
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Init vars
        createAccount = findViewById(R.id.create_account_button);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        // Set our custom toolbar as action bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // create account button functionality
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();

                // Check if both password fields are same or not
                if(password.equals(confirmPassword)){
                    // Create firebase authentication account
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If task is successful means account created successfully, show toasts accordingly
                            if(task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Password fields must be same", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}