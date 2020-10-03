package com.betharley.mobile.wesleycatula.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.betharley.mobile.wesleycatula.R;
import com.betharley.mobile.wesleycatula.model.Postagem;
import com.bumptech.glide.Glide;

public class VisualizarActivity extends AppCompatActivity {
    private WebView webView_visualizar;

    private Postagem postagem;

    private ImageView ver_imagem;
    private TextView ver_title;
    private TextView ver_destaque;
    private TextView ver_conteudo;
    private TextView ver_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        //TOOLBAR
        getSupportActionBar().setTitle("Detalhes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.icone_star_black_24dp);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            postagem = (Postagem) bundle.getSerializable("postagem");

            mostrarConteudo();
        }
    }

    private void mostrarConteudo() {
        String parteHtml = postagem.getContent();
        webView_visualizar.loadData(parteHtml, "text/html", "UTF-8");


        ver_title.setText( postagem.getTitle() );
        ver_conteudo.setText( postagem.getContent() );
        ver_destaque.setText( postagem.getExcerpt() );
        String data = postagem.getData().substring(0, 10);
        data = data.replace("-", "/");
        ver_data.setText( "Por Wesley Morais, " + data );

        //GLIDE
        Glide.with( VisualizarActivity.this )
                .load( postagem.getSource_url() )
                .into( ver_imagem );
    }

    private void iniciarComponentes(){
        webView_visualizar = findViewById(R.id.webView_visualizar);

        ver_title = findViewById(R.id.ver_title);
        ver_destaque = findViewById(R.id.ver_destaque);
        ver_conteudo = findViewById(R.id.ver_conteudo);
        ver_data = findViewById(R.id.ver_data);
        ver_imagem = findViewById(R.id.ver_imagem);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
