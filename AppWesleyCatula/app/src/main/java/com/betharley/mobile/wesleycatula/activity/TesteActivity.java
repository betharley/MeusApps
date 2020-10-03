package com.betharley.mobile.wesleycatula.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.betharley.mobile.wesleycatula.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TesteActivity extends AppCompatActivity {
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    TextView textView;
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste);


        textView = findViewById(R.id.teste_textView);



        //textView.setText(Html.fromHtml(getString(R.string.teste_html)));

    }

    public void publicar(View view){

        databaseRef.child("Postagem").child("texto").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ( dataSnapshot.exists() ){
                    //String texto = String.valueOf( dataSnapshot.getValue() );

                    String teste = getString(R.string.teste_html);
                    textView.setText( Html.fromHtml( (String) dataSnapshot.getValue())+ teste );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
