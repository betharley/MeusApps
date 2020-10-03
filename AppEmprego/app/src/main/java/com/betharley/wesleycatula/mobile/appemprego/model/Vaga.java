package com.betharley.wesleycatula.mobile.appemprego.model;
/*
jobkey   ---------- id
jobtitle   -------- nome da vaga
company  ---------- nome da empresa
city    ----------- cidade
state   ----------- estado
coutry   ---------- país
formattedLocation - Cidade, Estado, BR
source   ---------- nome da empresa
data    ----------- data
description ------- descricação
url  -------------- link para o site
logo   ------------ imagem da empresa
*
category ---------- null
bid --------------- 0.03
currency  --------- BRL
onmousedown ------- this.href=this.href+'&jsa=f5a42'

*/

import java.io.Serializable;

public class Vaga implements Serializable {

    private String jobkey;
    private String jobtitle;
    private String company;
    private String city;
    private String state;
    private String coutry;
    private String formattedLocation;
    private String source;
    private String date;
    private String description;
    private String url;
    private String logo;

    private String category;
    private int bid;
    private String currency;
    private String onmousedown;

    public Vaga() {
    }

    public Vaga(String jobkey, String jobtitle, String company, String city, String state, String coutry, String formattedLocation, String source, String date, String description, String url, String logo) {
        this.jobkey = jobkey;
        this.jobtitle = jobtitle;
        this.company = company;
        this.city = city;
        this.state = state;
        this.coutry = coutry;
        this.formattedLocation = formattedLocation;
        this.source = source;
        this.date = date;
        this.description = description;
        this.url = url;
        this.logo = logo;
    }

    public String getJobkey() {
        return jobkey;
    }

    public void setJobkey(String jobkey) {
        this.jobkey = jobkey;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCoutry() {
        return coutry;
    }

    public void setCoutry(String coutry) {
        this.coutry = coutry;
    }

    public String getFormattedLocation() {
        return formattedLocation;
    }

    public void setFormattedLocation(String formattedLocation) {
        this.formattedLocation = formattedLocation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOnmousedown() {
        return onmousedown;
    }

    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }
}





