package betharley.catempregos.com.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import betharley.catempregos.com.helper.SetupFirebase;

public class Anuncio implements Serializable {

    private String idAnuncio;
    private String estado;
    private String categoria;
    private String empresa;
    private String cargo;
    private String descricao;
    private String email;
    private String site;
    private String telefone;
    private String salario;
    private String data;
    private boolean combinar;
    private String anuncioCompleto;

    public Anuncio() {
        DatabaseReference anucioRef = SetupFirebase.getFirebase()
                .child("meus_anuncios");
        this.idAnuncio = anucioRef.push().getKey();
    }

    public String ajustarAnuncio(){
        String descricaoCompleta = "";

        if( !this.email.isEmpty() ){
            this.email = "\nEmail: " + this.email;
        }
        if( !this.site.isEmpty() ){
            this.site = "\nSite: " + this.site;
        }
        if( !this.telefone.isEmpty() ){
            this.telefone = "\nTelefone: " + this.telefone;
        }
        //if( this.salario.isEmpty() ){
        //this.salario = "\n" + this.salario;
        //}

        if( !this.categoria.isEmpty() ){
            this.categoria = "\nCategoria: " + this.categoria;
        }
        if( !this.estado.isEmpty()){
            this.estado = "\nRegião: " + this.estado;
        }

        descricaoCompleta =
                this.estado + this.categoria + this.salario +
                this.email +
                this.site +
                this.telefone +
                this.descricao;

        return descricaoCompleta;
    }

   public void salvar(){
        String idUsuario = SetupFirebase.getIdUsuario();
        DatabaseReference anuncioRef = SetupFirebase.getFirebase()
               .child("meus_anuncios");

        anuncioRef.child( idUsuario )
                .child( getIdAnuncio() )
                .setValue( this );

       salvarAnuncioPublico();
   }
    private void salvarAnuncioPublico(){
        DatabaseReference anuncioRef = SetupFirebase.getFirebase()
                .child("anuncios");

        anuncioRef.child( getIdAnuncio() )
                .setValue( this );

        // setAnuncioCompleto( ajustarAnuncio() );
    }

    public void remover(){
        String idUsuario = SetupFirebase.getIdUsuario();
        DatabaseReference anuncioRef = SetupFirebase.getFirebase()
                .child("meus_anuncios")
                .child( idUsuario )
                .child( getIdAnuncio() );

        anuncioRef.removeValue();

        removerAnuncioPublico();
    }
    private void removerAnuncioPublico(){
        DatabaseReference anuncioRef = SetupFirebase.getFirebase()
                .child("anuncios")
                .child( getIdAnuncio() );

        anuncioRef.removeValue();
    }

    public String getAnuncioCompleto() {
        return anuncioCompleto;
    }

    public void setAnuncioCompleto(String anuncioCompleto) {
        this.anuncioCompleto = anuncioCompleto;
    }

    public String getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(String idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCargo() {
        return cargo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isCombinar() {
        return combinar;
    }

    public void setCombinar(boolean combinar) {
        this.combinar = combinar;
    }

}
