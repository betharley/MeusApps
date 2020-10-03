package com.betharley.mobile.wesleycatula.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.betharley.mobile.wesleycatula.R;
import com.betharley.mobile.wesleycatula.adapter.AdaptadorPrevencao;
import com.betharley.mobile.wesleycatula.model.Prevencao;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PrevencaoActivity extends AppCompatActivity {
    //private Toolbar toolbarPrevencao;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Prevencao> listaPrevencao = new ArrayList<>();
    private AdaptadorPrevencao adaptadorPrevencao;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevencao);

        //toolbarPrevencao = findViewById(R.id.toolbarPrevencao);
        //setSupportActionBar(toolbarPrevencao);
        //getSupportActionBar().setTitle( "Dicas e Prevenção" );
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RECYCLER VIEW E ADAPTADOR
        recyclerView = findViewById(R.id.recyclerViewPrevencao);
        adaptadorPrevencao = new AdaptadorPrevencao(this, listaPrevencao);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adaptadorPrevencao );

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("Nome", "nome");
        mapa.put("idade", "nome");
        mapa.put("endereço", "nome");
        mapa.put("telefone", "nome");

        DatabaseReference dadaRef = FirebaseDatabase.getInstance().getReference();
        dadaRef.child("TESTEAndo").setValue(mapa).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PrevencaoActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarPrevendao();
    }

    private void carregarPrevendao(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Prevencao").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if( dataSnapshot.exists() ){
                            listaPrevencao.add( (Prevencao) dataSnapshot.getValue(Prevencao.class) );
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        adaptadorPrevencao.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
