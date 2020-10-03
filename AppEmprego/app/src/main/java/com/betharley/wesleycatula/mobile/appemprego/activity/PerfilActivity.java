package com.betharley.wesleycatula.mobile.appemprego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.betharley.wesleycatula.mobile.appemprego.AbrirSiteActivity;
import com.betharley.wesleycatula.mobile.appemprego.R;

public class PerfilActivity extends AppCompatActivity {
    private String siteWesleyCatula = "https://www.wesleycatula.com/";
    private String siteVagasDisponiveis = "https://www.vagasdisponiveis.com/";
    private String siteNeovoo = "https://neuvoo.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().setTitle( "Perfil" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }
    public void abrirWesleyCatula(View view){
        Intent intent = new Intent(PerfilActivity.this, AbrirSiteActivity.class);
        intent.putExtra( "siteClicado", siteWesleyCatula);
        startActivity( intent );
    }
    public void abrirVagasDisponiveis(View view){
        Intent intent = new Intent(PerfilActivity.this, AbrirSiteActivity.class);
        intent.putExtra( "siteClicado", siteVagasDisponiveis);
        startActivity( intent );
    }
    public void abrirNeuvoo(View view){
        Intent intent = new Intent(PerfilActivity.this, AbrirSiteActivity.class);
        intent.putExtra( "siteClicado", siteNeovoo);
        startActivity( intent );
    }


/*    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }*/
}
