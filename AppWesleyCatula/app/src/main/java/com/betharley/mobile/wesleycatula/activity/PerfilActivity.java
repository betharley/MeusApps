package com.betharley.mobile.wesleycatula.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.betharley.mobile.wesleycatula.R;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().setTitle( "Perfil" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
