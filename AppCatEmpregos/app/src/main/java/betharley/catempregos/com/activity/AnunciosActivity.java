package betharley.catempregos.com.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import betharley.catempregos.com.R;
import betharley.catempregos.com.adapter.AdapterAnuncios;
import betharley.catempregos.com.helper.RecyclerItemClickListener;
import betharley.catempregos.com.helper.SetupFirebase;
import betharley.catempregos.com.model.Anuncio;
import dmax.dialog.SpotsDialog;

public class AnunciosActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = SetupFirebase.getAutenticacao();
    private DatabaseReference anunciosPublicosRef;

    private RecyclerView recyclerAnunciosPublicos;
    private Button botao_regiao, botao_categoria;
    private AdapterAnuncios adaptadorAnuncios;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private AlertDialog dialog;
    private String filtroEstado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);

        //Toobar
        getSupportActionBar().setTitle("Goiânia Empregos");

        //Apagar depois
        //startActivity( new Intent( getApplicationContext(), CadastrarAnunciosActivity.class ) );

        //INICIAR COMPONENTES
        iniciarComponentes();
        anunciosPublicosRef = SetupFirebase.getFirebase().child("anuncios");

        //CONFIGURAR O ADAPTADOR
        adaptadorAnuncios = new AdapterAnuncios(listaAnuncios, this);

        //CONFIGURAR O RECYCLER VIEW
        recyclerAnunciosPublicos.setLayoutManager( new LinearLayoutManager(this));
        recyclerAnunciosPublicos.setHasFixedSize( true );
        recyclerAnunciosPublicos.setAdapter( adaptadorAnuncios );

        //Recuperar anuncios
        recuperarAnuncios();

        //Aplicar eventos de clique
        recyclerAnunciosPublicos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        AnunciosActivity.this,
                        recyclerAnunciosPublicos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Anuncio anuncioSelecionado = listaAnuncios.get( position );
                                Intent intent = new Intent(AnunciosActivity.this, DetalhesActivity.class);
                                intent.putExtra("anuncioSelecionado", anuncioSelecionado);
                                startActivity( intent );
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }

    //RECUPERAR ANUNCIOS PUBLICOS
    private void recuperarAnuncios(){
        listaAnuncios.clear();

        //TELA DE CARREGANDO ANUNCIOS
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando Anúncios")
                .setCancelable(false)
                .build();
        dialog.show();


        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for( DataSnapshot anuncio : dataSnapshot.getChildren() ){
                    listaAnuncios.add( anuncio.getValue( Anuncio.class) );
                }
                Collections.reverse( listaAnuncios );
                adaptadorAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if( autenticacao.getCurrentUser() == null ){ //Usuario Deslogado
            menu.setGroupVisible(R.id.group_deslogado, true);
        }else{ // Usuario Logado
            menu.setGroupVisible(R.id.group_logado, true);
        }
        //apagar
       // menu.setGroupVisible(R.id.group_logado, true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId() ){
            case R.id.publicar_anuncios: // Usuario logado
                startActivity( new Intent( getApplicationContext(), CadastrarAnunciosActivity.class ) );
                break;
            case R.id.menu_anuncios:
                startActivity( new Intent( getApplicationContext(), MeusAnunciosActivity.class ) );
                break;
            case R.id.menu_logout:
                autenticacao.signOut();
                invalidateOptionsMenu();
                break;
            case R.id.menu_fechar_logado:

                break;


            case R.id.menu_publicar: // Usuario deslogado
                //startActivity( new Intent( getApplicationContext(), CadastrarAnunciosActivity.class ) );
                startActivity( new Intent(getApplicationContext(), LoginActivity.class) );
                break;
            case R.id.menu_login:
                startActivity( new Intent(getApplicationContext(), LoginActivity.class) );
                break;
            case R.id.menu_cadastrar:
                if(autenticacao.getCurrentUser() == null){
                    //apagar depois
                    startActivity( new Intent(getApplicationContext(), RegistroActivity.class) );
                }else{
                    Toast.makeText(AnunciosActivity.this,
                            "Usuário logado " + autenticacao.getCurrentUser(),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_fechar_deslogado:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //INICIAR COMPUNENTES
    private void iniciarComponentes(){

        recyclerAnunciosPublicos = findViewById(R.id.recycler_anuncios_publicos);

        //botao_regiao = findViewById(R.id.anuncios_regiao);
        //botao_categoria = findViewById(R.id.anuncios_categoria);

    }

    //FILTRAR POR ESTADO
    public void filtrarEstado(View view){

        AlertDialog.Builder dialogEstado = new AlertDialog.Builder(this);
        dialogEstado.setTitle("Selecione o Estado Desejado");

        //Configurar o spinner Estados
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        final Spinner spinnerEstado = viewSpinner.findViewById(R.id.spinnerFiltro);
        //ESTADOS
        String[] estados = getResources().getStringArray(R.array.spiner_estados);
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                estados
        );
        adapterEstados.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter( adapterEstados );
        dialogEstado.setView( viewSpinner );

        dialogEstado.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                filtroEstado = spinnerEstado.getSelectedItem().toString();
            }
        });
        dialogEstado.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = dialogEstado.create();
        dialog.show();
    }
    //FILTRAR POR CATEGORIA
    public void filtrarCategoria(View view){

    }


}

