package com.betharley.mobile.ecommerceonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProdutoDetalhesActivity extends AppCompatActivity {
    private ImageView detalhes_foto;
    private TextView detalhes_nome;
    private TextView detalhes_descricao;
    private TextView detalhes_preco;
    private TextView detalhes_preco_total;
    private ElegantNumberButton detalhes_button_elegant;
    private Button detalhes_adicionar_carrinho;
    private ProgressBar detalhes_progressBar;

    private Produto produto;
    private int quantidade = 0;
    private int totalResultado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_detalhes);

        //INICIAR COMPONENTES
        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            produto = (Produto) bundle.getSerializable("produto");
            mostrarDetalhes();

            detalhes_adicionar_carrinho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adicionarCarrinho();
                }
            });
        }

        detalhes_button_elegant.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularTotal();
            }
        });

        calcularTotal();
    }

    private void adicionarCarrinho(){
        /*.Carrinho
	        .IdUsuario		(dados+querEnviar)
		        .IdProduto(idProduto, idVendedor, idUsuario) 	Produto2.class
        * */
        detalhes_adicionar_carrinho.setEnabled( false );
        detalhes_progressBar.setVisibility( View.VISIBLE );

        produto.setQuantidade( quantidade );
        produto.setTotal( totalResultado );

        DatabaseReference usuarioLogado = Firebase.getDatabase()
                .child( "Usuarios" )
                .child( UsuarioFirebase.getIdUsuario() );

        usuarioLogado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue( Usuario.class );
                    produto.setUsuario( usuario );

                    if( produto.carrinho()){
                        mensagem("Produto Adicionado ao Seu Carrinho");
                        detalhes_adicionar_carrinho.setEnabled( true );
                        detalhes_progressBar.setVisibility( View.VISIBLE );
                        finish();
                    }else {
                        mensagem("Erro ao Adicionado ao Seu Carrinho");
                        detalhes_adicionar_carrinho.setEnabled( true );
                        detalhes_progressBar.setVisibility( View.VISIBLE );
                    }
                detalhes_progressBar.setVisibility( View.VISIBLE );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                detalhes_progressBar.setVisibility( View.VISIBLE );
            }
        });
    }

    private void calcularTotal(){
        quantidade = Integer.parseInt( detalhes_button_elegant.getNumber() );
        totalResultado = produto.getPreco() * quantidade;
        detalhes_preco_total.setText( "Qtd " + quantidade + ", Total: R$ " + totalResultado );
    }

    private void mostrarDetalhes(){

        detalhes_nome.setText( produto.getNome() );
        detalhes_descricao.setText( produto.getDescricao() );
        detalhes_preco.setText( "R$: " + String.valueOf( produto.getPreco() ) );

        if( produto.getFoto() != null ){
            Picasso.get().load( produto.getFoto() ).into( detalhes_foto );
        }
    }

    private void iniciarComponentes(){
        detalhes_foto = findViewById(R.id.detalhes_foto);
        detalhes_nome = findViewById(R.id.detalhes_nome);
        detalhes_descricao = findViewById(R.id.detalhes_descricao);
        detalhes_preco = findViewById(R.id.detalhes_preco);
        detalhes_preco_total = findViewById(R.id.detalhes_preco_total);
        detalhes_button_elegant = findViewById(R.id.detalhes_button_elegant);
        detalhes_adicionar_carrinho = findViewById(R.id.detalhes_adicionar_carrinho);
        detalhes_progressBar = findViewById(R.id.detalhes_progressBar2);
    }

    private void mensagem(String texto ){
        Toast.makeText( getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }
}
