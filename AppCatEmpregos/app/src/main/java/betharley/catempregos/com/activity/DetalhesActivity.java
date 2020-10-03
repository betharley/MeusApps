package betharley.catempregos.com.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import betharley.catempregos.com.R;
import betharley.catempregos.com.model.Anuncio;

public class DetalhesActivity extends AppCompatActivity {

    private TextView empresa, cargo, salario;
    private TextView email, telefone, site;
    private TextView descricao, data;
    private TextView categoria, estado;

    private Anuncio anuncioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        //Toobar
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalhes do Anúncio");

        //Iniciar componentes
        iniciarComponentes();

        //Recuperar anuncio para exibição
        anuncioSelecionado = (Anuncio) getIntent().getSerializableExtra("anuncioSelecionado");

        if(anuncioSelecionado != null){
            empresa.setText( anuncioSelecionado.getEmpresa().trim() );
            cargo.setText( anuncioSelecionado.getCargo().trim() );
            salario.setText( anuncioSelecionado.getSalario().trim() );

            email.setText( anuncioSelecionado.getEmail().trim() );
            telefone.setText( anuncioSelecionado.getTelefone().trim() );
            site.setText( anuncioSelecionado.getSite().trim() );


            descricao.setText( "Descrição: \n"+ anuncioSelecionado.getDescricao().trim() );
            data.setText( anuncioSelecionado.getData().trim() );
            categoria.setText( anuncioSelecionado.getCategoria().trim() );
            estado.setText( anuncioSelecionado.getEstado().trim() );
        }

        //PELO MENOS UMA DESSAS INFORMAÇÕES VAI FICAR COM PREENCHIMENTO - AS OUTRAS DUAS NÃO SÃO OBRIGATORIAS
        if(anuncioSelecionado.getEmail().replace(" ", "").isEmpty()){
            email.setVisibility(View.GONE);
        }
        if(anuncioSelecionado.getTelefone().replace(" ", "").isEmpty()){
            telefone.setVisibility(View.GONE);
        }
        if(anuncioSelecionado.getSite().replace(" ", "").isEmpty()){
            site.setVisibility(View.GONE);
        }

    }

    private void iniciarComponentes() {
        empresa = findViewById(R.id.detalhes_empresa);
        cargo = findViewById(R.id.detalhes_cargo);
        salario = findViewById(R.id.detalhes_salario);
        email = findViewById(R.id.detalhes_email);
        telefone = findViewById(R.id.detalhes_telefone);
        site = findViewById(R.id.detalhes_site);
        descricao = findViewById(R.id.detalhes_descricao);
        data = findViewById(R.id.detalhes_data);
        categoria = findViewById(R.id.detalhes_categoria);
        estado = findViewById(R.id.detalhes_regiao);

    }


}
