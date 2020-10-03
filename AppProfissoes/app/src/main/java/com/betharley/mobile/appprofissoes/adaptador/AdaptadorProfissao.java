package com.betharley.mobile.appprofissoes.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.appprofissoes.DetalhesActivity;
import com.betharley.mobile.appprofissoes.R;
import com.betharley.mobile.appprofissoes.model.Profissao;

import java.util.ArrayList;

public class AdaptadorProfissao extends RecyclerView.Adapter<AdaptadorProfissao.ViewHolderProfissao> {
    private Context context;
    private ArrayList<String> lista;
    private String profissaoArea;

    public AdaptadorProfissao(Context context, ArrayList<String> lista, String profissaoArea) {
        this.context = context;
        this.lista = lista;
        this.profissaoArea = profissaoArea;
    }

    @NonNull
    @Override
    public ViewHolderProfissao onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profissao, parent, false);
        ViewHolderProfissao viewHolderProfissao = new ViewHolderProfissao(view);
        return viewHolderProfissao;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderProfissao holder, int position) {
        final String profissao = lista.get( position );
        holder.profissaoNome.setText( profissao  );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDetalhes = new Intent(holder.itemView.getContext(), DetalhesActivity.class);
                intentDetalhes.putExtra( "profissaoEspecifica", profissao );
                intentDetalhes.putExtra( "profissaoArea", profissaoArea );
                context.startActivity( intentDetalhes );
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderProfissao extends RecyclerView.ViewHolder{
        TextView profissaoNome;

        public ViewHolderProfissao(@NonNull View itemView) {
            super(itemView);
            profissaoNome = itemView.findViewById(R.id.item_profissao_nome);
        }
    }
}
