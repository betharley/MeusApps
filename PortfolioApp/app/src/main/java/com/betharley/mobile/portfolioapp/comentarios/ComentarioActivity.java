package com.betharley.mobile.portfolioapp.comentarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.betharley.mobile.portfolioapp.R;
import com.betharley.mobile.portfolioapp.portfolio.PortfolioItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import io.paperdb.Paper;

public class ComentarioActivity extends AppCompatActivity {

    private PortfolioItem portfolioItem;
    private DatabaseReference comentarioRef;
    private static String keyUsuario;

    private ArrayList<Comentario> listaComentario = new ArrayList<>();
    private AdaptadorComentario adaptadorComentario;
    private RecyclerView recyclerView;
    private EditText campoDigitado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Paper.init(ComentarioActivity.this);
        keyUsuario = Paper.book().read("keyUsuario");
        campoDigitado = findViewById(R.id.comment_input);

        //Log.i("Comentario ", " onCreate " + keyUsuario );

        portfolioItem = (PortfolioItem) getIntent().getSerializableExtra("portfolioItem");
        //getSupportActionBar().setTitle( "Comentários" );
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        //ADAPTADOR E RECYCLER VIEW
        adaptadorComentario = new AdaptadorComentario(listaComentario, ComentarioActivity.this);
        adaptadorComentario.setPorfFolio( portfolioItem );
        adaptadorComentario.setkeyUsuario( keyUsuario );
        recyclerView = findViewById(R.id.recyclerviewComentario);
        recyclerView.setLayoutManager( new LinearLayoutManager( this));
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adaptadorComentario );

        if( portfolioItem != null ){
            carregarDados();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        finish();
        return false;
    }

    private void carregarDados(){

        //Log.i("Comentario ", " keyUsuario  " + keyUsuario );

        comentarioRef = FirebaseDatabase.getInstance().getReference();

        comentarioRef.child("Comentarios").child( portfolioItem.getKey() ) //
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if( dataSnapshot.exists() ){
                            listaComentario.clear();
                            for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                                Comentario comentario = (Comentario) dado.getValue(Comentario.class);
                                listaComentario.add( comentario );
                            }
                            //Collections.reverse( listaComentario );
                            //Log.i("Comentario ", " listaComentario.size()  " + listaComentario.size() );
                            adaptadorComentario.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void salvarComentario(View view){
        keyUsuario = Paper.book().read("keyUsuario");

        String textoDigitado = campoDigitado.getText().toString();
        if( TextUtils.isEmpty( textoDigitado ) ){
            return;
        }

        comentarioRef = FirebaseDatabase.getInstance().getReference()
                .child("Comentarios").child( portfolioItem.getKey() ); //.child("IdComentario");

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat data = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = data.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        String currentTime = time.format(calendar.getTime());
        String dataTime = currentDate + "  " + currentTime;

        String idComentario = comentarioRef.push().getKey();
        final Comentario comentario = new Comentario(textoDigitado, dataTime, keyUsuario, idComentario );

        comentarioRef.child(idComentario).setValue( comentario )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Log.i("Comentario", "TEXTO SALVO " + comentario);
                        campoDigitado.setText("");
                        adaptadorComentario.notifyDataSetChanged();
                        //adaptadorComentario.notifyItemInserted(listaComentario.size());
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.i("Comentario", "onStart " );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
