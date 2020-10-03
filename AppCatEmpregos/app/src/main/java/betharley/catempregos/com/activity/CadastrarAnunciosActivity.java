package betharley.catempregos.com.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.santalu.maskedittext.MaskEditText;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import betharley.catempregos.com.R;
import betharley.catempregos.com.helper.SetupFirebase;
import betharley.catempregos.com.model.Anuncio;
import dmax.dialog.SpotsDialog;

public class CadastrarAnunciosActivity extends AppCompatActivity {
    private Spinner campoEstado, campoCategoria;

    private TextInputEditText campoEmpresa, campoCargo, campoDescricao;
    private TextInputEditText campoEmail, campoSite;
    private MaskEditText campoTelefone;
    private CurrencyEditText campoSalario;
    private CheckBox campoCombinar;
    private Button publicarVaga;
    private TextView campoData;

    String salario;
    boolean combinar;
    private Anuncio anuncio = new Anuncio();

    private AlertDialog dialog;

    private Calendar calendar;
    private int ano, mes, dia, hora, minuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncios);

        //Toobar
        getSupportActionBar().setTitle("Cadastrar Usuário");

        //Iniciar componentes
        iniciarComponentes();

        //Carregar Spiner
        carregarSpiner();

        DatabaseReference anuncioRef = SetupFirebase.getFirebase()
                .child("anuncios").child("teste").child("empresa");
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarData();

    }

    public void validarAnuncio(View view){
        String texto = "";
        verificarData();

        String estado = campoEstado.getSelectedItem().toString();
        String categoria = campoCategoria.getSelectedItem().toString();
        if(estado.equals("Região")){
           estado = "";
        }
        if(categoria.equals("Categoria")){
            categoria = "";
        }
        String empresa = campoEmpresa.getText().toString();
        String cargo = campoCargo.getText().toString();
        if(combinar || campoSalario.getRawValue() < 1){
            salario = "\nSalário: a combinar";
            texto = salario;
        }else{
            salario = "\nSalário: " + campoSalario.getText().toString(); //R$12.256.33
        }

        String email = campoEmail.getText().toString();
        String telefone = campoTelefone.getText().toString(); //62-99999-8888
        String site = campoSite.getText().toString();

        String descricao = campoDescricao.getText().toString();
        String data = campoData.getText().toString();


        if( empresa.isEmpty() ){
            texto = "Prencha o nome da empresa";
        }else if( cargo.isEmpty() ){
            texto = "Prencha o nome do cargo";
        }else if( descricao.isEmpty() ){
            texto = "Prencha a descriçao da função";
        }else
        if( email.isEmpty() && telefone.isEmpty() && site.isEmpty() ){
            texto = "Prencha ao menos uma dessas informações: \n \t Email, Telefone ou Site";
        }else{

            anuncio.setEstado( estado );
            anuncio.setCategoria( categoria );
            anuncio.setEmpresa( empresa );
            anuncio.setCargo( cargo );
            anuncio.setCombinar( combinar );
            anuncio.setSalario( salario );
            anuncio.setEmail( email );
            anuncio.setTelefone( telefone );
            anuncio.setSite( site );
            anuncio.setDescricao( descricao );
            anuncio.setData( data );

            String teste = anuncio.ajustarAnuncio();
            anuncio.setAnuncioCompleto( teste );
            mensagem("Anúncio sendo salvo ");
            salvarAnuncio();

            return;
        }

        mensagem(texto);
    }

    public void ativarCheckBox(View view){
        combinar = campoCombinar.isChecked();
        if(combinar){
            salario = "A combinar";
            campoSalario.setEnabled(false);
            campoSalario.setBackgroundColor(Color.parseColor("#AAAAAA"));
        }else{
            salario = campoSalario.getText().toString();
            campoSalario.setEnabled(true);
            campoSalario.setBackgroundColor(R.drawable.input_fundo);

        }
    }

    private void mensagem(String texto){
        Toast.makeText(CadastrarAnunciosActivity.this,
                " " + texto,
                Toast.LENGTH_LONG).show();
    }

    public void salvarAnuncio(){
        //salario = campoSalario.getText().toString(); //R$12.256.33
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Salvando Anuncio")
                .setCancelable(false)
                .build();
        dialog.show();


        //SALVAR O ANUNCIO NO BANCO DE DADOS
        anuncio.salvar();

        dialog.dismiss();
        finish();
    }

    private void iniciarComponentes(){
        campoEstado = findViewById(R.id.cadastrar_anuncio_estado);
        campoCategoria = findViewById(R.id.cadastrar_anuncio_categoria);

        campoEmpresa = findViewById(R.id.cadastrar_anuncio_empresa);
        campoCargo = findViewById(R.id.cadastrar_anuncio_cargo);
        campoDescricao = findViewById(R.id.cadastrar_anuncio_descricao);
        campoEmail = findViewById(R.id.cadastrar_anuncio_email);
        campoTelefone = findViewById(R.id.cadastrar_anuncio_telefone);
        campoSite = findViewById(R.id.cadastrar_anuncio_site);
        campoSalario = findViewById(R.id.cadastrar_anuncio_salario);
        campoCombinar = findViewById(R.id.cadastrar_anuncio_check_box_salario);
        publicarVaga = findViewById(R.id.cadastrar_anuncio_publicar_vaga);
        campoData = findViewById(R.id.cadastrar_anuncio_data);

        //Configurar a localidade
        Locale locale = new Locale("pt", "BR");
        campoSalario.setLocale( locale );

    }

    public void carregarSpiner(){
        //ESTADOS
        String[] estados = getResources().getStringArray(R.array.spiner_estados);
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                estados
        );
        adapterEstados.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        campoEstado.setAdapter( adapterEstados );

        //CATEGORIA
        String[] categorias = getResources().getStringArray(R.array.spiner_categorias);
        ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                categorias
        );
        adapterCategoria.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        campoCategoria.setAdapter( adapterCategoria );

    }

    private void verificarData(){

        String dataAtual, horaAtual;
        calendar = Calendar.getInstance();

        //A DATA FUNCIONA A  parti da API 24
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        dataAtual = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        //SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        horaAtual = currentTime.format(calendar.getTime());

        campoData.setText(horaAtual  + "  -  " + dataAtual );

        /*
        ano = calendar.get( Calendar.YEAR );
        mes = calendar.get( Calendar.MONTH );
        dia = calendar.get( Calendar.DAY_OF_MONTH );
        hora = calendar.get( Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);

        mes = mes + 1;

        String textoDia = "";
        String textoMes = "";
        String textohora = "";
        String textoMinuto = "";

        if( dia < 10 ){
            textoDia = ("0" + dia);
        }
        if( mes < 10 ){
            textoMes = ("0" + mes);
        }
        if( hora < 10 ){
            textohora = "0" + hora;
        }
        if( minuto < 10){
            textoMinuto = "0" + minuto;
        }

        if(hora == 0){
            textoMinuto = "00";
        }
        if( minuto == 0){
            textoMinuto = "00";
        }
        */



/*
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();

        //Formata Hora
        DateFormat hora = DateFormat.getTimeInstance();
        System.out.println("Hora formatada: "+hora.format(data));

        DateFormat f = DateFormat.getDateInstance(DateFormat.FULL); //Data COmpleta
        System.out.println("Data brasileira: "+f.format(data));

        f = DateFormat.getDateInstance(DateFormat.MEDIUM);
        System.out.println("Data resumida 1: "+f.format(data));

        campoData.setText(""+f.format(data));
*/
    }
}















