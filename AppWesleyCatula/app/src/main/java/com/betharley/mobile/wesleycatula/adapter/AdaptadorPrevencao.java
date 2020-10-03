package com.betharley.mobile.wesleycatula.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.wesleycatula.R;
import com.betharley.mobile.wesleycatula.model.Prevencao;

import java.util.ArrayList;

public class AdaptadorPrevencao extends RecyclerView.Adapter<AdaptadorPrevencao.MyViewHolderPrevencao> {
    private Context context;
    private ArrayList<Prevencao> listaPrevencao;

    public AdaptadorPrevencao(Context context, ArrayList<Prevencao> listaPrevencao) {
        this.context = context;
        this.listaPrevencao = listaPrevencao;
    }

    @NonNull
    @Override
    public MyViewHolderPrevencao onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prevencao, parent, false);
        return new MyViewHolderPrevencao(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderPrevencao holder, int position) {
        Prevencao prevencao = listaPrevencao.get( position );

        holder.titulo.setText( prevencao.getTitulo() );
        holder.texto.setText( prevencao.getTexto() );
        holder.titulo.setText( "IMAGEM CARREGANDO"  );
    }

    @Override
    public int getItemCount() {
        return listaPrevencao.size();
    }

    public static class MyViewHolderPrevencao extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView texto;
        ImageView imagem;

        public MyViewHolderPrevencao(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_prevencao_titulo);
            texto = itemView.findViewById(R.id.item_prevencao_texto);
           imagem = itemView.findViewById(R.id.item_prevencao_imagem);

        }
    }
}
