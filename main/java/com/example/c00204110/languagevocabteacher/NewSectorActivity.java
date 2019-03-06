package com.example.c00204110.languagevocabteacher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class NewSectorActivity extends AppCompatActivity {
    private EditText newSector;        //initialising fields
    private Button addNewSectorButton;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar addSectorProgress;
    private TextView addSectorTitle;
    private Toolbar newSectorToolbar;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private DatabaseReference sectorDatabaseRef;
    private FirebaseFirestore sectorDatabase;
    private CollectionReference sectorCollectionRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sector);
        sectorDatabase = FirebaseFirestore.getInstance();
        sectorCollectionRef  = sectorDatabase.collection("language");
        newSectorToolbar = findViewById(R.id.new_sector_toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        addSectorProgress = (ProgressBar) findViewById(R.id.addSectorProgress);
        newSector = (EditText) findViewById(R.id.new_sector);  //setting fields to variables
        addNewSectorButton = (Button) findViewById(R.id.addNewSectorButton);
        addSectorTitle = (TextView) findViewById(R.id.add_new_sector_title);
        storageReference = FirebaseStorage.getInstance().getReference();
        setSupportActionBar(newSectorToolbar);
        getSupportActionBar().setTitle("Add New Sector");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //back button
        addNewSectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sector = newSector.getText().toString();
                if(!TextUtils.isEmpty(sector)){
                    addSectorProgress.setVisibility(View.VISIBLE);
                    Map<String, Object> sectorMap = new HashMap<>();
                    sectorMap.put("name", sector);
                    Globals g = Globals.getInstance();
                    final String data=g.getData();
                    sectorCollectionRef.document(data).collection("Sectors").document(sector).set(sectorMap).addOnCompleteListener(new OnCompleteListener<Void>(){
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(NewSectorActivity.this, "Sector has been added!", Toast.LENGTH_LONG).show();
                                Intent addSector = new Intent(NewSectorActivity.this, ChooseSectorActivity.class);
                                startActivity(addSector);
                                finish();
                            }
                            else{
                                addSectorProgress.setVisibility(View.INVISIBLE);
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(NewSectorActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
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
        Intent mainIntent = new Intent(NewSectorActivity.this, SetupActivity.class);
        startActivity(mainIntent);
    }
    private void myWords() {
        Intent mainIntent = new Intent(NewSectorActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);
    }
    private void home() {
        Intent mainIntent = new Intent(NewSectorActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        Intent mainIntent = new Intent(NewSectorActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
