package com.betharley.wesleycatula.mobile.appemprego.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.adaptador.AdaptadorPostagem;
import com.betharley.wesleycatula.mobile.appemprego.model.Postagem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostagemActivity extends AppCompatActivity {
    private Dialog dialog;
    private Toolbar toolbarPostagem;

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private ArrayList<Postagem> lista = new ArrayList<>();
    private RecyclerView recyclerViewPostagem;
    private AdaptadorPostagem adaptadorPostagem;


    private int contador = 1;
    public String JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=";
    public static TextView txtCarregarMais;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        txtCarregarMais = findViewById(R.id.carregar_mais);
        //Toolbar postagem_toolbar = findViewById(R.id.postagem_toolbar);
        getSupportActionBar().setTitle( "Postagem" );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        adView = (AdView) findViewById(R.id.adViewPostagem);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.shape_fundo_radius));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable( true );

        //ADAPTADOR E A LISTA DE RECYCLERVIEW
        adaptadorPostagem = new AdaptadorPostagem(this.lista, PostagemActivity.this, txtCarregarMais, new AdaptadorPostagem.AdicionarListener() {
            @Override
            public void onAdicionar(int tamanho) {
                contador++;
                JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=" + contador;
                jsonrequest();
            }
        });
        recyclerViewPostagem = findViewById(R.id.recyclerViewPostagem);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPostagem.setLayoutManager( layoutManager );
        recyclerViewPostagem.setHasFixedSize( true );
        recyclerViewPostagem.setAdapter( adaptadorPostagem );

        contador = 1;

    }

    @Override
    protected void onStart() {
        super.onStart();
        lista.clear();
        contador = 1;
        JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=" + contador;
        jsonrequest();
        //Toast.makeText(MainActivity.this, "onStart/ contador " + contador, Toast.LENGTH_LONG).show();
    }

    public void carregar_mais(View view){
        contador = contador + 1;
        JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=" + contador;
        jsonrequest();
        txtCarregarMais.setVisibility( View.GONE );
        //Toast.makeText(MainActivity.this, "Carregar mais / contador " + contador, Toast.LENGTH_LONG).show();

    }

    public void jsonrequest() {
        dialog.show();
        this.request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Postagem postagem = new Postagem();

                        //TITULO
                        JSONObject jsonObjectTitle = jsonObject.getJSONObject("title");
                        postagem.setTitle( jsonObjectTitle.getString("rendered") );

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

                        /*postagem.setName(jsonObject.getString("name"));
                        postagem.setDescription(jsonObject.getString("description"));
                        postagem.setRating(jsonObject.getString("Rating"));
                        postagem.setEpisode(jsonObject.getInt("episode"));
                        postagem.setCategory(jsonObject.getString("categorie"));
                        postagem.setStudio(jsonObject.getString("studio"));
                        postagem.setImage_url(jsonObject.getString("img"));*/

                        lista.add(postagem);

                    } catch (JSONException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();
                //NOTIFICA O ADAPTADOR
                adaptadorPostagem.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, contador + " \n " + JSON_URL, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        requestQueue = Volley.newRequestQueue(PostagemActivity.this);
        requestQueue.add(request);
    }




}
