package com.betharley.wesleycatula.mobile.appemprego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AbrirSiteActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AdView mAdView;

    private AdView adView;
    // https://www.vagasdisponiveis.com
    private static String SITE_CLICADO = null;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_site);

        SITE_CLICADO = getIntent().getStringExtra("siteClicado");

        getSupportActionBar().setTitle("Voltar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        if( SITE_CLICADO == null ){
            SITE_CLICADO = "https://www.vagasdisponiveis.com";
        }

        webView = findViewById(R.id.web_view);


        webView.setWebViewClient( new MyBrowser() );
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                //setTitle("Carregando..."); //ESSA LINHA
                //MOSTRAR BARRA A TOOLBAR
                //getSupportActionBar().show();
                if (newProgress == 100){

                    //android:background="@android:color/transparent"

                    //carregando.setBackgroundColor(Color.TRANSPARENT);
                    //frameLayout2.setBackgroundColor(Color.TRANSPARENT);
                    //carregando.setTextColor(Color.TRANSPARENT);
                    //carregando.setBackgroundResource(R.color.transparent);
                    //frameLayout2.setBackgroundResource(R.color.transparent);
                    // carregando.setBackgroundResource(R.color.transparent);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        //CHECAR A CONEXAO DA INTERNET
        if( verificarConexao() ){
            webView.loadUrl(SITE_CLICADO);
        }else{
            Toast.makeText(AbrirSiteActivity.this, "Ative a sua internet para usar o Aplicativo", Toast.LENGTH_LONG).show();
        }

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        webView.loadUrl(SITE_CLICADO);


        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            //finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            //carregando.setVisibility(View.VISIBLE);
            return true; //super.shouldOverrideUrlLoading(view, url);
        }

        public boolean overrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private boolean verificarConexao(){
        boolean wifi = false;
        boolean movel = false;

        ConnectivityManager conexao = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = conexao.getAllNetworkInfo();

        for( NetworkInfo index : networkInfos){
            if(index.getTypeName().equalsIgnoreCase("WIFI")){
                if(index.isConnected()){
                    wifi = true;
                }
            }
            if(index.getTypeName().equalsIgnoreCase("MOBILE")){
                if(index.isConnected()){
                    movel = true;
                }
            }
        }
        return wifi || movel;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( (keyCode== KeyEvent.KEYCODE_BACK) && webView.canGoBack() ){
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
