package com.betharley.catempregos.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.betharley.catempregos.R;
import com.betharley.catempregos.activity.HomeActivity;
import com.betharley.catempregos.activity.LoginActivity;
import com.betharley.catempregos.helpe.SetupFirebase;
import com.betharley.catempregos.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private TextInputEditText loginEmail, loginSenha;
    private Button loginBtn;
    private ProgressBar progressBar;
    private Usuario usuario = new Usuario();
    private FirebaseAuth autenticacao = SetupFirebase.getAutenticacao();

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginEmail  = view.findViewById(R.id.login_fragment_email);
        loginSenha  = view.findViewById(R.id.login_fragment_senha);
        loginBtn    = view.findViewById(R.id.login_fragment_logar);
        progressBar = view.findViewById(R.id.login_fragment_progress_bar);


        progressBar.setVisibility(View.INVISIBLE);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = loginEmail.getText().toString();
                String senha = loginSenha.getText().toString();

                if( email.isEmpty() ){
                    Toast.makeText(getActivity(),
                            "Preencha o email - login_fragment",
                            Toast.LENGTH_SHORT).show();
                }else if( senha.isEmpty() ){
                    Toast.makeText(getActivity(),
                            "Preencha a senha - login_fragment",
                            Toast.LENGTH_SHORT).show();
                }else{
                    usuario.setEmail( email );
                    usuario.setSenha( senha );
                    validar();
                }
            }
        });

        return view;
    }
    private void validar(){
        progressBar.setVisibility(View.VISIBLE);

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    Toast.makeText(getActivity(),
                            "LOGIN SUCESSO - login_fragment",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity( new Intent(getActivity(), HomeActivity.class));

                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(),
                            "LOGIN ERRO - login_fragment",
                            Toast.LENGTH_SHORT).show();
                    //APAGAR ESSA PARTE DEPOIS
                    startActivity( new Intent(getActivity(), HomeActivity.class));

                }
            }
        });
    }
}
