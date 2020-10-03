package com.betharley.mobile.ecommerceonline.fragment;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.HomeActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.Permissao;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private CircleImageView perfil_foto;
    private ImageView perfil_alterarFoto;
    private TextInputEditText perfil_nome;
    private TextView perfil_email;
    private Button perfil_botao;

    //IMAGEM
    private Uri imageUri;
    private String myUri;
    private ProgressBar progressBarFoto;
    private ProgressBar progressBarDados;

    //private Usuario user;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //DADOS DO USUARIO
    private TextInputEditText perfil_telefone;
    private TextInputEditText perfil_endereco;

    //
    private String idUsuario = UsuarioFirebase.getIdUsuario();

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1 );

        //CONFIGURAÇÕES INICIAIS
        Paper.init(getContext());

        perfil_foto        = view.findViewById(R.id.perfil_foto);
        perfil_alterarFoto = view.findViewById(R.id.perfil_alterar_foto);
        perfil_email       = view.findViewById(R.id.perfil_email);
        perfil_nome        = view.findViewById(R.id.perfil_nome);
        perfil_telefone    = view.findViewById(R.id.perfil_telefone);
        perfil_endereco    = view.findViewById(R.id.perfil_endereco);
        perfil_botao       = view.findViewById(R.id.perfil_botao);

        progressBarFoto    = view.findViewById(R.id.perfil_progressBar2_foto);
        progressBarDados   = view.findViewById(R.id.perfil_progressBar2_dados);

        //ALTERAR FOTO DO USUARIO
        perfil_alterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        //.setAspectRatio(5, 3)
                        .getIntent( getActivity() );
                startActivityForResult( intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE );
            }
        });

        perfil_botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atualizarDados();
            }
        });
        return view;
    }
    private void atualizarDados(){
        final String nome       = perfil_nome.getText().toString().trim();
        final String telefone   = perfil_telefone.getText().toString().trim();
        final String endereco   = perfil_endereco.getText().toString().trim();

        if( nome.isEmpty() ){
            mensagem("O nome é obrigatório");
        }else
        if( telefone.isEmpty() ){
            mensagem("Digite o seu telefone");
        }else
        if( endereco.isEmpty() ){
            mensagem("Preencha o seu endereço completo");
        }else {
            progressBarDados.setVisibility( View.VISIBLE );
            perfil_botao.setEnabled( false );

            DatabaseReference usuarioRef = Firebase.getDatabase()
                    .child("Usuarios")
                    .child( idUsuario );
            HashMap<String, Object> mapa = new HashMap<>();
            mapa.put("nome", nome);
            mapa.put("telefone", telefone);
            mapa.put("endereco", endereco);

            usuarioRef.updateChildren( mapa )
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if( task.isSuccessful() ){
                                if( UsuarioFirebase.atualizarNomeUsuario( nome ) ){

                                    mensagem("Dados Atualizados com sucesso.");
                                    /*Usuario usuario = new Usuario(); //Paper.book().read("usuario"); //ADICIONADO
                                    usuario.setNome( nome );
                                    usuario.setTelefone( telefone );
                                    usuario.setEndereco( endereco );
                                    Paper.book().write("usuario", usuario);
                                    */
                                    HomeActivity.mostrarDadosHeader();
                                }
                            }
                            progressBarDados.setVisibility( View.INVISIBLE );
                            perfil_botao.setEnabled( true );
                        }
                    });
        }
    }

    private void recuperarDados(){
        //BOOK
        /*
        Usuario user = Paper.book().read("usuario");
        perfil_nome.setText( user.getNome() +" book");
        perfil_email.setText( user.getEmail() +" book");
        perfil_telefone.setText( user.getTelefone() +" book");
        perfil_endereco.setText( user.getEndereco() +" book");

        if( user.getFoto() != null ){
            Picasso.get().load( user.getFoto() ).into( perfil_foto );
        }
        */

        //Usuario usuario = UsuarioFirebase.getDadosUsuarioLogado()
        DatabaseReference usuarioRef = Firebase.getDatabase()
                .child( "Usuarios" )
                .child( UsuarioFirebase.getIdUsuario() );

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                perfil_email.setText( usuario.getEmail() );
                perfil_nome.setText( usuario.getNome() );
                perfil_telefone.setText( usuario.getTelefone() );
                perfil_endereco.setText( usuario.getEndereco() );

                if( usuario.getFoto() != null && usuario.getFoto() != "" ){
                    Picasso.get().load( usuario.getFoto() ).into( perfil_foto );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarDados();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null ){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            perfil_foto.setImageURI( imageUri );
            atualizarFoto();
        }
    }

    private void atualizarFoto(){
        if( imageUri != null ){
            progressBarFoto.setVisibility( View.VISIBLE );

            final StorageReference fileRef = Firebase.getStorage()
                    .child( idUsuario )
                    .child( "Perfil" )
                    .child( "foto" + ".jpg" );
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

                        DatabaseReference ref = Firebase.getDatabase()
                                .child("Usuarios")
                                .child( UsuarioFirebase.getIdUsuario() );

                        HashMap<String, Object> mapa = new HashMap<>();
                        mapa.put("foto", myUri);

                        //Usuario user = Paper.book().read("usuario");
                        //user.setFoto( myUri );
                        //Paper.book().write("usuario", user );

                        ref.updateChildren( mapa ).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if( task.isSuccessful() ){
                                    progressBarFoto.setVisibility( View.INVISIBLE );
                                    mensagem( "Foto Salva com Sucesso" );
                                    HomeActivity.mostrarDadosHeader();
                                    perfil_foto.setImageURI( imageUri );
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    private void mensagem(String texto){
        Toast.makeText(getContext(), texto, Toast.LENGTH_SHORT).show();
    }
}
