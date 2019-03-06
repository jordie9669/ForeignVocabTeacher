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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyWordsActivity extends AppCompatActivity {


    private Toolbar myWordsToolbar;
    private Button my_word_button;
    private final String FIRE_LOG = "Fire_log";
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView wordRecycler;
    private List<MyWords> myWordsList;
    private MyWordsListAdapter myWordListAdapter;
    private ImageButton searchButton;
    private EditText searchText;
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_words);

        searchButton = findViewById(R.id.search_button);
        myWordsList = new ArrayList<>();
        myWordListAdapter = new MyWordsListAdapter(getApplicationContext(), myWordsList);
        wordRecycler = findViewById(R.id.my_words_list_view);
        wordRecycler.setHasFixedSize(true);
        wordRecycler.setLayoutManager(new LinearLayoutManager(this));
        wordRecycler.setAdapter(myWordListAdapter);
        my_word_button = findViewById(R.id.my_word_button);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        myWordsToolbar = findViewById(R.id.my_words_toolbar);

        setSupportActionBar(myWordsToolbar);
        getSupportActionBar().setTitle("My words");

        Globals g = Globals.getInstance();
        String data=g.getData();
        String mySelectedWord = g.getMySelectedWord();

        databaseRef = FirebaseDatabase.getInstance().getReference("language");

        firebaseFirestore.collection("Users").document(user_id).collection("savedWords").addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(e != null){

                    Toast.makeText(MyWordsActivity.this, "No data has been added yet!", Toast.LENGTH_LONG).show();

                }

                for(DocumentChange a : documentSnapshots.getDocumentChanges()){

                    if(a.getType() == DocumentChange.Type.ADDED){

                        MyWords myWords = a.getDocument().toObject(MyWords.class);
                        myWordsList.add(myWords);

                        myWordListAdapter.notifyDataSetChanged();

                    }

                }

            }
        });


    }


    public class WordViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView my_word_button;
        public ImageButton searchButton;

        public WordViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            my_word_button = mView.findViewById(R.id.my_word_button);
            searchButton = mView.findViewById(R.id.search_button);

        }

        public void setDetails(String myButtonText){

            TextView my_word_button = mView.findViewById(R.id.my_word_button);
            my_word_button.setText(myButtonText);
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

        Intent mainIntent = new Intent(MyWordsActivity.this, SetupActivity.class);
        startActivity(mainIntent);

    }

    private void myWords() {

        Intent mainIntent = new Intent(MyWordsActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);

    }
    private void home() {

        Intent mainIntent = new Intent(MyWordsActivity.this, MainActivity.class);
        startActivity(mainIntent);

    }

    private void logout() {

        firebaseAuth.signOut();
        sendToLogin();

    }

    private void sendToLogin() {

        Intent mainIntent = new Intent(MyWordsActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();

    }

}


