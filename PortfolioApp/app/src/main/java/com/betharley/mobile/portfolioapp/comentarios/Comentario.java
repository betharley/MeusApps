package com.betharley.mobile.portfolioapp.comentarios;

import java.io.Serializable;

public class Comentario implements Serializable {
    private String texto;
    private String time;
    private String keyUsuario;
    private String keyComentario;

    public Comentario() {
    }

    public Comentario(String texto, String time, String keyUsuario, String keyComentario) {
        this.texto = texto;
        this.time = time;
        this.keyUsuario = keyUsuario;
        this.keyComentario = keyComentario;
    }

    public String getKeyComentario() {
        return keyComentario;
    }

    public void setKeyComentario(String keyComentario) {
        this.keyComentario = keyComentario;
    }

    public String getKeyUsuario() {
        return keyUsuario;
    }

    public void setKeyUsuario(String keyUsuario) {
        this.keyUsuario = keyUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
