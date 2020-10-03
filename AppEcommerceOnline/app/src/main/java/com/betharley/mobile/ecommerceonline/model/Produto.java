package com.betharley.mobile.ecommerceonline.model;

import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Produto implements Serializable{
    private String id;
    private String foto;
    private String nome;
    private String descricao;
    private String hora;
    private String data;
    private String status;
    private String pesquisar;
    private String publicar;
    private int quantidade;
    private int preco;
    private int total;

    private Vendedor vendedor;
    private Usuario usuario;


    public Produto() {
    }

    public boolean salvar(){
        try {
            DatabaseReference produtoRef = Firebase.getDatabase()
                    .child("Vendedores")
                    .child( vendedor.getId() )
                    .child("Produtos")
                    .child( getId() );

            produtoRef.setValue( this );
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean publicar(){
        try {

            DatabaseReference publicarRef = Firebase.getDatabase()
                    .child("Produtos")
                    .child( getId() );

            publicarRef.setValue( this );

            DatabaseReference vendedorRef = Firebase.getDatabase()
                    .child("Vendedores")
                    .child( vendedor.getId() )
                    .child("Produtos")
                    .child( getId() );

            vendedorRef.removeValue();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean remover(){
        DatabaseReference removerRef = Firebase.getDatabase()
                .child("Vendedores")
                .child( vendedor.getId() )
                .child("Produtos")
                .child( getId() );

        removerRef.removeValue();
        return true;
    }

    public boolean alterar(){

        DatabaseReference produtoRef = Firebase.getDatabase()
                .child("Vendedores")
                .child( vendedor.getId() )
                .child("Produtos")
                .child( getId() );

        produtoRef.setValue( this );

        return true;
    }

    public boolean carrinho(){
        DatabaseReference carrinhoRef = Firebase.getDatabase()
                .child( "Carrinho" )
                .child( getUsuario().getId() )
                .child( getId() );

        setStatus( "Não Enviado" );
        carrinhoRef.setValue( this );

        return true;
    }
    public boolean atualizarCarrinho(){
        DatabaseReference removerRef = Firebase.getDatabase()
                .child( "Carrinho" )
                .child( getUsuario().getId() )
                .child( getId() );

        removerRef.setValue( this );
        return true;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        setPesquisar( nome.toLowerCase() );
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPesquisar() {
        return pesquisar;
    }

    public void setPesquisar(String pesquisar) {
        this.pesquisar = pesquisar;
    }

    public String getPublicar() {
        return publicar;
    }

    public void setPublicar(String publicar) {
        this.publicar = publicar;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getPreco() {
        return preco;
    }

    public void setPreco(int preco) {
        this.preco = preco;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
}
