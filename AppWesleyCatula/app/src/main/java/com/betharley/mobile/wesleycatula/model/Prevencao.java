package com.betharley.mobile.wesleycatula.model;

public class Prevencao {
    private String urlImagem;
    private String titulo;
    private String texto;

    public Prevencao() {
    }

    public Prevencao(String urlImagem, String titulo, String texto) {
        this.urlImagem = urlImagem;
        this.titulo = titulo;
        this.texto = texto;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
