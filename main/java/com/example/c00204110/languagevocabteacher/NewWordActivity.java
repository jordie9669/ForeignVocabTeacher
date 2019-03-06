package com.example.c00204110.languagevocabteacher;
import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class NewWordActivity extends AppCompatActivity {
    private EditText newWord;        //initialising fields
    private Button addNewWordButton;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar addWordProgress;
    private TextView addWordTitle;
    private Toolbar newWordToolbar;
    private StorageReference storageReference;
    private DatabaseReference wordDatabaseRef;
    private FirebaseFirestore wordDatabase;
    private FirebaseAuth firebaseAuth;
    private CollectionReference wordCollectionRef;
    private EditText newWordDescription;
    private ImageView wordImage;
    private Uri wordImageURI = null;
    private boolean isChanged = false;
    private Random generator = new Random();
    private EditText wordTranslation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        wordTranslation = findViewById(R.id.new_word_translation);
        wordImage = findViewById(R.id.word_image);
        storageReference = FirebaseStorage.getInstance().getReference();
        wordDatabase = FirebaseFirestore.getInstance();
        wordCollectionRef = wordDatabase.collection("language");
        newWordDescription = findViewById(R.id.new_word_desc);
        newWordToolbar = findViewById(R.id.new_word_toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        addWordProgress = (ProgressBar) findViewById(R.id.addWordProgress);
        newWord = (EditText) findViewById(R.id.new_word);  //setting fields to variables
        addNewWordButton = (Button) findViewById(R.id.addNewWordButton);
        addWordTitle = (TextView) findViewById(R.id.add_new_word_title);
        storageReference = FirebaseStorage.getInstance().getReference();
        setSupportActionBar(newWordToolbar);
        getSupportActionBar().setTitle("Add New Word");
        addNewWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String word = newWord.getText().toString();
                final String description = newWordDescription.getText().toString();
                final String translation = wordTranslation.getText().toString();
                if (!TextUtils.isEmpty(word) && !TextUtils.isEmpty(description)) {
                    addWordProgress.setVisibility(View.VISIBLE);
                    if (isChanged) {
                        int word_id = generator.nextInt(8999999) + 1000000;
                        StorageReference image_path = storageReference.child("word_images").child(word_id + ".jpg");
                        image_path.putFile(wordImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storeFirestore(task, word, description, translation);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(NewWordActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                                    addWordProgress.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    } else {
                        storeFirestore(null, word, description, translation);
                    }
                }
            }

        });
        wordImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(NewWordActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(NewWordActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(NewWordActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
            }

        });
    }
    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String word, String description, String translation) {
        Uri download_uri;
        if(task != null) {
            download_uri = task.getResult().getDownloadUrl();
        } else {
            download_uri = wordImageURI;
        }
        Map<String, String> wordMap = new HashMap<>();
        wordMap.put("name", word);
        wordMap.put("translation", translation);
        if(description != null){
            wordMap.put("description", description);
        }
        if(download_uri != null)
        {
            wordMap.put("image", download_uri.toString());
        }
        Globals g = Globals.getInstance();
        final String data=g.getData();
        final String selectedSector = g.getSelectedSector();
        wordCollectionRef.document(data).collection("Sectors").document(selectedSector).collection("Words").document(word).set(wordMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(NewWordActivity.this, "Your word has been added.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(NewWordActivity.this, ChooseWordActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(NewWordActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                }
                addWordProgress.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(NewWordActivity.this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                wordImageURI = result.getUri();
                wordImage.setImageURI(wordImageURI);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
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
        Intent mainIntent = new Intent(NewWordActivity.this, SetupActivity.class);
        startActivity(mainIntent);
    }
    private void myWords() {
        Intent mainIntent = new Intent(NewWordActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);
    }
    private void home() {
        Intent mainIntent = new Intent(NewWordActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        Intent mainIntent = new Intent(NewWordActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
