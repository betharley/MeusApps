package com.betharley.wesleycatula.mobile.appemprego.model;

import java.io.Serializable;

public class Galeria implements Serializable {
    private String imagem;
    private String key;

    public Galeria() {
    }

    public Galeria(String imagem) {
        this.imagem = imagem;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

}
