package com.betharley.mobile.ecommerceonline.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.adapter.AdaptadorPedidos;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
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
public class PedidosFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdaptadorPedidos adaptadorPedidos;
    private ArrayList<Produto> lista;

    private DatabaseReference pedidoRef;
    private ValueEventListener valueEventListener;

    public PedidosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);

        //CONFIGURAÇÕES INICIAIS

        recyclerView = view.findViewById(R.id.pedidos_recycler_view);

        lista = new ArrayList<>();
        adaptadorPedidos = new AdaptadorPedidos(lista, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adaptadorPedidos );

        return view;
    }

    private void recuperarPedidos(){
        pedidoRef = Firebase.getDatabase()
                .child( "Pedidos" )
                .child( UsuarioFirebase.getIdUsuario() );

        valueEventListener = pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ( dataSnapshot.exists() ){
                    lista.clear();
                    for ( DataSnapshot dado : dataSnapshot.getChildren() ){
                        Produto produto = dado.getValue(Produto.class);
                        lista.add(produto);
                    }
                    Collections.reverse(lista);
                    adaptadorPedidos.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperarPedidos();
    }

    @Override
    public void onStop() {
        super.onStop();
        pedidoRef.removeEventListener(valueEventListener);
    }

}
