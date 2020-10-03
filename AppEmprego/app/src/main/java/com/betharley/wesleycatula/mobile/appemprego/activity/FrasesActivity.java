package com.betharley.wesleycatula.mobile.appemprego.activity;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.adaptador.AdaptadorFrase;
import com.betharley.wesleycatula.mobile.appemprego.model.Frase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class FrasesActivity extends AppCompatActivity {

    //private String[] frases = new String[60];

    private RecyclerView recyclerViewFrase;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorFrase adaptadorFrase;
    private ArrayList<Frase> lista;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frases);

        adView = (AdView) findViewById(R.id.adViewFrases);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        getSupportActionBar().setTitle( "Frases" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        lista = new ArrayList<>();
        adaptadorFrase = new AdaptadorFrase(lista, this);
        recyclerViewFrase = findViewById(R.id.recyclerViewFrases);
        layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerViewFrase.setLayoutManager( layoutManager );
        recyclerViewFrase.setAdapter( adaptadorFrase );
        recyclerViewFrase.setHasFixedSize( true );


    }

    private void carregarFrases(){

        Resources res = getResources();
        String[] planets = res.getStringArray(R.array.frases_array);

        Resources resAutor = getResources();
        String[] autor = resAutor.getStringArray(R.array.autor_array);

        for (int i = 0; i < planets.length; i++){
            lista.add( new Frase( planets[i], autor[i] ));
        }

        adaptadorFrase.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarFrases();

    }

/*    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }*/
}
