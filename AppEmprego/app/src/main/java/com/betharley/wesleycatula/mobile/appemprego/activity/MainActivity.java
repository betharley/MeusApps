package com.betharley.wesleycatula.mobile.appemprego.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.betharley.wesleycatula.mobile.appemprego.AbrirSiteActivity;
import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.adaptador.AdaptadorVaga;
import com.betharley.wesleycatula.mobile.appemprego.model.Vaga;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

//https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=15

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String siteWesleyCatula = "https://www.wesleycatula.com/";
    private String siteVagasDisponiveis = "https://www.vagasdisponiveis.com/";
    private String siteNeovoo = "https://neuvoo.com";

    private Dialog dialog;
    private AdView adView;

    private RequestQueue mQueue;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private ArrayList<Vaga> listaVaga = new ArrayList<>();
    private RecyclerView recyclerViewPostagem;
    private AdaptadorVaga adaptadorVaga;

    private int contador = 0;
    private boolean pagina = true;
    private boolean validador = true;
    public static String JSON_URL = "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=";
    private static String keyUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        verificarKey();
        //Paper.book().delete("keyUsuario");
        //Paper.book().destroy();
        //Log.i("MainActivity", "" + Paper.book().read("keyUsuario"));

        Toolbar toolbar = findViewById(R.id.toolbar);

        adView = (AdView) findViewById(R.id.adViewContent);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);


        setSupportActionBar(toolbar);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.shape_fundo_radius));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable( true );

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        mQueue = Volley.newRequestQueue(MainActivity.this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //ADAPTADOR E A LISTA DE RECYCLERVIEW
        adaptadorVaga = new AdaptadorVaga(this.listaVaga, getApplicationContext(), new AdaptadorVaga.AdicionarListener() {
            @Override
            public void onAdicionar(int tamanho) {
                if( pagina ){
                    //contador=contador+15;
                    //Log.i("JSON_URL ","JSON_URL " + JSON_URL);
                    JSON_URL = "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=" + contador;
                    //jsonrequest();
                    jsonParse();
                }
            }
        });
        recyclerViewPostagem = findViewById(R.id.recyclerViewPostagem);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPostagem.setLayoutManager( layoutManager );
        recyclerViewPostagem.setHasFixedSize( true );
        recyclerViewPostagem.setAdapter(adaptadorVaga);

        contador = 0;
        dialog.show();


        /*recyclerViewPostagem.addOnItemTouchListener( new RecyclerItemClickListener(
                getApplicationContext(), recyclerViewPostagem, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Usuario usuarioSel = lista_contatos.get( position );
                Vaga vagaClicada = listaVaga.get( position );

                Intent intent = new Intent(MainActivity.this, VisualizarActivity.class);
                intent.putExtra( "vagaClicada", vagaClicada);
                //startActivity( intent );

                //Remove usuario selecionado da lista
                //lista_contatos.remove( usuarioSel );

                //adiciona o usuario na nova lista selecionado
               // lista_grupo.add( usuarioSel );

                //Notifica os dois adaptadores da mudança na lista
                //adaptador_contatos.notifyDataSetChanged();
                //adaptador_grupo.notifyDataSetChanged();
                //atualizar_toolbar();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }
        ));*/
    }

    private void jsonParse(){
        //dialog.show();
        //contador = contador + 15;
        String URL_JSON =  "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=" + contador;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_JSON, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Log.i("MainAcivity ","JSON_URL " + JSON_URL);
                            JSONArray jsonArray = response.getJSONArray("results");
                            //Log.i("MainAcivity Teste ", " jsonArray  " + jsonArray );
                            //Log.i("MainAcivity Teste ", " length  " + jsonArray.length() );
                            //Log.i("MainAcivity Teste ", " contador  " + contador );
                            //Log.i("MainAcivity Teste ", " listaVaga INICIO " + listaVaga.size() );

                            if( jsonArray.length()  > 0 ){
                                dialog.show();
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject vagaRecuperada = jsonArray.getJSONObject(i);
                                    //Log.i("MainAcivity Teste ", " vagaRecuperada  " + vagaRecuperada );

                                    //String firstName = vagaRecuperada.getString("firstname");
                                    //int numero = vagaRecuperada.getInt("numero");

                                    Vaga vaga = new Vaga();
                                    //ID
                                    vaga.setJobkey( vagaRecuperada.getString("jobkey") );
                                    //TITULO
                                    vaga.setJobtitle( vagaRecuperada.getString("jobtitle") );
                                    //COMPANY
                                    vaga.setCompany( vagaRecuperada.getString("company") );
                                    //CITY
                                    vaga.setCity( vagaRecuperada.getString("city") );
                                    //ESTADO
                                    vaga.setState( vagaRecuperada.getString("state") );
                                    //COUNTRY
                                    vaga.setCoutry( vagaRecuperada.getString("country") );
                                    //formattedLocation - LOCALIZAÇÃO FORMATADA
                                    vaga.setFormattedLocation( vagaRecuperada.getString("formattedLocation") );
                                    //source
                                    vaga.setSource( vagaRecuperada.getString("source") );
                                    //DATA
                                    vaga.setDate( vagaRecuperada.getString("date") );
                                    //description
                                    vaga.setDescription( vagaRecuperada.getString("description") );
                                    //url
                                    vaga.setUrl( vagaRecuperada.getString("url") );
                                    //vaga.setUrl( "https://www.google.com.br/" );
                                    //logo
                                    vaga.setLogo( vagaRecuperada.getString("logo") );

                                    //bid
                                    vaga.setBid( vagaRecuperada.getInt("bid") );
                                    //currency
                                    vaga.setCurrency( vagaRecuperada.getString("currency") );
                                    //category
                                    vaga.setCategory( vagaRecuperada.getString("category") );
                                    //onmousedown
                                    vaga.setOnmousedown( vagaRecuperada.getString("onmousedown") );

                                    //Log.i("MainAcivity Teste ", " vaga  " + vaga.getJobtitle() );

                                    listaVaga.add(vaga);

                                }
                                contador = listaVaga.size() + 15;
                            }else {
                                pagina = false;
                            }
                            if ( !pagina && validador ){
                                //Toast.makeText(MainActivity.this, "Não temos mais vagas cadastradas", Toast.LENGTH_SHORT).show();
                                pagina = true;
                                validador = false;
                            }
                            //Log.i("MainAcivity Teste ", " listaVaga FINAL DO FOR  " + listaVaga.size() );
                            dialog.dismiss();
                            //NOTIFICA O ADAPTADOR
                            adaptadorVaga.notifyDataSetChanged();

                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        validador = true;
        listaVaga.clear();
        contador = 15;
        JSON_URL = "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=" + contador;
        //Log.i("MainAcivity Teste ", " JSON_URL  " + JSON_URL);
        //jsonrequest();
        jsonParse();
        //Toast.makeText(MainActivity.this, "onStart/ contador " + contador, Toast.LENGTH_LONG).show();

    }

    public void carregar_mais(View view){
        //contador = contador + 15;
        JSON_URL = "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=" + contador;
        //jsonrequest();
        jsonParse();
        //Toast.makeText(MainActivity.this, "Carregar mais / contador " + contador, Toast.LENGTH_LONG).show();

    }

    public void jsonrequest() {
        //dialog.show();
        this.request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //Log.i("MainAcivity Teste ", " response  " + response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Vaga vaga = new Vaga();

                        //JSONObject jsonObjectResults = jsonObject.getJSONObject("results");
                        //vaga.setJobtitle( jsonObjectResults.getString("rendered") );
                        //Log.i("MainAcivity Teste ", " jsonObjectResults " + jsonObjectResults);
                        //Log.i("MainAcivity Teste ", " jsonObject " + jsonObject);
                        //Log.i("MainAcivity Teste ", " jsonObject.getString(jobkey) " + jsonObject.getString("jobkey"));

                        //ID
                        vaga.setJobkey( jsonObject.getString("jobkey") );
                        //TITULO
                        vaga.setJobtitle( jsonObject.getString("jobtitle") );
                        //COMPANY
                        vaga.setCompany( jsonObject.getString("company") );
                        //CITY
                        vaga.setCity( jsonObject.getString("city") );
                        //ESTADO
                        vaga.setState( jsonObject.getString("state") );
                        //COUNTRY
                        vaga.setCoutry( jsonObject.getString("country") );
                        //formattedLocation - LOCALIZAÇÃO FORMATADA
                        vaga.setFormattedLocation( jsonObject.getString("formattedLocation") );
                        //source
                        vaga.setSource( jsonObject.getString("source") );
                        //DATA
                        vaga.setDate( jsonObject.getString("date") );
                        //description
                        vaga.setCompany( jsonObject.getString("description") );
                        //url
                        vaga.setCompany( jsonObject.getString("url") );
                        //logo
                        vaga.setCompany( jsonObject.getString("logo") );

                        //bid
                        //vaga.setCompany( jsonObject.getString("bid") );
                        //currency
                        vaga.setCompany( jsonObject.getString("currency") );
                        //category
                        vaga.setCompany( jsonObject.getString("category") );
                        //onmousedown
                        vaga.setCompany( jsonObject.getString("onmousedown") );


                        //Log.i("MainAcivity Teste ", "" + response.length());
/*
                        //TITULO
                        JSONObject jsonObjectTitle = jsonObject.getJSONObject("title");
                        vaga.setJobtitle( jsonObjectTitle.getString("rendered") );

                        //CONTEUDO
                        //postagem.setSource_url( jsonObject.getString("source_url") );
                        JSONObject jsonObjectConteudo = jsonObject.getJSONObject("content");
                        postagem.setContent( jsonObjectConteudo.getString("rendered") );

                        //DESTAQUE
                        JSONObject jsonObjectDestaque = jsonObject.getJSONObject("excerpt");
                        postagem.setExcerpt( jsonObjectDestaque.getString("rendered") );

                        //DATA
                        postagem.setData( jsonObject.getString("date") );

                        //IMAGEM
                        postagem.setSource_url( jsonObject.getString("jetpack_featured_media_url") );
*/
                        /*postagem.setName(jsonObject.getString("name"));
                        postagem.setDescription(jsonObject.getString("description"));
                        postagem.setRating(jsonObject.getString("Rating"));
                        postagem.setEpisode(jsonObject.getInt("episode"));
                        postagem.setCategory(jsonObject.getString("categorie"));
                        postagem.setStudio(jsonObject.getString("studio"));
                        postagem.setImage_url(jsonObject.getString("img"));*/

                        listaVaga.add(vaga);

                    } catch (JSONException e) {
                        //Log.i("MainAcivity Teste ", " EXCEÇÃO ");
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
                //NOTIFICA O ADAPTADOR
                adaptadorVaga.notifyDataSetChanged();
                //Log.i("MainAcivity Teste ", " notifyDataSetChanged ");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, contador + " \n " + JSON_URL, Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //Log.i("MainAcivity Teste ", " ErrorListener " + error);
                //Log.i("MainAcivity Teste ", " ErrorListener " + contador);
                //Log.i("MainAcivity Teste ", " ErrorListener " + JSON_URL);
            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
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
        }if (id == R.id.nav_pesquisar) {
            // Handle the camera action
            Intent intentPesquisar = new Intent(MainActivity.this, PesquisarActivity.class);
            startActivity( intentPesquisar );
        }else if (id == R.id.nav_site_catula) {
            // Handle the camera action
            Intent intentSite = new Intent(MainActivity.this, AbrirSiteActivity.class);
            intentSite.putExtra( "siteClicado", siteWesleyCatula);
            startActivity( intentSite );
            //finish();
        } else if (id == R.id.nav_site_vagas_disponiveis) {
            // Handle the camera action
            Intent intentSite = new Intent(MainActivity.this, AbrirSiteActivity.class);
            intentSite.putExtra( "siteClicado", siteVagasDisponiveis);
            startActivity( intentSite );
            //finish();
        }else if (id == R.id.nav_site_neuvoo) {
            // Handle the camera action
            Intent intentSite = new Intent(MainActivity.this, AbrirSiteActivity.class);
            intentSite.putExtra( "siteClicado", siteNeovoo);
            startActivity( intentSite );
            //finish();
        } else if (id == R.id.nav_my_posts) {
            Intent intentPostagem = new Intent(MainActivity.this, PostagemActivity.class);
            startActivity( intentPostagem );
        } else if (id == R.id.nav_gallery) {
            Intent intentGaleria = new Intent(MainActivity.this, GaleriaActivity.class);
            startActivity( intentGaleria );
        }else if (id == R.id.nav_slideshow) {
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
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void verificarKey(){
        keyUsuario = Paper.book().read( "keyUsuario" );
        //Log.i("Galeria "," keyUsuario "+keyUsuario);
        if( keyUsuario == null ){
            DatabaseReference databaseRefKey = FirebaseDatabase.getInstance().getReference().child("Galeria");
            String KeyGeradaDatabase = databaseRefKey.push().getKey();
            Paper.book().write("keyUsuario", KeyGeradaDatabase);
            //Log.i("Galeria ","KeyGeradaDatabase "+KeyGeradaDatabase);
        }
    }

}
