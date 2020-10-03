package com.betharley.mobile.ecommerceonline.inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.HomeActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import io.paperdb.Paper;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText edit_nome;
    private TextInputEditText edit_email;
    private TextInputEditText edit_senha;
    private TextInputEditText edit_confirmarSenha;
    private Button botao_logar;
    private ProgressBar cadastro_progressBar2;

    private Usuario usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //CONFIGURAÇÕES INICIAIS
        Paper.init(this);

        edit_nome = findViewById(R.id.cadastrar_nome);
        edit_email = findViewById(R.id.cadastrar_email);
        edit_senha = findViewById(R.id.cadastrar_senha);
        edit_confirmarSenha = findViewById(R.id.cadastrar_confirmar_senha);
        botao_logar = findViewById(R.id.cadastrar_botao);
        cadastro_progressBar2 = findViewById(R.id.cadastro_progressBar2);

        botao_logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDados();
            }
        });
    }

    private void validarDados(){
        String nome = edit_nome.getText().toString().trim();
        String email = edit_email.getText().toString().replace(" ", "");
        //email = email.replace(" ", "");
        String senha = edit_senha.getText().toString().replace(" ", "");
        String confirmarSenha = edit_confirmarSenha.getText().toString().replace(" ", "");

        if( TextUtils.isEmpty(nome) ){
            mensagem("Digite o seu nome");
        }else
        if (TextUtils.isEmpty(email)){
            mensagem("Digite o seu email");
        }else
        if (TextUtils.isEmpty(senha) &&  senha.length() <= 5){
            mensagem("Digite a seu senha de no minimo 6 caracteres");
        }else
        if (TextUtils.isEmpty(confirmarSenha) &&  confirmarSenha.length() <= 5){
            mensagem("Confirme a sua senha");
        }else {
            if( senha.equals(confirmarSenha) ){
                usuario = new Usuario(nome, email, senha);
                cadastrarUsuario(usuario);
            }else {
                mensagem("A senha não confere, digite novamente");
            }
        }
    }

    private void cadastrarUsuario(final Usuario usuario){
        cadastro_progressBar2.setVisibility( View.VISIBLE );
        botao_logar.setEnabled( false );

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(
            usuario.getEmail(), usuario.getSenha())
            .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if( task.isSuccessful() ){
                        botao_logar.setEnabled( true );
                        cadastro_progressBar2.setVisibility(View.INVISIBLE);
                        mensagem("Bem vindo, cadastro realizar com sucesso " + usuario.getNome());

                        usuario.setId( task.getResult().getUser().getUid() );
                        usuario.atualizar();
                        Paper.book().write("usuario", usuario );
                        UsuarioFirebase.atualizarNomeUsuario( usuario.getNome() );

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity( intent );
                        finish();
                    }else {
                        String excessao = "";
                        try{
                            throw task.getException();
                        }catch ( FirebaseAuthWeakPasswordException e){
                            excessao = "Digite uma senha mais forte";
                        }catch( FirebaseAuthInvalidCredentialsException e){
                            excessao = "Por favor, Digite um email valido";
                        }catch( FirebaseAuthUserCollisionException e){
                            excessao = "Esse conta já foi cadastrada";
                        }catch( Exception e){
                            excessao = "Erro ao cadastrar o usuário "+ usuario.getNome();
                            e.printStackTrace();
                        }
                        if( task.getException().toString().equalsIgnoreCase( "interrupted connection" )){
                            mensagem("Sem Conexão Com a Internet");
                        }else {
                            mensagem(excessao);
                        }
                        botao_logar.setEnabled( true );
                        cadastro_progressBar2.setVisibility(View.INVISIBLE);
                    }
                }
        });

    }

    private void mensagem(String texto){
        Toast toast = Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_SHORT);
        toast.show();
    }
}
