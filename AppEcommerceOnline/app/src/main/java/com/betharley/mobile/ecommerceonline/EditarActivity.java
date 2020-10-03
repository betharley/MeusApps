package com.betharley.mobile.ecommerceonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class EditarActivity extends AppCompatActivity {
    private ImageView editar_foto;
    private TextView editar_nome;
    private TextView editar_descricao;
    private TextView editar_preco;
    private TextView editar_preco_total;
    private ElegantNumberButton editar_button_elegant;
    private Button editar_adicionar_carrinho;
    private ProgressBar editar_progressBar;

    private Produto produto;
    private int quantidade = 0;
    private int totalResultado = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        //INICIAR COMPONENTES
        iniciarComponentes();

        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            produto = (Produto) bundle.getSerializable("produto");
            mostrarDetalhes();

            editar_adicionar_carrinho.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adicionarCarrinho();
                }
            });
        }

        editar_button_elegant.setOnClickListener(new ElegantNumberButton.OnClickListener() {
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
        editar_adicionar_carrinho.setEnabled( false );
        editar_progressBar.setVisibility( View.VISIBLE );

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
                Log.i( "USUARIO LOGADO ", usuario.getId() + " /////////////////" + usuario.toString() );
                if( produto.atualizarCarrinho()){
                    mensagem("Produto Alterado Com Sucesso no Carrinho");
                    editar_adicionar_carrinho.setEnabled( true );
                    editar_progressBar.setVisibility( View.VISIBLE );
                    finish();
                }else {
                    mensagem("Erro ao Editar Produto do Carrinho");
                    editar_adicionar_carrinho.setEnabled( true );
                    editar_progressBar.setVisibility( View.VISIBLE );
                }
                editar_progressBar.setVisibility( View.VISIBLE );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                editar_progressBar.setVisibility( View.VISIBLE );
            }
        });
    }
    private void calcularTotal(){
        quantidade = Integer.parseInt( editar_button_elegant.getNumber() );
        totalResultado = produto.getPreco() * quantidade;
        editar_preco_total.setText( "Qtd " + quantidade + ", Total: R$ " + totalResultado );
    }

    private void mostrarDetalhes(){

        editar_nome.setText( produto.getNome() );
        editar_descricao.setText( produto.getDescricao() );
        editar_preco.setText( "R$: " + String.valueOf( produto.getPreco() ) );

        if( produto.getFoto() != null ){
            Picasso.get().load( produto.getFoto() ).into( editar_foto );
        }
    }

    private void iniciarComponentes(){
        editar_foto = findViewById(R.id.editar_foto);
        editar_nome = findViewById(R.id.editar_nome);
        editar_descricao = findViewById(R.id.editar_descricao);
        editar_preco = findViewById(R.id.editar_preco);
        editar_preco_total = findViewById(R.id.editar_preco_total);
        editar_button_elegant = findViewById(R.id.editar_button_elegant);
        editar_adicionar_carrinho = findViewById(R.id.editar_adicionar_carrinho);
        editar_progressBar = findViewById(R.id.editar_progressBar2);
    }

    private void mensagem(String texto ){
        Toast.makeText( getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }
}
