package com.betharley.wesleycatula.mobile.appemprego.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.adaptador.AdaptadorVaga;
import com.betharley.wesleycatula.mobile.appemprego.model.Vaga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.paperdb.Paper;

public class PesquisarActivity extends AppCompatActivity {
    private Spinner pesquisarEstado, pesquisarCidade;
    private Button pesquisar_estado_btn, pesquisar_cidade_btn;

    private String estadoEscolhido;
    private String  cidadesRecuperadsas;
    private CheckBox pesquisar_checkBox;

    private RequestQueue mQueue;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private ArrayList<Vaga> listaVaga = new ArrayList<>();
    private RecyclerView recyclerViewPesquisar;
    private AdaptadorVaga adaptadorVaga;

    private Dialog dialog;
    private int contador = 0;
    private boolean pagina = true;
    private boolean validador = true;
    public static String JSON_URL = "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=";
    private static String keyUsuario;

    private LinearLayout pesquisar_linearLayout;
    private SearchView pesquisar_searchView;
    private String textoSearchView;

    private String estadoBook;
    private String cidadeBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);

        Paper.init(this);

        //Toobar
        //getSupportActionBar().hide();
        //Toolbar toolbarPesquisar = findViewById(R.id.toolbar_pesquisar);
        //setSupportActionBar(toolbarPesquisar);
        getSupportActionBar().setTitle("Filtrar vaga");
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.shape_fundo_radius));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable( true );

        pesquisar_estado_btn = findViewById(R.id.pesquisar_estado_btn);
        pesquisar_cidade_btn = findViewById(R.id.pesquisar_cidade_btn);
        pesquisar_checkBox = findViewById(R.id.pesquisar_checkBox);
        pesquisar_linearLayout = findViewById(R.id.pesquisar_linearLayout);
        //pesquisar_searchView = findViewById(R.id.pesquisar_searchView);

        /*pesquisar_searchView.setQueryHint("Pesquisar vaga ...");
        pesquisar_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textoSearchView = query;
                //Toast.makeText(PesquisarActivity.this, " SEARCH VIEW CLICADO " + textoSearchView, Toast.LENGTH_SHORT).show();
                contador = 15;
                listaVaga.clear();
                //Toast.makeText(PesquisarActivity.this, "listaVaga " + listaVaga.size(), Toast.LENGTH_SHORT).show();

                jsonParseSearch( query );
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

        mQueue = Volley.newRequestQueue(PesquisarActivity.this);
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
        recyclerViewPesquisar = findViewById(R.id.recyclerViewPesquisar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewPesquisar.setLayoutManager( layoutManager );
        recyclerViewPesquisar.setHasFixedSize( true );
        recyclerViewPesquisar.setAdapter(adaptadorVaga);

        contador = 0;
        dialog.show();

       verificarBook();

    }

    public void verificarBook(){

        estadoBook = Paper.book().read("ESTADO");
        cidadeBook = Paper.book().read("CIDADE");

        if ( estadoBook != null && !estadoBook.equals("") && !estadoBook.equals("Estado") ){
            pesquisar_estado_btn.setEnabled( true );
            pesquisar_estado_btn.setAlpha( 1 );
            pesquisar_estado_btn.setText( estadoBook );
            pesquisar_linearLayout.setVisibility( View.VISIBLE );
            pesquisar_checkBox.setChecked(true);
            recuperarCidadeDoEstado(estadoBook);
            //String cidade = Paper.book().read("CIDADE");
            pesquisar_cidade_btn.setText( "Cidade" );
            //Toast.makeText(this, "ESTADO CIDADE", Toast.LENGTH_SHORT).show();
            jsonParseBook(); //Estado e cidade
        }

        if ( cidadeBook != null && !cidadeBook.equals("") && !cidadeBook.equals("Cidade")){
            pesquisar_cidade_btn.setEnabled( true );
            pesquisar_cidade_btn.setAlpha( 1 );
            pesquisar_cidade_btn.setText( cidadeBook );
            recuperarCidadeDoEstado(estadoBook);
            String cidade = Paper.book().read("CIDADE");
            pesquisar_cidade_btn.setText( cidade );
            //Toast.makeText(this, "CIDADE", Toast.LENGTH_SHORT).show();
            //jsonParseBook();
        }else
        if ( estadoBook != null && !estadoBook.equals("") && !estadoBook.equals("Estado") ){
            pesquisar_estado_btn.setEnabled( true );
            pesquisar_estado_btn.setAlpha( 1 );
            pesquisar_estado_btn.setText( estadoBook );
            pesquisar_linearLayout.setVisibility( View.VISIBLE );
            pesquisar_checkBox.setChecked(true);
            Toast.makeText(this, "ESTADO", Toast.LENGTH_SHORT).show();
            recuperarCidadeDoEstado(estadoBook);
            jsonParseBook();
        }

    }

    public void carregar_mais(View view){
        //contador = contador + 15;
        JSON_URL = "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=" + contador;
        //jsonrequest();
        jsonParse();
        //Toast.makeText(MainActivity.this, "Carregar mais / contador " + contador, Toast.LENGTH_LONG).show();

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
                                    vaga.setFormattedLocation( vagaRecuperada.getString("formattedLocation").replaceAll("null","") );
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


                                    if ( pesquisar_cidade_btn.isEnabled() && !pesquisar_cidade_btn.getText().toString().equals("Cidade")){
                                        String cidadeDoBotao = pesquisar_cidade_btn.getText().toString();
                                        if( cidadeDoBotao.contains( vaga.getCity() )){
                                            listaVaga.add(vaga);
                                        }
                                    }else
                                    if ( pesquisar_estado_btn.isEnabled() && !pesquisar_estado_btn.getText().toString().equals("Estado") ){
                                        String estadoDoBotao = pesquisar_estado_btn.getText().toString();
                                        if( estadoDoBotao.contains( vaga.getState() )){
                                            listaVaga.add(vaga);
                                        }
                                    }else
                                    if( !pesquisar_checkBox.isChecked() ){
                                        listaVaga.add(vaga);
                                    }

                                }
                                //contador = listaVaga.size() + 15;
                                //contador = contador + 15;
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
                            if( contador < 80 ){
                                contador = contador + 15;
                                jsonParse();
                            }

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

    public void verificarCheckBox(View view){
        boolean valorCheck = pesquisar_checkBox.isChecked();
        if( valorCheck ){
            pesquisar_estado_btn.setEnabled( true );
            pesquisar_estado_btn.setAlpha( 1 );
            pesquisar_estado_btn.setText("Estado");
            pesquisar_linearLayout.setVisibility( View.VISIBLE );
        }else {
            pesquisar_estado_btn.setEnabled( false );
            pesquisar_estado_btn.setAlpha( 0.5f );
            pesquisar_estado_btn.setText("Estado");
            pesquisar_linearLayout.setVisibility( View.GONE );
            Paper.book().write("ESTADO", "" );
            contador = 15;
            listaVaga.clear();
            jsonParse();
        }
        pesquisar_cidade_btn.setEnabled( false );
        pesquisar_cidade_btn.setAlpha(0.5f);
        pesquisar_cidade_btn.setText("Cidade");
        Paper.book().write("CIDADE", "" );
    }

    public void filtroPorEstado(View view){
        //Toast.makeText(this, " clique no botao ", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogEstado = new AlertDialog.Builder(this);
        dialogEstado.setTitle("Selecione o Estado desejado");
        //Log.i("PesquisarActivity", "cidadesRecuperadsas  " );

        //Configurar Spinner
        View viewSpinner = getLayoutInflater().inflate(R.layout.item_spinner_layout, null);
        final Spinner spinnerEstado = viewSpinner.findViewById(R.id.spinnerFiltro);
        //Configurar spinner de estados
        String[] estados = getResources().getStringArray(R.array.spiner_estados);
        ArrayAdapter<String> adapterEstado = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                estados
        );
        adapterEstado.setDropDownViewResource( android.R.layout.simple_spinner_item);
        spinnerEstado.setAdapter( adapterEstado );
        dialogEstado.setView( viewSpinner );

        dialogEstado.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                estadoEscolhido = spinnerEstado.getSelectedItem().toString();
                //Log.i("PesquisarActivity", "estadoEscolhido " + estadoEscolhido);
                pesquisar_estado_btn.setText(estadoEscolhido);
                Paper.book().write("ESTADO", estadoEscolhido );
                Paper.book().write("CIDADE", "" );
                pesquisar_cidade_btn.setText("Cidade");
                recuperarCidadeDoEstado(estadoEscolhido);

                contador = 15;
                listaVaga.clear();
                contador = 15;
                jsonParse();
            }
        });
        dialogEstado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = dialogEstado.create();
        alert.show();
    }
    private void recuperarCidadeDoEstado(String EstadoSelecionado){
        //Toast.makeText(this, " recuperarCidadeDoEstado ", Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference().child("Estados").child(EstadoSelecionado)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if( dataSnapshot.exists() ){
                                cidadesRecuperadsas = dataSnapshot.child("cidades").getValue().toString();
                                //Log.i("PesquisarActivity", "cidadesRecuperadsas  " + cidadesRecuperadsas);
                                pesquisar_cidade_btn.setEnabled( true );
                                pesquisar_cidade_btn.setAlpha(1);
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void filtroPorCidade(View view){
        //Toast.makeText(this, " CLIQUE NO BOTAO CIDADE", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder dialogCidade = new AlertDialog.Builder(this);
        dialogCidade.setTitle("Selecione a Cidade do Estado " + estadoEscolhido);
        //Log.i("PesquisarActivity", "cidadesRecuperadsas  " );

        //Configurar Spinner
        View viewSpinner = getLayoutInflater().inflate(R.layout.item_spinner_layout, null);
        final Spinner spinnerCidade = viewSpinner.findViewById(R.id.spinnerFiltro);
        //Configurar spinner de estados   alunoRecebido.split(";");
        String[] cidades = cidadesRecuperadsas.split(",");  //getResources().getStringArray(R.array.spiner_estados); // cidadesRecuperadsas
        ArrayAdapter<String> adapterEstado = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                cidades
        );
        adapterEstado.setDropDownViewResource( android.R.layout.simple_spinner_item);
        spinnerCidade.setAdapter( adapterEstado );
        dialogCidade.setView( viewSpinner );

        dialogCidade.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cidadeEscolhido = spinnerCidade.getSelectedItem().toString();
                //Log.i("PesquisarActivity", "estadoEscolhido " + cidadeEscolhido);
                pesquisar_cidade_btn.setText(cidadeEscolhido);
                //Paper.book().write("CIDADE", cidadeEscolhido );
                //recuperarCidadeDoEstado(estadoEscolhido);

                contador = 15;
                listaVaga.clear();
                jsonParse();
            }
        });
        dialogCidade.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alert = dialogCidade.create();
        alert.show();
    }




    private void jsonParseBook(){
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
                                    vaga.setFormattedLocation( vagaRecuperada.getString("formattedLocation").replaceAll("null","") );
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
                                    if( cidadeBook.contains( vaga.getCity() ) && !cidadeBook.equals("Cidade") && !cidadeBook.equals("")){
                                        listaVaga.add(vaga);
                                        continue;
                                    }

                                    if( estadoBook.contains( vaga.getState()) && cidadeBook.contains( vaga.getCity()) && !cidadeBook.equals("Cidade") && !estadoBook.equals("Estado") ){
                                        listaVaga.add(vaga);
                                    }
                                    //estadoBook
                                    //cidadeBook

                                    if( estadoBook.contains( vaga.getState() ) && !estadoBook.equals("Estado") && !estadoBook.equals("") ){
                                        listaVaga.add(vaga);

                                    }

                                }
                                //contador = listaVaga.size() + 15;
                                //contador = contador + 15;
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
                            if( contador < 80 ){
                                contador = contador + 15;
                                jsonParseBook();
                            }

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





    private void jsonParseSearch(final String textoSearch){
        //dialog.show();
        //contador = contador + 15;
        String URL_JSON =  "https://neuvoo.com/services/api-new/search?ip=1.1.1.1&useragent=123asd&k=vendedor&l=GO&country=br&jobdesc=1&contenttype=organic&limit=15&format=json&publisher=eec90186&cpcfloor=1&subid=10101&start=" + contador;
        //Toast.makeText(this, ""+URL_JSON, Toast.LENGTH_SHORT).show();
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
                                //dialog.show();
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
                                    vaga.setFormattedLocation( vagaRecuperada.getString("formattedLocation").replaceAll("null","") );
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
                                    String texoSearchMinusculo = textoSearch.trim().toLowerCase().toString();
                                    if( pesquisar_estado_btn.isEnabled() && !pesquisar_estado_btn.getText().toString().equals("Estado") && pesquisar_cidade_btn.isEnabled() && !pesquisar_cidade_btn.getText().toString().equals("Cidade") ){
                                        String estadoDoBotao = pesquisar_estado_btn.getText().toString().toLowerCase();
                                        String cidadeDoBotao = pesquisar_cidade_btn.getText().toString().toLowerCase();

                                        if( estadoDoBotao.contains( vaga.getState().toLowerCase() ) && cidadeDoBotao.contains( vaga.getCity().toLowerCase() ) ){
                                            if( texoSearchMinusculo.contains(vaga.getJobtitle().toLowerCase() ) ){
                                                listaVaga.add(vaga);
                                            }
                                        }
                                    }else
                                    if( pesquisar_estado_btn.isEnabled() && !pesquisar_estado_btn.getText().toString().equals("Estado")){
                                        String estadoDoBotao = pesquisar_estado_btn.getText().toString().toLowerCase();
                                        if( estadoDoBotao.contains( vaga.getState().toLowerCase() )){
                                            if( texoSearchMinusculo.contains(vaga.getJobtitle().toLowerCase() ) ){
                                                listaVaga.add(vaga);
                                            }
                                        }
                                    }else
                                     if( texoSearchMinusculo.contains(vaga.getJobtitle().toLowerCase() ) ){
                                         listaVaga.add(vaga);
                                         //Toast.makeText(PesquisarActivity.this, vaga.getJobtitle().toLowerCase()+ " Minusculo "+texoSearchMinusculo, Toast.LENGTH_SHORT).show();

                                     }

                                    //Toast.makeText(PesquisarActivity.this, "LOOP "+pesquisarSearchView, Toast.LENGTH_SHORT).show();

                                }
                                //contador = listaVaga.size() + 15;
                                //contador = contador + 15;
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

        if( contador < 80 ){
            contador = contador + 15;
            jsonParseSearch(textoSearch);
        }
    }


    public void pesquisar_voltar_main(View view){
        Intent intentMain = new Intent(PesquisarActivity.this, MainActivity.class);
        startActivity( intentMain );
        finish();
    }

}
