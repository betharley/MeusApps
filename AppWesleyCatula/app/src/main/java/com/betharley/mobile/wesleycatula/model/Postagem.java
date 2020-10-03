package com.betharley.mobile.wesleycatula.model;

import java.io.Serializable;

public class Postagem implements Serializable {
    private String id;

    private String categoria;
    private String title; //rendered
    private String content; //rendered
    private String excerpt; //rendered
    private String source_url; // imagem
    private String data;

    public Postagem() {
    }

    public Postagem(String id, String title, String content, String excerpt, String source_url) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.excerpt = excerpt;
        this.source_url = source_url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
}
