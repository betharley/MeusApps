package com.betharley.wesleycatula.mobile.appemprego.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.betharley.wesleycatula.mobile.appemprego.model.Postagem;
import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.model.Vaga;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

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

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }*/
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
/*

public class VisualizarActivity extends AppCompatActivity {
    private WebView webView_visualizar;

    private Vaga vagaClicada;

    private CircleImageView ver_imagem_empresa;
    private TextView ver_nome_empresa;
    private TextView ver_titulo;
    private TextView ver_link_vaga;
    private TextView ver_localidade;
    private TextView ver_data_hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar);

        //TOOLBAR
        getSupportActionBar().setTitle("Detalhes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            vagaClicada = (Vaga) bundle.getSerializable("vagaClicada");

            mostrarConteudo();
        }
    }

    private void mostrarConteudo() {
        String parteHtml = vagaClicada.getDescription();
        webView_visualizar.loadData(parteHtml, "text/html", "UTF-8");


        ver_nome_empresa.setText( vagaClicada.getCompany() );
        ver_titulo.setText( vagaClicada.getJobtitle() );
        //ver_link_vaga.setText( vagaClicada.getUrl() );
        ver_localidade.setText( vagaClicada.getFormattedLocation() );
        ver_data_hora.setText( vagaClicada.getDate() );
        //String data = postagem.getData().substring(0, 10);
        //data = data.replace("-", "/");
        //ver_data.setText( "Por Wesley Morais, " + data );

        //GLIDE
        Picasso.get( )
                .load( vagaClicada.getUrl() )
                .placeholder(R.drawable.emprego_centro )
                .into( ver_imagem_empresa );
    }

    private void iniciarComponentes(){
        webView_visualizar = findViewById(R.id.webView_visualizar);

        ver_nome_empresa = findViewById(R.id.ver_nome_empresa);
        ver_titulo = findViewById(R.id.ver_titulo);
        ver_link_vaga = findViewById(R.id.ver_link_vaga);
        ver_localidade = findViewById(R.id.ver_localidade);
        ver_data_hora = findViewById(R.id.ver_data_hora);

        ver_imagem_empresa = findViewById(R.id.ver_imagem_empresa);

    }

*/
/*    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }*//*

}
*/
