package com.betharley.wesleycatula.mobile.appemprego.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.model.Frase;

import java.util.ArrayList;

public class AdaptadorFrase extends RecyclerView.Adapter<AdaptadorFrase.ViewHolderFrases>{
    private ArrayList<Frase> lista;
    private Context context;

    public AdaptadorFrase(ArrayList<Frase> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderFrases onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_frase, parent, false);
        ViewHolderFrases viewHolderFrases = new ViewHolderFrases(view);
        return viewHolderFrases;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFrases holder, int position) {
        Frase frase = lista.get( position );
        holder.item_frase.setText( frase.getFrase() );
        holder.item_autor.setText( frase.getAutor() );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderFrases extends RecyclerView.ViewHolder{
        TextView item_frase;
        TextView item_autor;

        public ViewHolderFrases(@NonNull View itemView) {
            super(itemView);

            item_frase = itemView.findViewById(R.id.item_frase);
            item_autor = itemView.findViewById(R.id.item_autor);

        }
    }
}
