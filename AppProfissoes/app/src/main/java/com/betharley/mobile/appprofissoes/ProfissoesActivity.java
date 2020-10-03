package com.betharley.mobile.appprofissoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.betharley.mobile.appprofissoes.adaptador.AdaptadorProfissao;
import com.betharley.mobile.appprofissoes.model.Profissao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfissoesActivity extends AppCompatActivity {
    private Toolbar toolbarProfissao;

    private ArrayList<String> minha_lista = new ArrayList<>();
    private String profissaoArea = null;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private RecyclerView recyclerViewProfissao;
    private LinearLayoutManager layoutManager;
    //private ArrayList<Profissao> listaProfisao = new ArrayList<>();
    private AdaptadorProfissao adaptadorProfissao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profissoes);

        toolbarProfissao = findViewById(R.id.toolbarProfissao);
        setSupportActionBar( toolbarProfissao );
        getSupportActionBar().setTitle("Profissões");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        profissaoArea = getIntent().getStringExtra("profissao");
        Toast.makeText(this, ""+ profissaoArea, Toast.LENGTH_SHORT).show();
        getSupportActionBar().setTitle(profissaoArea);

        //ADAPTADOR E RECYCLER VIEW
        adaptadorProfissao = new AdaptadorProfissao(this, minha_lista, profissaoArea);
        recyclerViewProfissao = findViewById(R.id.recyclerViewProfissao);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewProfissao.setHasFixedSize(true);
        recyclerViewProfissao.setLayoutManager( layoutManager );
        recyclerViewProfissao.setAdapter( adaptadorProfissao );

    }

    @Override
    protected void onStart() {
        super.onStart();

        if( profissaoArea != null ){
            recuperarProfissao();
        }
    }

    private void recuperarProfissao(){
        DatabaseReference refProfissoes = databaseReference.child("Profissoes").child(profissaoArea);

        refProfissoes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                minha_lista.clear();
                for ( DataSnapshot snapshot : dataSnapshot.getChildren() ){
                    String nomeRecuperado = String.valueOf( snapshot.getValue() );
                    minha_lista.add( nomeRecuperado );
                }
                adaptadorProfissao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
