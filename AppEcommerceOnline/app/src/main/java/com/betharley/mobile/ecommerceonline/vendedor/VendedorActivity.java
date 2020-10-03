package com.betharley.mobile.ecommerceonline.vendedor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.adapter.AdaptadorOrdem;
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

public class VendedorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorOrdem adaptador;
    private ArrayList<Produto> lista = new ArrayList<>();

    private DatabaseReference enviadosRef;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);

        //TOLLBAR
        Toolbar toolbar_vendedor = findViewById(R.id.toolbar_vendedor);
        setSupportActionBar(toolbar_vendedor);
        getSupportActionBar().setTitle("Lista Pedidos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_ic_close_black_24dp);


        //RECYCLER VIEW E ADAPTADOR
        adaptador = new AdaptadorOrdem(lista,this);
        recyclerView = findViewById(R.id.vendedor_recyclerView);
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
        enviadosRef.removeEventListener(valueEventListener);
    }

    private void recuperarProdutos(){
        enviadosRef = Firebase.getDatabase()
                .child("Enviados")
                .child( UsuarioFirebase.getIdUsuario() );

        valueEventListener = enviadosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                    Produto produto = dado.getValue( Produto.class );
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vendedor, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch ( id ){

            case R.id.vend_adicionar:
                Intent intentNovo = new Intent(getApplicationContext(), AdicionarActivity.class);
                startActivity( intentNovo );
                return true;

            case R.id.vend_notificacoes:
                return true;

            case R.id.vend_listar:
                Intent intentLista = new Intent(getApplicationContext(), ListaVendedorActivity.class);
                startActivity( intentLista );
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
