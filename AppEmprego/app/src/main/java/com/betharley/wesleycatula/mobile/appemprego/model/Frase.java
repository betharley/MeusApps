package com.betharley.wesleycatula.mobile.appemprego.model;

public class Frase {

    private String frase;
    private String autor;

    public Frase(String frase, String autor) {
        this.frase = frase;
        this.autor = autor;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
