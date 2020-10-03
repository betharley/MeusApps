package com.betharley.mobile.portfolioapp.team;

public class Android {

    private String url;
    private String name;
    private String ano;

    public Android() {
    }

    public Android(String url, String name, String ano) {
        this.url = url;
        this.name = name;
        this.ano = ano;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
}
