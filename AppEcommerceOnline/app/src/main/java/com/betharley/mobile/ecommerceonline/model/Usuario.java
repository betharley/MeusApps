package com.betharley.mobile.ecommerceonline.model;

import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;

public class Usuario implements Serializable {
    private String id;
    private String foto;
    private String nome;
    private String email;

    private String telefone;
    private String endereco;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public void atualizar(){
        //SALVANDO O PERFIL DE USUARIO
        DatabaseReference usuarioRef = Firebase.getDatabase()
                .child("Usuarios")
                .child( getId() );

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("id", getId() );
        mapa.put("nome", getNome() );
        mapa.put("email", getEmail() );
        //mapa.put("senha", getSenha() );
        usuarioRef.updateChildren( mapa );


        //SALVANDO O PERFIL DE VENDEDOR
        /*Vendedor vendedor = new Vendedor();
        vendedor.setId( getId() );
        vendedor.setNome( getNome() );
        vendedor.setEmail( getEmail() );

        DatabaseReference vendedorRef = Firebase.getDatabase()
                .child("Vendedores")
                .child( vendedor.getId() );

        vendedorRef.updateChildren( mapa );*/
    }

    public void atualizarLogin(){
        //SALVANDO O PERFIL DE USUARIO
        DatabaseReference usuarioRef = Firebase.getDatabase()
                .child("Usuarios")
                .child( getId() );

        HashMap<String, Object> mapa = new HashMap<>();
        mapa.put("id", getId() );
        mapa.put("email", getEmail() );

        usuarioRef.updateChildren( mapa );

    }
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
