package com.betharley.mobile.appprofissoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetalhesActivity extends AppCompatActivity {
    private Toolbar toolbarDetalhes;
    private String profissaoEspecifica = null;
    private String profissaoArea = null;

    //public String JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=";
    //holder.webView.loadData(parteHtml, "text/html", "UTF-8");

    //private TextView detalhes_nome_profissao;
    private ImageView detalhes_imagem;
    private WebView webView_detalhes;

    private DatabaseReference databaseReference;
    private String conteudoProfissao = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        toolbarDetalhes = findViewById(R.id.toolbarDetalhes);
        setSupportActionBar( toolbarDetalhes );
        getSupportActionBar().setTitle("Detalhes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        profissaoEspecifica = getIntent().getStringExtra("profissaoEspecifica");
        profissaoArea = getIntent().getStringExtra("profissaoArea");

        Toast.makeText(DetalhesActivity.this, profissaoArea+"\n   ....    \n"+profissaoEspecifica, Toast.LENGTH_SHORT).show();;

        //detalhes_nome_profissao = findViewById(R.id.detalhes_nome_profissao);
        detalhes_imagem = findViewById(R.id.detalhes_imagem);
        webView_detalhes = findViewById(R.id.webView_detalhes);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //detalhes_nome_profissao.setText( profissaoEspecifica );

        carregarProfissao();

    }

    private void carregarProfissao(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CONTEUDOS")
                .child(profissaoArea).child(profissaoEspecifica);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        conteudoProfissao = String.valueOf( dataSnapshot.getValue() );

                        webView_detalhes.loadData(conteudoProfissao, "text/html", "UTF-8");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        DatabaseReference imagemRef = FirebaseDatabase.getInstance().getReference()
                .child("URL_IMAGENS")
                .child(profissaoArea).child(profissaoEspecifica);//.child("urlImagem");

        imagemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String urlImagem = String.valueOf( dataSnapshot.getValue() );
                Log.i("CAMINHO DA IMAGEM", ""+urlImagem);

                Glide.with( DetalhesActivity.this)
                        .load( urlImagem )
                        .into( detalhes_imagem );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
        //return super.onSupportNavigateUp();
    }
}
