package com.betharley.mobile.ecommerceonline.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.adapter.AdaptadorProduto;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdutosFragment extends Fragment {

    private ProgressBar produtos_progressBar2;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Produto> lista = new ArrayList<>();
    private AdaptadorProduto adaptador;

    private DatabaseReference produtosRef;
    private ValueEventListener valueEventListener;

    public ProdutosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        //INICIAR COMPONENTES
        produtos_progressBar2 = view.findViewById( R.id.produtos_progressBar2);
        recyclerView = view.findViewById(R.id.produtos_recyclerView);

        //RECYCLER VIEW E ADAPRATOR
        adaptador = new AdaptadorProduto(lista, getContext());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( adaptador );

        return view;
    }

    private void recuperarProdutos(){
        produtos_progressBar2.setVisibility( View.VISIBLE );

        produtosRef = Firebase.getDatabase()
                .child("Produtos");

        valueEventListener = produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                    Produto produto = dado.getValue(Produto.class);
                    lista.add( produto );
                }
                Collections.reverse( lista );
                produtos_progressBar2.setVisibility( View.INVISIBLE );
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarProdutos();
    }

    @Override
    public void onStop() {
        super.onStop();
        produtosRef.removeEventListener( valueEventListener );
    }
}
