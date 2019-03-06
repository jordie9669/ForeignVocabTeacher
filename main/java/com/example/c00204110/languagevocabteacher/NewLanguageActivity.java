package com.example.c00204110.languagevocabteacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
public class NewLanguageActivity extends AppCompatActivity {
    private EditText newLanguage;        //initialising fields
    private Button addNewLanguageButton;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar addLanguageProgress;
    private TextView addLanguageTitle;
    private Toolbar newLanguageToolbar;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private DatabaseReference languageDatabaseRef;
    private FirebaseFirestore languageDatabase;
    private CollectionReference languageCollectionRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_language);
        languageDatabase = FirebaseFirestore.getInstance();
        languageCollectionRef  = languageDatabase.collection("language");
        newLanguageToolbar = findViewById(R.id.quiz_toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        addLanguageProgress = (ProgressBar) findViewById(R.id.addLanguageProgress);
        newLanguage = (EditText) findViewById(R.id.new_language);  //setting fields to variables
        addNewLanguageButton = (Button) findViewById(R.id.addNewLanguageButton);
        addLanguageTitle = (TextView) findViewById(R.id.add_new_language_title);
        storageReference = FirebaseStorage.getInstance().getReference();
        setSupportActionBar(newLanguageToolbar);
        getSupportActionBar().setTitle("Add New Language");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //back button
        addNewLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = newLanguage.getText().toString();
                if(!TextUtils.isEmpty(language)) {
                    addLanguageProgress.setVisibility(View.VISIBLE);
                    Map<String, Object> languageMap = new HashMap<>();
                    languageMap.put("name", language);
                    languageCollectionRef.document(language).set(languageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewLanguageActivity.this, "Language has been added!", Toast.LENGTH_LONG).show();
                                //language2.setVisibility(View.VISIBLE);
                                Intent addLanguage = new Intent(NewLanguageActivity.this, MainActivity.class);
                                startActivity(addLanguage);
                                finish();
                            } else {
                                addLanguageProgress.setVisibility(View.INVISIBLE);
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(NewLanguageActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home_button:
                home();
                return true;
            case R.id.action_myWords_button:
                myWords();
                return true;
            case R.id.action_logout_button:
                logout();
                return true;
            case R.id.action_settings_button:
                sendToSettings();
                return true;
            default:
                return false;
        }
    }
    private void sendToSettings() {
        Intent mainIntent = new Intent(NewLanguageActivity.this, SetupActivity.class);
        startActivity(mainIntent);

    }
    private void myWords() {
        Intent mainIntent = new Intent(NewLanguageActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);
    }
    private void home() {
        Intent mainIntent = new Intent(NewLanguageActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        Intent mainIntent = new Intent(NewLanguageActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
