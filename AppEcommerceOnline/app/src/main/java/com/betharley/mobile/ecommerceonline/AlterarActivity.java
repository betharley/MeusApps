package com.betharley.mobile.ecommerceonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlterarActivity extends AppCompatActivity {

    private String chave;
    private ImageView alterar_foto;
    private EditText alterar_nome;
    private EditText alterar_descricao;
    private EditText alterar_preco;
    private Button alterar_botao;
    private ProgressBar alterar_progress_bar;

    private Produto produto;
    private Usuario usuarioLogado;

    private Uri myUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        //CONFIGURAÇÕES INICIAIS
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //INICIAR COMPONENTES
        alterar_foto      = findViewById(R.id.alterar_foto_produto);
        alterar_nome      = findViewById(R.id.alterar_nome_produto);
        alterar_descricao = findViewById(R.id.alterar_descricao_produto);
        alterar_preco     = findViewById(R.id.alterar_preco_produto);
        alterar_botao     = findViewById(R.id.alterar_button);
        alterar_progress_bar = findViewById(R.id.alterar_progress_bar);

        alterar_botao.setEnabled( false );

        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            //chave = bundle.getString( "chave" );
            produto = (Produto) bundle.getSerializable("produto");
            mostrarProduto(produto);
        }else {
            mensagem( "Desculpe, erro de conexão" );
        }

        alterar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mensagem( "Desculpe, a foto não pode ser alterada" );
            }
        });

    }

    public void mostrarProduto(Produto produto){

        myUri = Uri.parse( produto.getFoto() );

        alterar_nome.setText( produto.getNome() );
        alterar_descricao.setText( produto.getDescricao() );
        alterar_preco.setText( String.valueOf( produto.getPreco() ) );

        if( produto.getFoto() != null ){
            Picasso.get().load( produto.getFoto() ).into( alterar_foto );
        }
        alterar_botao.setEnabled( true );

        /*DatabaseReference produtoRef = Firebase.getDatabase()
                .child( "Vendedores" )
                .child( usuarioLogado.getId() )
                .child( "Produtos" )
                .child( chave );

        produtoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.exists() ){
                    produto = dataSnapshot.getValue(Produto.class);
                    myUri = Uri.parse( produto.getFoto() );

                    alterar_nome.setText( produto.getNome() );
                    alterar_descricao.setText( produto.getDescricao() );
                    alterar_preco.setText( String.valueOf( produto.getPreco() ) );

                    if( produto.getFoto() != null ){
                        Picasso.get().load( produto.getFoto() ).into( alterar_foto );
                    }
                    alterar_botao.setEnabled( true );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

        alterar_botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarProduto();
            }
        });
    }

    private void validarProduto(){
        String nome = alterar_nome.getText().toString().trim();
        String descricao = alterar_descricao.getText().toString().trim();
        String preco = alterar_preco.getText().toString().trim();

        if(TextUtils.isEmpty(nome)){
            mensagem("Digite o Nome para o Produto");
        }else
        if(TextUtils.isEmpty(nome)){
            mensagem("Digite o Nome para o Produto");
        }else
        if( TextUtils.isEmpty(descricao)){
            mensagem( "Digite a Descrição do Produto" );
        }else
        if( TextUtils.isEmpty(preco)){
            mensagem( "Digite o Preço do Produto" );
        }else {
            produto.setNome( nome );
            produto.setDescricao( descricao );
            produto.setPreco( Integer.parseInt( preco ));
            atualizarProduto();
        }
    }

    private void atualizarProduto(){
        alterar_progress_bar.setVisibility( View.VISIBLE );
        alterar_botao.setEnabled( false );

        if( produto.remover() ){
            Calendar calendar = Calendar.getInstance();

            final SimpleDateFormat data = new SimpleDateFormat("dd, MMM yyyy"); // "dd/MM/yyyy"
            produto.setData( data.format(calendar.getTime()) );

            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
            produto.setHora( time.format(calendar.getTime()) );

            produto.setId( produto.getData() + "  " + produto.getHora() );

            if( produto.alterar() ){
                alterar_progress_bar.setVisibility( View.INVISIBLE );
                alterar_botao.setEnabled( true );
                finish();
                mensagem("Produto Atualizado com Sucesso...");
            }else {
                alterar_progress_bar.setVisibility( View.INVISIBLE );
                alterar_botao.setEnabled( true );
                mensagem("Erro ao Atualizar Produto, Tente Novamente");
            }
        }

    }

    private void mensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }
}
