package com.betharley.mobile.ecommerceonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.betharley.mobile.ecommerceonline.adapter.AdaptadorComentario;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Comentario;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComentariosActivity extends AppCompatActivity {

    private Produto produto;
    private RecyclerView recyclerView;
    private AdaptadorComentario adaptador;
    private LinearLayoutManager layoutManager;
    private ArrayList<Comentario> lista = new ArrayList<>();

    private DatabaseReference comentarioRef;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar_vendedor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle( "Comentários" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_ic_close_black_24dp);

        //INICIAR COMPONENTES
        recyclerView = findViewById(R.id.comentarios_recyclerView);
        adaptador = new AdaptadorComentario(lista, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( adaptador );

        //BUDLE
        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            produto = (Produto) bundle.getSerializable("produto");
            //mostrarComentarios();
        }
    }

    private void mostrarComentarios(){
        comentarioRef = Firebase.getDatabase()
                .child( "Comentarios" )
                .child( produto.getId() );

        valueEventListener = comentarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for( DataSnapshot dado : dataSnapshot.getChildren() ){
                    Comentario comentario = dado.getValue(Comentario.class);
                    lista.add( comentario );
                }
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mostrarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentarioRef.removeEventListener( valueEventListener );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
