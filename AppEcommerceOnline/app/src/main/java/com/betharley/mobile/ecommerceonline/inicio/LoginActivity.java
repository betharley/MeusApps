package com.betharley.mobile.ecommerceonline.inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.HomeActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {
    private TextView login_fogout_senha;
    private TextInputEditText edit_email;
    private TextInputEditText edit_senha;
    private Button logarBotao;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = findViewById(R.id.login_email);
        edit_senha = findViewById(R.id.login_senha);
        logarBotao = findViewById(R.id.login_botao);
        progressBar = findViewById(R.id.login_progressBar2);
        login_fogout_senha = findViewById(R.id.login_fogout_senha);

        logarBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDados();
            }
        });

        login_fogout_senha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ResetarSenhaActivity.class);
                startActivity( intent );
            }
        });
    }

    private void validarDados(){
        String email = edit_email.getText().toString().replaceAll(" ", "");
        String senha = edit_senha.getText().toString().replace(" ", "");

        if(TextUtils.isEmpty(email)){
            mensagem("Digite seu email");
        }else
        if(TextUtils.isEmpty(senha) &&  senha.length() <= 5){
            mensagem("Digite a sua senha. A senha deve ter no minimo 6 caracteres");
        }else{
            logarUsuario(email, senha);
        }
    }
    private void logarUsuario(String email, String senha){
        firebaseAuth = Firebase.getAutenticacao();
        progressBar.setVisibility(View.VISIBLE);
        logarBotao.setEnabled(false);

        firebaseAuth.signInWithEmailAndPassword( email, senha )
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ){
                            progressBar.setVisibility(View.INVISIBLE);
                            logarBotao.setEnabled(true);

                            mensagem("Bem vindo, login realizar com sucesso.");
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity( intent );
                            finish();
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            logarBotao.setEnabled(true);
                            String excessao = "";
                            try{
                                throw task.getException();
                            }catch ( FirebaseAuthInvalidCredentialsException e){
                                excessao = "Email e senha não corresponde a um usuario cadastrado";
                            }catch ( FirebaseAuthInvalidUserException e){
                                excessao = "Usuario não esta cadastrado";
                            }catch ( Exception e){
                                excessao = "Erro ao fazer login do usuario ";
                                e.printStackTrace();
                            }
                            if( task.getException().toString().equalsIgnoreCase( "interrupted connection" )){
                                mensagem("Sem Conexão Com a Internet");
                            }else {
                                mensagem(excessao);
                            }
                        }
                    }
                });
    }

    private void mensagem(String texto){
        Toast toast = Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT);
        toast.show();
    }
}
