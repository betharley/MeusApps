package com.betharley.wesleycatula.mobile.appemprego.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.adaptador.AdaptadorFoto;
import com.betharley.wesleycatula.mobile.appemprego.adaptador.AdaptadorGaleria;
import com.betharley.wesleycatula.mobile.appemprego.model.Foto;
import com.betharley.wesleycatula.mobile.appemprego.model.Galeria;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import io.paperdb.Paper;

public class GaleriaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFoto;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorGaleria adaptadorGaleria;
    private ArrayList<Galeria> listaGaleria;

    private AdView adView;
    private DatabaseReference databaseRef;
    private static String keyUsuario;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);

        Paper.init(this);

        getSupportActionBar().setTitle( "Minhas Fotos" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        adView = (AdView) findViewById(R.id.adViewGaleria);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        //ADAPTADOR E RECYCLER VIEW
        listaGaleria = new ArrayList<>();
        adaptadorGaleria = new AdaptadorGaleria(this, listaGaleria);
        recyclerViewFoto = findViewById(R.id.recyclerViewFoto);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewFoto.setLayoutManager( layoutManager );
        recyclerViewFoto.setAdapter( adaptadorGaleria );

        carregarDialog();
    }
    private void verificarKey(){
        keyUsuario = Paper.book().read( "keyUsuario" );
        //Log.i("Galeria "," keyUsuario "+keyUsuario);
        if( keyUsuario == null ){
            databaseRef = FirebaseDatabase.getInstance().getReference().child("Galeria");
            String KeyGeradaDatabase = databaseRef.push().getKey();
            Paper.book().write("keyUsuario", KeyGeradaDatabase);
            //Log.i("Galeria ","KeyGeradaDatabase "+KeyGeradaDatabase);
        }

        adaptadorGaleria.setkeyUsuario( keyUsuario );
        //Log.i("Galeria "," keyUsuario "+keyUsuario);
    }
    private void carregarGaleria(){
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Galeria");

        verificarKey();
        if( keyUsuario == null ){
            return;
        }
        dialog.show();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ( dataSnapshot.exists() ){
                    listaGaleria.clear();
                    for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                        Galeria galeria = (Galeria) dado.getValue(Galeria.class);
                        listaGaleria.add( galeria );
                    }
                    dialog.dismiss();
                    Collections.reverse(listaGaleria);
                    adaptadorGaleria.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
            }
        });



        /*for ( int i = 0;  i < 8; i++){
           String chaveGerada = databaseRef.push().getKey();

            HashMap<String, Object> map = new HashMap<>();
            map.put(chaveGerada, "chaveGerada");
            databaseRef.child("Galeria").child(chaveGerada).child("key").setValue(chaveGerada)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
        }*/


    }

   /* private void carregarFotos(){
        lista.add( new Foto( R.drawable.imagem_01 ));
        lista.add( new Foto( R.drawable.imagem_02 ));
        lista.add( new Foto( R.drawable.imagem_04 ));
        lista.add( new Foto( R.drawable.imagem_05 ));
        lista.add( new Foto( R.drawable.imagem_06 ));
        lista.add( new Foto( R.drawable.imagem_07 ));
        lista.add( new Foto( R.drawable.imagem_08 ));
        lista.add( new Foto( R.drawable.imagem_03 ));

        adaptadorGaleria.notifyDataSetChanged();
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        //carregarFotos();
        carregarGaleria();
        //Log.i("Galeria ","onStart ");
    }

/*    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }*/

    private void carregarDialog(){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.shape_fundo_radius));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable( true );
    }
}
