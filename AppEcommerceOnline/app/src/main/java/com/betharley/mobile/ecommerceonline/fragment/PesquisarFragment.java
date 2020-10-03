package com.betharley.mobile.ecommerceonline.fragment;


import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.adapter.AdaptadorProduto;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;

    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorProduto adaptadorProduto;
    private ArrayList<Produto> lista;
    private DatabaseReference pesquisaRef = Firebase.getDatabase().child("Produtos");

    public PesquisarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesquisar, container, false);


        //INICIAR COMPONENTES
        searchView   = view.findViewById(R.id.pesquisar_searchView);
        recyclerView = view.findViewById(R.id.pesquisar_recyclerView);



        //RECYCLER VIEW
        lista = new ArrayList<>();
        adaptadorProduto = new AdaptadorProduto(lista, getContext() );
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter(adaptadorProduto);

        //RECUPERAR PRODUTOS APENAS UMA VEZ NA CRIAÇÃO DO FRAGMENT
        recuperarProduto();



        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        pesquisarProduto();
    }

    //PESQUISAR O PRODUTO
    private void pesquisarProduto(){
        searchView.setQueryHint("Pesquisar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String texto = newText.toLowerCase();

                if( texto.length() > 0 ){
                    //mensagem("Texto Pesquisado: " + texto);

                    Query query = pesquisaRef.orderByChild( "pesquisar" )
                            .startAt( texto )
                            .endAt(texto + "\uf8ff" );

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            lista.clear();
                            for( DataSnapshot dado : dataSnapshot.getChildren() ){
                                Produto produto = dado.getValue(Produto.class);
                                lista.add( produto );
                            }
                            adaptadorProduto.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                return true;
            }
        });
    }

    private void recuperarProduto(){
        DatabaseReference recuperarRef = Firebase.getDatabase()
                .child("Produtos");
        recuperarRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for( DataSnapshot dado : dataSnapshot.getChildren() ){
                    Produto produto = dado.getValue(Produto.class);
                    lista.add( produto );
                }
                Collections.reverse( lista );
                adaptadorProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mensagem(String texto){
        Toast.makeText(getContext(), texto, Toast.LENGTH_SHORT).show();
    }

}
