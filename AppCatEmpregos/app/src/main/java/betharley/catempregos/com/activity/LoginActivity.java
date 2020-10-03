package betharley.catempregos.com.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import betharley.catempregos.com.R;
import betharley.catempregos.com.helper.SetupFirebase;
import betharley.catempregos.com.model.Usuario;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginEmail, loginSenha;
    private Button loginBtn;
    private ProgressBar progressBar;
    private Usuario usuario = new Usuario();
    private FirebaseAuth autenticacao = SetupFirebase.getAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toobar
        getSupportActionBar().setTitle("Logar Usúario");

        //APAGAR DEPOIS
        //startActivity( new Intent(LoginActivity.this, AnunciosActivity.class));

        iniciarComponentes();

        logado();

        progressBar.setVisibility(View.INVISIBLE);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = loginEmail.getText().toString();
                String senha = loginSenha.getText().toString();

                if( email.isEmpty() ){
                    Toast.makeText(LoginActivity.this,
                            "Preencha o email",
                            Toast.LENGTH_SHORT).show();
                }else if( senha.isEmpty() ){
                    Toast.makeText(LoginActivity.this,
                            "Preencha a senha",
                            Toast.LENGTH_SHORT).show();
                }else{
                    usuario.setEmail( email );
                    usuario.setSenha( senha );
                    validar();
                }
            }
        });
    }

    private void logado(){
        if( autenticacao.getCurrentUser() != null){
            startActivity( new Intent(LoginActivity.this, AnunciosActivity.class));
            finish();
        }
    }

    private void iniciarComponentes() {
        loginEmail  = findViewById(R.id.login_email);
        loginSenha  = findViewById(R.id.login_senha);
        loginBtn    = findViewById(R.id.login_logar);
        progressBar = findViewById(R.id.login_progress_bar);

    }

    public void cadastrar(View view){
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity( intent );
    }

    private void validar(){
        progressBar.setVisibility(View.VISIBLE);

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    Toast.makeText(LoginActivity.this,
                            "LOGIN SUCESSO",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity( new Intent(LoginActivity.this, AnunciosActivity.class));
                    finish();
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this,
                            "LOGIN ERRO " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                    //APAGAR ESSA PARTE DEPOIS
                    startActivity( new Intent(LoginActivity.this, AnunciosActivity.class));
                    finish();
                }
            }
        });
    }
}
