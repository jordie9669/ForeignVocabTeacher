package com.example.c00204110.languagevocabteacher;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class QuizActivity extends AppCompatActivity {
    private Button finishButton;
    private TextView quizWord;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView translationsRecycler;
    private List<Translations> translationsList;
    private TranslationsListAdapter translationsListAdapter;
    private Toolbar quizToolbar;
    private Button translations_button;
    private DatabaseReference databaseRef;
    private CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        quizToolbar = findViewById(R.id.quiz_toolbar);
        quizWord = findViewById(R.id.quiz_word);
        translationsRecycler = findViewById(R.id.quiz_translations);
        finishButton = findViewById(R.id.finish_button);
        translationsList = new ArrayList<>();
        translationsListAdapter = new TranslationsListAdapter(getApplicationContext(), translationsList);
        firebaseAuth = FirebaseAuth.getInstance();
        translationsList = new ArrayList<>();
        translationsRecycler.setHasFixedSize(true);
        translationsRecycler.setLayoutManager(new LinearLayoutManager(this));
        translationsRecycler.setAdapter(translationsListAdapter);
        translations_button = findViewById(R.id.word_button);
        firebaseFirestore = FirebaseFirestore.getInstance();
        setSupportActionBar(quizToolbar);
        getSupportActionBar().setTitle("Quiz");
        Globals g = Globals.getInstance();
        String data = g.getData();
        String selectedSector = g.getSelectedSector();
        firebaseFirestore = FirebaseFirestore.getInstance();//.getReference("language");
        CollectionReference collectionReference = firebaseFirestore.collection("language/" + data + "/" + selectedSector + "/" + "Words/Cable");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Translations> translationsList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Translations translations = document.toObject(Translations.class);
                        translationsList.add(translations);
                    }
                    int translationsCount = translationsList.size();
                    int randomNumber = new Random().nextInt(translationsCount);
                    List<Translations> randomTranslationsList = new ArrayList<>();
                    for (int i = 1; i < 2; i++) {
                        randomTranslationsList.add(translationsList.get(randomNumber));
                    }
                } else {
                    Toast.makeText(QuizActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class TranslationsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView translations_button;
        public TranslationsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            translations_button = mView.findViewById(R.id.translations_button);
        }
        public void setDetails(String buttonText){
            TextView word_button = mView.findViewById(R.id.translations_button);
            translations_button.setText(buttonText);
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
        Intent mainIntent = new Intent(QuizActivity.this, SetupActivity.class);
        startActivity(mainIntent);
    }
    private void myWords() {
        Intent mainIntent = new Intent(QuizActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);
    }
    private void home() {
        Intent mainIntent = new Intent(QuizActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        Intent mainIntent = new Intent(QuizActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
