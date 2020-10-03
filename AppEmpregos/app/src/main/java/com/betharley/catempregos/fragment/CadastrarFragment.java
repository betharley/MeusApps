package com.betharley.catempregos.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betharley.catempregos.R;
import com.betharley.catempregos.activity.CadastroActivity;
import com.betharley.catempregos.activity.HomeActivity;
import com.betharley.catempregos.helpe.SetupFirebase;
import com.betharley.catempregos.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarFragment extends Fragment {

    private TextInputEditText cadastroNome, cadastroSenha, cadastroEmail;
    private Button cadastrarBtn;
    private ProgressBar progressBar;
    private Usuario usuario;

    private FirebaseAuth autenticacao = SetupFirebase.getAutenticacao();

    public CadastrarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cadastrar, container, false);

        cadastroNome    = view.findViewById(R.id.cadastro_nome);
        cadastroEmail   = view.findViewById(R.id.cadastro_email);
        cadastroSenha   = view.findViewById(R.id.cadastro_senha);
        cadastrarBtn    = view.findViewById(R.id.cadastro_cadastrar);
        progressBar     = view.findViewById(R.id.cadastro_progress_bar);


        progressBar.setVisibility(View.INVISIBLE);
        cadastrarBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String nome = cadastroNome.getText().toString();
                String email = cadastroEmail.getText().toString();
                String senha = cadastroSenha.getText().toString();

                if( nome.isEmpty() ){
                    Toast.makeText(getActivity(),
                            "Preencha o nome",
                            Toast.LENGTH_SHORT).show();
                }else if( email.isEmpty() ){
                    Toast.makeText(getActivity(),
                            "Preencha o email",
                            Toast.LENGTH_SHORT).show();
                }else if( senha.isEmpty() ){
                    Toast.makeText(getActivity(),
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

        return view;
    }

    private void cadastrar() {
        progressBar.setVisibility(View.VISIBLE);

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(
                getActivity(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);

                            Toast.makeText(getActivity(),
                                    "Cadastro realizado com sucesso",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), HomeActivity.class));


                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            String erro = "";

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = " 1 Digite uma senha mais forte!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = " 2 Por favor, digite um e-mail válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = " 3 Esta conta já foi cadastrada";
                            } catch (Exception e) {
                                erro = " 4 ao cadastrar usuário " + e.getMessage();
                                e.printStackTrace();
                            }

                            Toast.makeText(getActivity(),
                                    "Erro " + erro,
                                    Toast.LENGTH_LONG).show();

                            Log.i("Erro", " // " + erro);
                        }
                    }
                }
        );
    }
}
