package betharley.catempregos.com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import betharley.catempregos.com.R;
import betharley.catempregos.com.helper.SetupFirebase;
import betharley.catempregos.com.model.Usuario;


public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText cadastroNome, cadastroSenha, cadastroEmail;
    private Button cadastrarBtn;
    private ProgressBar progressBar;
    private Usuario usuario;

    private FirebaseAuth autenticacao = SetupFirebase.getAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        iniciarComponentes();

        progressBar.setVisibility(View.INVISIBLE);
        cadastrarBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String nome = cadastroNome.getText().toString();
                String email = cadastroEmail.getText().toString();
                String senha = cadastroSenha.getText().toString();

                if( nome.isEmpty() ){
                    Toast.makeText(RegistroActivity.this,
                            "Preencha o nome",
                            Toast.LENGTH_SHORT).show();
                }else if( email.isEmpty() ){
                    Toast.makeText(RegistroActivity.this,
                            "Preencha o email",
                            Toast.LENGTH_SHORT).show();
                }else if( senha.isEmpty() ){
                    Toast.makeText(RegistroActivity.this,
                            "Preencha a senha",
                            Toast.LENGTH_SHORT).show();
                }else{
                    usuario = new Usuario();
                    usuario.setNome( nome );
                    usuario.setEmail( email );
                    usuario.setSenha( senha );

                    cadastrar( );

                }

            }
        });
    }

    private void cadastrar() {
        progressBar.setVisibility(View.VISIBLE);

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ){
                            progressBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(RegistroActivity.this,
                                    "Cadastro realizado com sucesso",
                                    Toast.LENGTH_SHORT).show();
                            startActivity( new Intent(getApplicationContext(), AnunciosActivity.class) );
                            finish();

                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            String erro = "";

                            try{
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                erro = " 1 Digite uma senha mais forte!";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erro = " 2 Por favor, digite um e-mail válido";
                            }catch (FirebaseAuthUserCollisionException e){
                                erro = " 3 Esta conta já foi cadastrada";
                            }catch (Exception e){
                                erro = " 4 ao cadastrar usuário " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(RegistroActivity.this,
                                    "Erro " + erro,
                                    Toast.LENGTH_LONG).show();

                            Log.i("Erro", " // "+ erro);
                            startActivity( new Intent(getApplicationContext(), AnunciosActivity.class));
                        }
                    }
                }
        );
    }

    private void iniciarComponentes() {
        cadastroNome    = findViewById(R.id.cadastro_nome);
        cadastroEmail   = findViewById(R.id.cadastro_email);
        cadastroSenha   = findViewById(R.id.cadastro_senha);
        cadastrarBtn    = findViewById(R.id.cadastro_cadastrar);
        progressBar     = findViewById(R.id.cadastro_progress_bar);
    }


}