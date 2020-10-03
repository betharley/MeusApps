package com.betharley.wesleycatula.mobile.appemprego.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.model.Foto;

import java.util.ArrayList;

public class AdaptadorFoto extends RecyclerView.Adapter<AdaptadorFoto.ViewHolderFoto> {

    private Context context;
    private ArrayList<Foto> lista;

    public AdaptadorFoto(Context context, ArrayList<Foto> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolderFoto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate(R.layout.item_foto, parent, false);
        ViewHolderFoto viewHolderFoto = new ViewHolderFoto(view);
        return viewHolderFoto;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFoto holder, int position) {

        Foto foto = lista.get( position );
        holder.item_foto_galeria.setImageResource( foto.getFoto() );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderFoto extends RecyclerView.ViewHolder{
        ImageView item_foto_galeria;

        public ViewHolderFoto(@NonNull View itemView) {
            super(itemView);
            item_foto_galeria = itemView.findViewById(R.id.item_foto_galeria);
        }
    }
}
