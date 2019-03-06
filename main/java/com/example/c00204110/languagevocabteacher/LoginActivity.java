package com.example.c00204110.languagevocabteacher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailText;        //initialising fields
    private EditText loginPasswordText;
    private Button loginButton;
    private Button loginSignUpButton;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginEmailText = (EditText) findViewById(R.id.loginEmail);  //setting fields to variables
        loginPasswordText = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        loginSignUpButton = (Button) findViewById(R.id.loginSignUpButton);
        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
                finish();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String loginEmail = loginEmailText.getText().toString();
                        String loginPassword = loginPasswordText.getText().toString();
                        if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPassword)) {
                            loginProgress.setVisibility(View.VISIBLE);
                            mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        sendToMain();

                                    } else {
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                    }
                                    loginProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            sendToMain();

        }               //redirects user to main if they're already logged in
    }
    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
