package com.example.c00204110.languagevocabteacher;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
public class ChooseWordActivity extends AppCompatActivity {
    private Toolbar chooseWordToolbar;
    private Button word_button;
    private final String FIRE_LOG = "Fire_log";
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView wordRecycler;
    private List<Words> wordList;
    private WordListAdapter wordListAdapter;
    private Button addWord;
    private ImageButton searchButton;
    private EditText searchText;
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private Button quizButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_word);
        firebaseAuth = FirebaseAuth.getInstance();
        searchButton = findViewById(R.id.search_button);
        wordList = new ArrayList<>();
        wordListAdapter = new WordListAdapter(getApplicationContext(), wordList);
        wordRecycler = findViewById(R.id.word_list_view);
        wordRecycler.setHasFixedSize(true);
        wordRecycler.setLayoutManager(new LinearLayoutManager(this));
        wordRecycler.setAdapter(wordListAdapter);
        word_button = findViewById(R.id.word_button);
        addWord = findViewById(R.id.addNewWord);
        firebaseFirestore = FirebaseFirestore.getInstance();
        chooseWordToolbar = findViewById(R.id.new_word_toolbar);
        quizButton = findViewById(R.id.quizButton);
        setSupportActionBar(chooseWordToolbar);
        getSupportActionBar().setTitle("Select word");
        Globals g = Globals.getInstance();
        String data=g.getData();
        String selectedSector = g.getSelectedSector();
        databaseRef = FirebaseDatabase.getInstance().getReference("language");
        firebaseFirestore.collection("language").document(data).collection("Sectors").document(selectedSector).collection("Words").addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(ChooseWordActivity.this, "No data has been added yet!", Toast.LENGTH_LONG).show();
                }
                for(DocumentChange sec : documentSnapshots.getDocumentChanges()){
                    if(sec.getType() == DocumentChange.Type.ADDED){
                        Words words = sec.getDocument().toObject(Words.class);
                        wordList.add(words);
                        wordListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        addWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newword = new Intent(ChooseWordActivity.this, NewWordActivity.class);
                startActivity(newword);
            }
        });
        /*quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToQuiz = new Intent (ChooseWordActivity.this, QuizActivity.class);
                startActivity(goToQuiz);

            }
        });*/
    }
    public class WordViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView word_button;
        public ImageButton searchButton;
        public WordViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            word_button = mView.findViewById(R.id.word_button);
            searchButton = mView.findViewById(R.id.search_button);
        }
        public void setDetails(String buttonText){
            TextView word_button = mView.findViewById(R.id.word_button);
            word_button.setText(buttonText);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        Intent mainIntent = new Intent(ChooseWordActivity.this, SetupActivity.class);
        startActivity(mainIntent);
    }
    private void myWords() {
        Intent mainIntent = new Intent(ChooseWordActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);
    }
    private void home() {
        Intent mainIntent = new Intent(ChooseWordActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        Intent mainIntent = new Intent(ChooseWordActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }

}

