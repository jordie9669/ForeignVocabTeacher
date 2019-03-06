package com.example.c00204110.languagevocabteacher;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.api.translate.Language;
//import com.google.api.translate.Translate;
import android.speech.tts.TextToSpeech;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class LearnWordActivity extends AppCompatActivity {
    private Uri wordImageURI = null;
    private static final String WORD_LOG = "Word_log";
    private EditText wordDescription;
    private boolean isChanged = false;
    private ProgressBar saveProgress;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageView wordImage;
    private Button saveWordButton;
    private TextView wordTitle;
    private EditText wordTranslation;
    private TextToSpeech mTTS;
    private Button soundButton;
    private String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);
        Toolbar saveWordToolbar = findViewById(R.id.learn_word_toolbar);
        setSupportActionBar(saveWordToolbar);
        getSupportActionBar().setTitle("Learn Word");
        firebaseFirestore = FirebaseFirestore.getInstance();
        soundButton = findViewById(R.id.sound_button);
        wordImage = findViewById(R.id.word_image);
        wordTitle= findViewById(R.id.learn_word_title);
        wordDescription = findViewById(R.id.word_desc);
        wordTranslation = findViewById(R.id.word_translation);
        saveWordButton = findViewById(R.id.deleteWordButton);
        saveProgress = findViewById(R.id.saveWordProgress);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        saveProgress.setVisibility(View.VISIBLE);
        Globals g = Globals.getInstance();
        final String data=g.getData();
        final String selectedSector = g.getSelectedSector();
        final String selectedWord = g.getSelectedWord();
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        soundButton.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        firebaseFirestore.collection("language").document(data).collection("Sectors").document(selectedSector).collection("Words").document(selectedWord).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        String translation = task.getResult().getString("translation");
                        String description = task.getResult().getString("description");
                        String image = task.getResult().getString("image");
                        if (image != null) {
                            wordImageURI = Uri.parse(image);
                        }
                        wordTitle.setText(name);
                        wordTranslation.setText(translation);
                        if (description != null) {
                            wordDescription.setText(description);
                        }
                        Globals g = Globals.getInstance();
                        final String data = g.getData();
                        g.setTranslation(translation);
                        g.setDescription(description);
                        g.setImageUri(image);
                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.ic_launcher_background);
                        Glide.with(LearnWordActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(wordImage);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(LearnWordActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                }
                saveProgress.setVisibility(View.INVISIBLE);
            }
        });
        saveWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProgress.setVisibility(View.VISIBLE);
                Globals g = Globals.getInstance();
                final String data = g.getData();
                final String description = g.getDescription();
                final String translation = g.getTranslation();
                final String imageUri = g.getImageUri();
                Map<String, Object> savedWordsMap = new HashMap<>();
                savedWordsMap.put("name", selectedWord);
                savedWordsMap.put("translation", translation);
                savedWordsMap.put("description", description);
                savedWordsMap.put("image", imageUri);
                firebaseFirestore.collection("Users").document(user_id).collection("savedWords").document(selectedWord).set(savedWordsMap).addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            saveProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(LearnWordActivity.this, "Word has been saved", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LearnWordActivity.this, ChooseWordActivity.class);
                            startActivity(intent);

                        }
                        else{
                            saveProgress.setVisibility(View.INVISIBLE);
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LearnWordActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }
    private void speak() {
        String text = wordTranslation.getText().toString();
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
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
        Intent mainIntent = new Intent(LearnWordActivity.this, SetupActivity.class);
        startActivity(mainIntent);
    }
    private void myWords() {
        Intent mainIntent = new Intent(LearnWordActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);
    }

    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        Intent mainIntent = new Intent(LearnWordActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void home() {
        Intent mainIntent = new Intent(LearnWordActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}
