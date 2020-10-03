package com.betharley.mobile.portfolioapp.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;

import com.betharley.mobile.portfolioapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidActivity extends AppCompatActivity {
    private AdapterAndroid adapterAndroid;
    private RecyclerView recyclerView;
    private List<Android> lista;

    private DatabaseReference refData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lista = new ArrayList<>();
        adapterAndroid = new AdapterAndroid(lista, this);
        recyclerView = findViewById(R.id.recyclerview_android);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter( adapterAndroid );

        carregar_dados();
    }

    private void carregar_dados(){

        refData = FirebaseDatabase.getInstance().getReference();

        refData.child("Android")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if( dataSnapshot.exists() ){
                            for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                                Android android = dado.getValue(Android.class);
                                lista.add( android );
                            }
                            //Collections.reverse( lista );
                            adapterAndroid.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}
