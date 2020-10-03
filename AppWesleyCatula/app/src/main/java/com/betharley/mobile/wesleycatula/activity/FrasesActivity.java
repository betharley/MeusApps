package com.betharley.mobile.wesleycatula.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.betharley.mobile.wesleycatula.R;
import com.betharley.mobile.wesleycatula.adapter.AdaptadorFrase;
import com.betharley.mobile.wesleycatula.model.Frase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FrasesActivity extends AppCompatActivity {

    //private String[] frases = new String[60];

    private RecyclerView recyclerViewFrase;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorFrase adaptadorFrase;
    private ArrayList<Frase> lista;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frases);

        //getSupportActionBar().setTitle( "Frases" );
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        lista = new ArrayList<>();
        adaptadorFrase = new AdaptadorFrase(lista, this);
        recyclerViewFrase = findViewById(R.id.recyclerViewFrases);
        layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerViewFrase.setLayoutManager( layoutManager );
        recyclerViewFrase.setAdapter( adaptadorFrase );
        recyclerViewFrase.setHasFixedSize( true );


    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
