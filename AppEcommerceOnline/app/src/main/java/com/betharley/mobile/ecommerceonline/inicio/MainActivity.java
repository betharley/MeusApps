package com.betharley.mobile.ecommerceonline.inicio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.HomeActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.inicio.CadastroActivity;
import com.betharley.mobile.ecommerceonline.inicio.LoginActivity;
import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {

    private Button mainLogar;
    private Button mainCadastrar;

    private ImageView main_google;
    private GoogleSignInClient googleSignInClient;
    private static final int GOOGLE = 15;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CONFIGURAÇÕES INICIAIS
        firebaseAuth = Firebase.getAutenticacao();

        logarUsuario();

        mainCadastrar = findViewById(R.id.main_cadastrar);
        mainLogar     = findViewById(R.id.main_logar);
        main_google   = findViewById(R.id.main_google);

        mainCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
                startActivity( intent );
            }
        });
        mainLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity( intent );
            }
        });

        main_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logarGoogle();
            }
        });

        servicosGoogle();
    }

    private void logarGoogle(){
        GoogleSignInAccount conta = GoogleSignIn.getLastSignedInAccount(this);

        if( conta == null ){
            Intent intent = googleSignInClient.getSignInIntent();
            startActivityForResult( intent, GOOGLE);
        }else{
            //Já exite alguém conectaro pela google
            //mensagem("Você já esta logado");
            startActivity( new Intent( getApplicationContext(), HomeActivity.class ) );
        }
    }
    private void adicionarGoogleFirebase( GoogleSignInAccount conta ){

        AuthCredential credential = GoogleAuthProvider.getCredential( conta.getIdToken(), null);
        firebaseAuth.signInWithCredential( credential )
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    startActivity( new Intent( getApplicationContext(), HomeActivity.class ) );
                    String idUsuario = UsuarioFirebase.getIdUsuario();
                    String emailGoogle = UsuarioFirebase.getUsuarioAtual().getEmail();

                    Usuario usuario = new Usuario("", emailGoogle, "");
                    usuario.setId( idUsuario );
                    usuario.atualizarLogin();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( RESULT_OK == resultCode && requestCode == GOOGLE  ){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );

            try {
                GoogleSignInAccount conta = task.getResult(ApiException.class);
                adicionarGoogleFirebase( conta );
            }catch (Exception e){
                mensagem( "Erro ao fazer login com o Google" );
                e.printStackTrace();
            }
        }
    }

    private void servicosGoogle(){
        GoogleSignInOptions gsm = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken( getString( R.string.default_web_client_id ) )
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient( this, gsm );
    }

    private void logarUsuario() {
        FirebaseAuth firebaseAuth = Firebase.getAutenticacao();

        if( firebaseAuth.getCurrentUser() != null ){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class );
            startActivity( intent );
            finish();
        }
    }

    private void mensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }
}
