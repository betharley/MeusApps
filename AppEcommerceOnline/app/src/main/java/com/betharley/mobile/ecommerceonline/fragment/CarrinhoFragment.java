package com.betharley.mobile.ecommerceonline.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.adapter.AdaptadorCarrinho;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarrinhoFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorCarrinho adaptador;
    private ArrayList<Produto> lista = new ArrayList<>();

    private DatabaseReference carrinhoRef;
    private ValueEventListener valueEventListener;

    public CarrinhoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_carrinho, container, false);

        //INICIAR COMPONENTES
        recyclerView = view.findViewById(R.id.carrinho_recyclerView);

        //RECYCLER VIEW E ADAPTADOR
        layoutManager = new LinearLayoutManager( getContext() );
        adaptador = new AdaptadorCarrinho(getContext(), lista );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adaptador );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarCarrinho();
    }

    @Override
    public void onStop() {
        super.onStop();
        carrinhoRef.removeEventListener( valueEventListener );
    }

    private void recuperarCarrinho(){

        carrinhoRef = Firebase.getDatabase()
                .child("Carrinho")
                .child(UsuarioFirebase.getIdUsuario() );


        valueEventListener = carrinhoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.clear();
                for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                    Produto produto = dado.getValue( Produto.class );
                    lista.add( produto );
                }
                //Collections.reverse( lista );
                adaptador.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
