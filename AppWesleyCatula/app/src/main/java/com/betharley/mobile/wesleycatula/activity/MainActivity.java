package com.betharley.mobile.wesleycatula.activity;

import android.content.Intent;
import android.os.Bundle;

import com.betharley.mobile.wesleycatula.GaleriaActivity;
import com.betharley.mobile.wesleycatula.R;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //ADAPTADOR E A LISTA DE RECYCLERVIEW
        //adaptadorPostagem = new AdaptadorPostagem(this.lista, getApplicationContext(), txtCarregarMais, new AdaptadorPostagem.AdicionarListener() {
            //@Override
            //public void onAdicionar(int tamanho) {
                //contador++;
                //JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=" + contador;
                //jsonrequest();
           // }
        //});


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intentMain = new Intent(MainActivity.this, MainActivity.class);
            startActivity( intentMain );
            finish();
        } else if (id == R.id.nav_gallery) {
            Intent intentGaleria = new Intent(MainActivity.this, GaleriaActivity.class);
            startActivity( intentGaleria );
        } else if (id == R.id.nav_slideshow) {
            Intent intentFrases = new Intent(MainActivity.this, FrasesActivity.class);
            startActivity( intentFrases );
        } else if (id == R.id.nav_tools) {
            Intent intentPerfil = new Intent(MainActivity.this, PerfilActivity.class);
            startActivity( intentPerfil );
        } else if (id == R.id.nav_send) {
            Intent intentFechar = new Intent(MainActivity.this, MainActivity.class);
            intentFechar.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            finish();
            //startActivity( intent );
        } else if (id == R.id.nav_prevencao) {
            Intent intentPrevencao = new Intent(MainActivity.this, PrevencaoActivity.class);
            startActivity( intentPrevencao );
    }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
