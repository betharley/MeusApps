package com.betharley.mobile.ecommerceonline.vendedor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.adapter.AdaptadorPublicar;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ListaVendedorActivity extends AppCompatActivity {
    Toolbar toolbar_lista;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorPublicar adaptador;
    private ArrayList<Produto> lista = new ArrayList<>();

    private DatabaseReference produtoRef;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vendedor);

        //TOLLBAR
        toolbar_lista = findViewById(R.id.toolbar_vendedor);
        setSupportActionBar(toolbar_lista);
        getSupportActionBar().setTitle("Lista Para Publicar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_ic_arrow_back_black_24dp);


        //RECYCLER VIEW E ADAPTADOR
        adaptador = new AdaptadorPublicar(lista, this);
        recyclerView = findViewById(R.id.vendedor_lista_recyclerView);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adaptador );

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarProdutos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        produtoRef.removeEventListener(valueEventListener);
    }


    private void recuperarProdutos(){
        produtoRef = Firebase.getDatabase()
                .child("Vendedores")
                .child( UsuarioFirebase.getIdUsuario() )
                .child("Produtos");

        valueEventListener = produtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                    Produto produto = dado.getValue(Produto.class);
                    lista.add( produto );
                    //Log.i("PRODUTO OBJETO ", dado.getValue() +"" );
                }
                Collections.reverse(lista);
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
