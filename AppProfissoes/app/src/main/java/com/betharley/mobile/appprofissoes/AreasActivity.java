package com.betharley.mobile.appprofissoes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.FloatArrayEvaluator;
import android.os.Bundle;

import com.betharley.mobile.appprofissoes.adaptador.AdaptadorArea;
import com.betharley.mobile.appprofissoes.model.Area;

import java.util.ArrayList;

public class AreasActivity extends AppCompatActivity {
    private Toolbar toolbarAreas;

    private RecyclerView recyclerViewAreas;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorArea adaptadorArea;
    private ArrayList<Area> lista = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areas);

        toolbarAreas = findViewById(R.id.toolbarAreas);
        setSupportActionBar( toolbarAreas );
        getSupportActionBar().setTitle("Areas Diversas");

        //ADAPTADOR E RECYCLER VIEW
        adaptadorArea = new AdaptadorArea(this, lista);
        recyclerViewAreas = findViewById(R.id.recyclerViewAreas);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewAreas.setLayoutManager( layoutManager );
        recyclerViewAreas.setHasFixedSize( true );
        recyclerViewAreas.setAdapter( adaptadorArea );


    }

    @Override
    protected void onStart() {
        super.onStart();
        preencherListaArea();
    }

    private void preencherListaArea(){
        lista.clear();

        lista.add( new Area( "Administração, Negócios e Serviços", R.drawable.administracao ) );
        lista.add( new Area( "Artes e Design", R.drawable.artes ) );
        lista.add( new Area( "Ciências Biológicas e da Terra", R.drawable.biologicas ) );
        lista.add( new Area( "Ciências Exatas e Informática", R.drawable.exatas) );
        lista.add( new Area( "Ciências Sociais e Humanas", R.drawable.sociais ) );
        lista.add( new Area( "Comunicação e Informação", R.drawable.comunicacao ) );
        lista.add( new Area( "Engenharia e Produção", R.drawable.engenharia ) );
        lista.add( new Area( "Saúde e Bem-Estar", R.drawable.saude ) );
        lista.add( new Area( "Outras", R.drawable.outras ) );

        adaptadorArea.notifyDataSetChanged();
    }
}
