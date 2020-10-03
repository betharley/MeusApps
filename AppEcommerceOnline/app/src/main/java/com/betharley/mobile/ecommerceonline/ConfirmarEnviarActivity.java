package com.betharley.mobile.ecommerceonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfirmarEnviarActivity extends AppCompatActivity {
    private EditText enviar_nome;
    private EditText enviar_telefone;
    private EditText enviar_enredeco;
    private Button enviar_botao;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_enviar);

        //INICIAR COMPONENTES
        enviar_nome     = findViewById(R.id.enviar_nome);
        enviar_telefone = findViewById(R.id.enviar_telefone);
        enviar_enredeco = findViewById(R.id.enviar_endereco);
        enviar_botao    = findViewById(R.id.enviar_botao);

        Bundle bundle = getIntent().getExtras();

        if( bundle != null ){
            produto = (Produto) bundle.getSerializable("produto");
            mostrarDados();

            enviar_botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validarDados();
                }
            });
        }
    }

    private void mostrarDados(){
        enviar_nome.setText( produto.getUsuario().getNome() );
        enviar_telefone.setText( produto.getUsuario().getTelefone() );
        enviar_enredeco.setText( produto.getUsuario().getEndereco() );
    }


    private void validarDados(){
        String nome     = enviar_nome.getText().toString().trim();
        String telefone = enviar_telefone.getText().toString().trim();
        String endereco = enviar_enredeco.getText().toString().trim();

        if( TextUtils.isEmpty(nome) || nome.isEmpty() || nome.equals("") ){
            mensagem("Por favor escreva seu nome completo");
        }else
        if( TextUtils.isEmpty(telefone) || telefone.isEmpty() || telefone.equals("")){
            mensagem("Por favor, forneça o seu numero de telefone");
        }else
        if( TextUtils.isEmpty(endereco) || endereco.isEmpty() || endereco.equals("")){
            mensagem("Por favor, Descreva o Local de Entrega e Horário");
        }else {
            produto.getUsuario().setNome( nome );
            produto.getUsuario().setTelefone( telefone );
            produto.getUsuario().setEndereco( endereco );
            confirmarEnvio();
        }
    }

    private void confirmarEnvio(){
        String currentData, currentTime;
        final Calendar calendar = Calendar.getInstance();

        SimpleDateFormat data = new SimpleDateFormat("dd, MMM yyyy");
        currentData = data.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        currentTime = time.format(calendar.getTime());

        produto.setData( currentData );
        produto.setHora( currentTime );
        produto.setStatus( "Enviado" );

        DatabaseReference pedidosRef = Firebase.getDatabase()
                .child( "Pedidos" )
                .child( produto.getUsuario().getId() )
                .child( produto.getId() );

        pedidosRef.setValue( produto ) // CRIA O NO DE PEDIDOS
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if ( task.isSuccessful() ){

                            DatabaseReference enviadosRef = Firebase.getDatabase()
                                    .child( "Enviados" )
                                    .child( produto.getVendedor().getId() )
                                    .child( produto.getId() );

                            enviadosRef.setValue( produto ) // CRIA O NO DE ENVIADOS
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if( task.isSuccessful() ){

                                                DatabaseReference removerRef = Firebase.getDatabase()
                                                        .child( "Carrinho" )
                                                        .child( produto.getUsuario().getId() )
                                                        .child( produto.getId() );

                                                removerRef.removeValue() // REMOVE O PROGUTO DO CARRINHO
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                mensagem("Seu pedido foi enviado com sucesso");
                                                                finish();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void mensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }
}
