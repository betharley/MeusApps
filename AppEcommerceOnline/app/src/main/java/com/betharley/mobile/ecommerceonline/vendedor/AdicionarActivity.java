package com.betharley.mobile.ecommerceonline.vendedor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.HomeActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.betharley.mobile.ecommerceonline.model.Vendedor;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdicionarActivity extends AppCompatActivity {

    private ImageView novo_foto;
    private ProgressBar novo_progress_bar;
    private EditText novo_nome;
    private EditText novo_descricao;
    private EditText novo_preco;
    private Button novo_button;

    private Uri imageUri;
    private String myUri;
    private Produto produto;
    private Vendedor vendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);

        //CONFIGURAÇÕES INICIAIS
        Paper.init( this );

        //INICIAR COMPONENTES
        novo_foto   = findViewById(R.id.novo_foto_produto);
        novo_progress_bar = findViewById(R.id.novo_progress_bar);
        novo_nome   = findViewById(R.id.novo_nome_produto);
        novo_descricao   = findViewById(R.id.novo_descricao_produto);
        novo_preco   = findViewById(R.id.novo_preco_produto);
        novo_button  = findViewById(R.id.novo_button);

        novo_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity(imageUri)
                        .setAspectRatio(10, 8)
                        //.setAspectRatio(5, 3)
                        .getIntent( getApplicationContext() );
                startActivityForResult( intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE );
            }
        });

        novo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarProduto();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null ){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            novo_foto.setImageURI( imageUri );
        }
    }

    private void validarProduto(){
        String nome = novo_nome.getText().toString().trim();
        String descricao = novo_descricao.getText().toString().trim();
        String preco = novo_preco.getText().toString().trim();

        if( imageUri == null ){
            mensagem("Selecione Uma Imagem Para o Produto");
        }else
        if ( TextUtils.isEmpty( nome )){
            mensagem("Digite o Nome do Produto");
        }else
        if( TextUtils.isEmpty( descricao )){
            mensagem( "Digite a Descrição do Produto" );
        }else
        if( TextUtils.isEmpty( preco )){
            mensagem( "Digite o Preço do Produto" );
        }else {
            produto = new Produto();
            produto.setFoto( String.valueOf( imageUri ) );
            produto.setNome( nome );
            produto.setDescricao( descricao );
            produto.setPreco( Integer.parseInt( preco ) );
            recuperarVendedor();

        }
    }
    private void recuperarVendedor(){
        novo_progress_bar.setVisibility( View.VISIBLE );
        novo_button.setEnabled( false );
        /*if( vendedor.salvar() ){
            mensagem("Conta do Vendedor " + vendedor.getNome() + "Criado com Sucesso!\nSalvando produto");
            salvarProduto();
        }*/

        //RECUPERA O VENDEDOR NO DATABASE
        final DatabaseReference databaseReference = Firebase.getDatabase()
                .child("Usuarios")
                .child( UsuarioFirebase.getIdUsuario() );

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                vendedor = new Vendedor();
                vendedor.setId( usuario.getId() );
                vendedor.setNome( usuario.getNome() );
                vendedor.setEmail( usuario.getEmail() );
                vendedor.setFoto( usuario.getFoto() );
                vendedor.setTelefone( usuario.getTelefone() );
                vendedor.setEndereco( usuario.getEndereco() );

                produto.setVendedor( vendedor );
                salvarProduto();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                novo_progress_bar.setVisibility( View.INVISIBLE );
                novo_button.setEnabled( true );
            }
        });
        //mensagem( "recuperarVendedor" );

    }


    private void salvarProduto(){
        Calendar calendar = Calendar.getInstance();

        final SimpleDateFormat data = new SimpleDateFormat("dd, MMM yyyy"); // "dd/MM/yyyy"
        produto.setData( data.format(calendar.getTime()) );

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
        produto.setHora( time.format(calendar.getTime()) );

        produto.setId( produto.getData() + "  " + produto.getHora() );

        final StorageReference fileRef = Firebase.getStorage()
                .child( vendedor.getId() )
                .child( "Produtos" )
                .child( produto.getId()+ ".jpg" );

        StorageTask uploadTask = fileRef.putFile( imageUri );
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if( !task.isSuccessful() ){
                    throw  task.getException();
                }
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if( task.isSuccessful() ){
                    Uri downloadUrl = (Uri) task.getResult();
                    myUri = downloadUrl.toString();

                    /*DatabaseReference produtoRef = Firebase.getDatabase()
                            .child("Vendedores")
                            .child( vendedor.getId() )
                            .child( produto.getId() );*/

                    //HashMap<String, Object> mapa = new HashMap<>();
                    //mapa.put("foto", myUri);
                    produto.setFoto( myUri );
                    if( produto.salvar() ){
                        mensagem( "Produto Adicionado a Sua Lista" );
                        novo_progress_bar.setVisibility( View.INVISIBLE );
                        novo_button.setEnabled( true );
                        Intent intentLista = new Intent(getApplicationContext(), ListaVendedorActivity.class);
                        startActivity( intentLista );
                        finish();
                    }

                }
            }
        });

    }

    private void mensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT).show();
    }
}
