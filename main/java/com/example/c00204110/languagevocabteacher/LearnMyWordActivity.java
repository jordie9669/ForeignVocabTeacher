package com.example.c00204110.languagevocabteacher;

import android.content.Intent;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LearnMyWordActivity extends AppCompatActivity {

    private Uri myWordImageURI = null;
    private static final String WORD_LOG = "Word_log";
    private EditText myWordDescription;
    private boolean isChanged = false;
    private ProgressBar deleteProgress;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageView myWordImage;
    private Button deleteWordButton;
    private TextView myWordTitle;
    private EditText myWordTranslation;
    private TextToSpeech mTTS;
    private Button myWordSoundButton;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_my_word);

        Toolbar deleteWordToolbar = findViewById(R.id.learn_my_word_toolbar);
        setSupportActionBar(deleteWordToolbar);
        getSupportActionBar().setTitle("Learn Word");

        firebaseFirestore = FirebaseFirestore.getInstance();
        myWordSoundButton = findViewById(R.id.my_word_sound_button);
        myWordImage = findViewById(R.id.my_word_image);
        myWordTitle= findViewById(R.id.learn_my_word_title);
        myWordDescription = findViewById(R.id.my_word_desc);
        myWordTranslation = findViewById(R.id.my_word_translation);
        deleteWordButton = findViewById(R.id.deleteWordButton);
        deleteProgress = findViewById(R.id.deleteWordProgress);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        deleteProgress.setVisibility(View.VISIBLE);

        Globals g = Globals.getInstance();
        final String data=g.getData();
        final String mySelectedWord = g.getMySelectedWord();

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);
                    if(data == "German"){
                        result = mTTS.setLanguage(Locale.GERMAN);
                    }
                    if(data == "French"){
                        result = mTTS.setLanguage(Locale.FRENCH);
                    }
                    if(data == "Italian"){
                        result = mTTS.setLanguage(Locale.ITALIAN);
                    }
                    if(data == "Chinese"){
                        result = mTTS.setLanguage(Locale.CHINESE);
                    }
                    if(data == "Spanish"){
                        Locale spanish = new Locale("es", "ES");
                        result = mTTS.setLanguage(spanish);
                    }
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        myWordSoundButton.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        deleteWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteProgress.setVisibility(View.VISIBLE);

                firebaseFirestore.collection("Users").document(user_id).collection("savedWords").document(mySelectedWord).delete().addOnCompleteListener(new OnCompleteListener<Void>(){
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            deleteProgress.setVisibility(View.INVISIBLE);
                            Toast.makeText(LearnMyWordActivity.this, "Word has been deleted", Toast.LENGTH_LONG).show();
                            Intent a = new Intent(LearnMyWordActivity.this, MyWordsActivity.class);
                            startActivity(a);
                            finish();

                        }
                        else{

                            deleteProgress.setVisibility(View.INVISIBLE);
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(LearnMyWordActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();

                        }

                    }
                });

            }
        });

        myWordSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speak();

            }
        });

        firebaseFirestore.collection("Users").document(user_id).collection("savedWords").document(mySelectedWord).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String translation = task.getResult().getString("translation");
                        String description = task.getResult().getString("description");
                        String image = task.getResult().getString("image");

                        if(image != null){
                            myWordImageURI = Uri.parse(image);
                        }

                        myWordTitle.setText(name);
                        myWordTranslation.setText(translation);
                        if(description != null){
                            myWordDescription.setText(description);
                        }

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.ic_launcher_background);

                        Glide.with(LearnMyWordActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(myWordImage);

                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(LearnMyWordActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

                deleteProgress.setVisibility(View.INVISIBLE);
                //setupBtn.setEnabled(true);

            }
        });

    }

    private void speak() {
        String text = myWordTranslation.getText().toString();

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

        Intent mainIntent = new Intent(LearnMyWordActivity.this, SetupActivity.class);
        startActivity(mainIntent);

    }

    private void myWords() {

        Intent mainIntent = new Intent(LearnMyWordActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);

    }
    private void home() {

        Intent mainIntent = new Intent(LearnMyWordActivity.this, MainActivity.class);
        startActivity(mainIntent);

    }

    private void logout() {

        firebaseAuth.signOut();
        sendToLogin();

    }

    private void sendToLogin() {

        Intent mainIntent = new Intent(LearnMyWordActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();

    }

}
