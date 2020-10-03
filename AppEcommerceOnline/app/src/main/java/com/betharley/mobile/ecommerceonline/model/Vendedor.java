package com.betharley.mobile.ecommerceonline.model;

import androidx.annotation.NonNull;

import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;

import io.paperdb.Paper;

public class Vendedor implements Serializable {
    private String id;
    private String foto;
    private String email;

    private String nome;
    private String telefone;
    private String endereco;

    public Vendedor() {
    }

    public boolean salvar(){

        DatabaseReference vendedorRef = Firebase.getDatabase()
                .child("Vendedores")
                .child( getId() );
        vendedorRef.setValue( this );
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}
