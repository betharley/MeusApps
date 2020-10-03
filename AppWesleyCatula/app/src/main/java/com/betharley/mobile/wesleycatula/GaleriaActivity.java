package com.betharley.mobile.wesleycatula;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.betharley.mobile.wesleycatula.adapter.AdaptadorFoto;
import com.betharley.mobile.wesleycatula.model.Foto;

import java.util.ArrayList;

public class GaleriaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFoto;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorFoto adaptadorFoto;
    private ArrayList<Foto> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        getSupportActionBar().setTitle( "Fotos" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        //ADAPTADOR E RECYCLER VIEW
        lista = new ArrayList<>();
        adaptadorFoto = new AdaptadorFoto(this, lista);
        recyclerViewFoto = findViewById(R.id.recyclerViewFoto);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewFoto.setLayoutManager( layoutManager );
        recyclerViewFoto.setAdapter( adaptadorFoto );

    }

    private void carregarFotos(){
        lista.add( new Foto( R.drawable.imagem_01 ));
        lista.add( new Foto( R.drawable.imagem_02 ));
        lista.add( new Foto( R.drawable.imagem_04 ));
        lista.add( new Foto( R.drawable.imagem_05 ));
        lista.add( new Foto( R.drawable.imagem_06 ));
        lista.add( new Foto( R.drawable.imagem_07 ));
        lista.add( new Foto( R.drawable.imagem_08 ));
        lista.add( new Foto( R.drawable.imagem_03 ));

        adaptadorFoto.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarFotos();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }

}
