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
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ChooseSectorActivity extends AppCompatActivity {
    private Toolbar chooseSectorToolbar;
    private Button sector_button;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView sectorRecycler;
    private List<Sectors> sectorList;
    private SectorListAdapter sectorListAdapter;
    private Button addSector;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sector);
        sectorList = new ArrayList<>();
        sectorListAdapter = new SectorListAdapter(getApplicationContext(), sectorList);
        firebaseAuth = FirebaseAuth.getInstance();
        sectorRecycler = findViewById(R.id.sector_list_view);
        sectorRecycler.setHasFixedSize(true);
        sectorRecycler.setLayoutManager(new LinearLayoutManager(this));
        sectorRecycler.setAdapter(sectorListAdapter);
        sector_button = findViewById(R.id.sector_button);
        addSector = findViewById(R.id.addNewSector);
        firebaseFirestore = FirebaseFirestore.getInstance();
        chooseSectorToolbar = findViewById(R.id.new_sector_toolbar);
        setSupportActionBar(chooseSectorToolbar);
        getSupportActionBar().setTitle("Choose Sector");
        Globals g = Globals.getInstance();
        String data=g.getData();

        firebaseFirestore.collection("language").document(data).collection("Sectors").addSnapshotListener(new EventListener<QuerySnapshot>() {


                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if(e != null){
                        Toast.makeText(ChooseSectorActivity.this, "No data has been added yet!", Toast.LENGTH_LONG).show();
                    }
                    for(DocumentChange sec : documentSnapshots.getDocumentChanges()){
                        if(sec.getType() == DocumentChange.Type.ADDED){
                            Sectors sectors = sec.getDocument().toObject(Sectors.class);
                            sectorList.add(sectors);
                            sectorListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        addSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newSector = new Intent(ChooseSectorActivity.this, NewSectorActivity.class);
                startActivity(newSector);
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
        Intent mainIntent = new Intent(ChooseSectorActivity.this, SetupActivity.class);
        startActivity(mainIntent);
    }

    private void myWords() {
        Intent mainIntent = new Intent(ChooseSectorActivity.this, MyWordsActivity.class);
        startActivity(mainIntent);
    }
    private void home() {
        Intent mainIntent = new Intent(ChooseSectorActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
    private void logout() {
        firebaseAuth.signOut();
        sendToLogin();
    }
    private void sendToLogin() {
        Intent mainIntent = new Intent(ChooseSectorActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
