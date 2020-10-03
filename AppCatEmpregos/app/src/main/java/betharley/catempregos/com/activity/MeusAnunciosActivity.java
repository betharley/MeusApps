package betharley.catempregos.com.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import betharley.catempregos.com.R;
import betharley.catempregos.com.adapter.AdapterAnuncios;
import betharley.catempregos.com.helper.RecyclerItemClickListener;
import betharley.catempregos.com.helper.SetupFirebase;
import betharley.catempregos.com.model.Anuncio;
import dmax.dialog.SpotsDialog;

public class MeusAnunciosActivity extends AppCompatActivity {

    private RecyclerView recyclerAnuncios;
    private List<Anuncio> anuncios = new ArrayList<>();
    private AdapterAnuncios adaptadorAnuncios;
    private DatabaseReference anuncioUsuarioRef;

    private AlertDialog dialog;
    private boolean confirmar = false;
    AlertDialog.Builder dialogExcluir;
    Anuncio anuncioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);

        //Toobar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Meus Anúncios");

        //CONFIGURAÇÇÕES INICIAIS
        anuncioUsuarioRef = SetupFirebase.getFirebase()
                .child( "meus_anuncios" )
                .child( SetupFirebase.getIdUsuario() );

        //INICIAR COMPONENTES
        iniciarComponentes();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getApplicationContext(), CadastrarAnunciosActivity.class ) );
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //CONFIGURAR O ADAPTADOR
        adaptadorAnuncios = new AdapterAnuncios(anuncios, this);

        //CONFIGURAR O RECYCLER VIEW
        recyclerAnuncios.setLayoutManager( new LinearLayoutManager(this));
        recyclerAnuncios.setHasFixedSize( true );
        recyclerAnuncios.setAdapter( adaptadorAnuncios );

        //RECUPERAR ANUNCIOS
        recuperarAnuncios();

        //ADICIONAR EVENTO DE CLIQUE NO RECYCLER VIEW
        recyclerAnuncios.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerAnuncios,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Anuncio anuncioSelecionado = anuncios.get( position );
                                Intent intent = new Intent(MeusAnunciosActivity.this, DetalhesActivity.class);
                                intent.putExtra("anuncioSelecionado", anuncioSelecionado);
                                startActivity( intent );
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                Anuncio anuncioClicado = anuncios.get( position );
                                //String nomeCargo = anuncioSelecionado.getCargo();
                                removerAnuncio(anuncioClicado);
                                   // anuncioSelecionado.remover();
                                   // adaptadorAnuncios.notifyDataSetChanged();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }

    private void recuperarAnuncios(){

        //TELA DE CARREGANDO ANUNCIOS
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Recuperando Anúncios")
                .setCancelable(false)
                .build();
        dialog.show();


        anuncioUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                anuncios.clear();

                for( DataSnapshot ds : dataSnapshot.getChildren() ){
                    anuncios.add( ds.getValue( Anuncio.class) );
                }
                Collections.reverse( anuncios );
                adaptadorAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //INICIAR COMPONENTES
    private void iniciarComponentes() {

        recyclerAnuncios = findViewById(R.id.recyclerMeusAnuncios);

    }

    //FILTRAR POR ESTADO
    public void removerAnuncio(Anuncio anuncioClicado){
        anuncioSelecionado = anuncioClicado;

        dialogExcluir = new AlertDialog.Builder(this);
        dialogExcluir.setTitle("Deseja excluir o anúncio " +"\n " + anuncioSelecionado.getCargo() );
        //dialogExcluir.setC

        dialogExcluir.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                anuncioSelecionado.remover();
                adaptadorAnuncios.notifyDataSetChanged();
            }
        });
        dialogExcluir.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = dialogExcluir.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);

    }

}
