package com.example.c00204110.languagevocabteacher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText regEmailText;        //initialising fields
    private EditText regPasswordText;
    private EditText regConfirmPasswordText;
    private Button regSignUpButton;
    private ProgressBar regProgress;
    private Button regLoginButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        regLoginButton = (Button) findViewById(R.id.regLoginButton);
        regEmailText = (EditText) findViewById(R.id.regEmail);  //setting fields to variables
        regPasswordText = (EditText) findViewById(R.id.regPassword);
        regConfirmPasswordText = (EditText) findViewById(R.id.regConfirmPassword);
        regSignUpButton = (Button) findViewById(R.id.regSignUpButton);
        regProgress = (ProgressBar) findViewById(R.id.regProgress);
        regLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(regIntent);
                finish();

            }
        });
        regSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmailText.getText().toString();
                String pass = regPasswordText.getText().toString();
                String confirmPass = regConfirmPasswordText.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirmPass)){
                    if(pass.equals(confirmPass)){
                        regProgress.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                        startActivity(setupIntent);
                                        finish();
                                }
                                else{
                                   String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                    regProgress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "Password and Confirm Password fields do not match", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
