package com.betharley.mobile.ecommerceonline.inicio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetarSenhaActivity extends AppCompatActivity {

    private TextInputEditText recuperar_email;
    private Button recuperar_botao;
    private ProgressBar recuperar_progressBar2;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha);

        //INICIAR COMPONENTES
        recuperar_email = findViewById(R.id.recuperar_email);
        recuperar_botao = findViewById(R.id.recuperar_botao);
        recuperar_progressBar2 = findViewById(R.id.recuperar_progressBar2);

        recuperar_botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarEmail();
            }
        });
    }

    private void validarEmail(){
        String email = recuperar_email.getText().toString().replace(" ", "");

        if ( TextUtils.isEmpty( email ) ){
            mensagem("Digite o seu emal acima");
        }else {
            enviarEmail(email);
        }
    }

    private void enviarEmail(String email) {
        firebaseAuth = Firebase.getAutenticacao();

        firebaseAuth.sendPasswordResetEmail( email )
            .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //mensagem("Enviamos uma Mensagem no seu Email para Redefinir sua Senha.");
                mostrarMensagem();
            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mensagem("Erro ao Enviar Email. Verifique se Digitou seu Email Corretamente.");
            }
        });
    }

    private void mostrarMensagem() {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "Email Enviado" );
        alert.setMessage("Enviamos uma Mensagem no seu Email para Redefinir sua Senha do Aplicativo E-Commerce.");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        alert.show();
    }


    private void mensagem(String texto){
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }
}
