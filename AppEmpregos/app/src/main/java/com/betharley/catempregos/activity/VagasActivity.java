package com.betharley.catempregos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.betharley.catempregos.R;
import com.betharley.catempregos.helpe.SetupFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class VagasActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = SetupFirebase.getAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vagas);
    }

    public void deslogar(View view){
        autenticacao.signOut();
        finish();
    }
}
