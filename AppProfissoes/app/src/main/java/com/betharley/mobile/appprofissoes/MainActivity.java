package com.betharley.mobile.appprofissoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void acessar(View view) {
        Intent intentAreas = new Intent(getApplicationContext(), AreasActivity.class);
        startActivity( intentAreas );
    }
}
