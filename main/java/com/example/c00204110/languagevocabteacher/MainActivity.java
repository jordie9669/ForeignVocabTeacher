package com.example.c00204110.languagevocabteacher;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Tag";
    private static final String FIRE_LOG = "Fire_log";
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private Button addNewLanguage;
    private FirebaseFirestore firebaseFirestore;
    private String currentUserId;
    private Button englishButton;
    private RecyclerView languageRecycler;
    private List<Languages> languageList;
    private LanguagesListAdapter languagesListAdapter;
    public String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        languageList = new ArrayList<>();
        languagesListAdapter = new LanguagesListAdapter(getApplicationContext(), languageList);
        languageRecycler = findViewById(R.id.language_list_view);
        languageRecycler.setHasFixedSize(true);
        languageRecycler.setLayoutManager(new LinearLayoutManager(this));
        languageRecycler.setAdapter(languagesListAdapter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        addNewLanguage = (Button) findViewById(R.id.addNewLanguage);
        mainToolbar = (Toolbar) findViewById(R.id.quiz_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Vocabulary Teacher");

        firebaseFirestore.collection("language").addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {

                    Log.d(FIRE_LOG, "Error: " + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        Languages languages = doc.getDocument().toObject(Languages.class);
                        languageList.add(languages);
                        languagesListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        addNewLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addIntent = new Intent(MainActivity.this, NewLanguageActivity.class);
                startActivity(addIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {

                sendToLogin();
        }
        else {                          //Checks if user has logged in but does not have a username or image
                                        //if they have none they are redirected from MainActivity to SetupActivity to add username.
            currentUserId = mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            if(!task.getResult().exists()){

                                    Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                                    startActivity(setupIntent);
                                    finish();
                            }
                        }
                        else{

                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

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
        Intent mainIntent = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(mainIntent);

    }

    private void myWords() {
        Intent mainIntent = new Intent(MainActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);

    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();

    }

    private void sendToLogin() {
        Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
