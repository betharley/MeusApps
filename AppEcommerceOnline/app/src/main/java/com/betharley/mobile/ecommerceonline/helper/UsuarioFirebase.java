package com.betharley.mobile.ecommerceonline.helper;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {
    private static FirebaseUser firebaseUser;
    private static String usuarioFoto;

    public static FirebaseUser getUsuarioAtual(){
        firebaseUser = Firebase.getAutenticacao().getCurrentUser();
        return firebaseUser;
    }

    public static String getIdUsuario(){
        FirebaseUser firebaseUser = getUsuarioAtual();
         usuarioFoto = firebaseUser.getUid();
         return usuarioFoto;
    }


    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setNome( firebaseUser.getDisplayName() );
        usuario.setEmail( firebaseUser.getEmail() );
        usuario.setId( firebaseUser.getUid() );

        if( firebaseUser.getPhotoUrl() == null ){
            usuario.setFoto( "" );
        }else{
            usuario.setFoto( firebaseUser.getPhotoUrl().toString() );
        }
        return usuario;
    }

    //Atualiza a foto do usuario
    public static boolean atualizarFotoUsuario(Uri url){

        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri( url )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){

                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    //Atualiza o nome do usuario
    public static boolean atualizarNomeUsuario(String nome){

        try{
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( nome )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){

                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
