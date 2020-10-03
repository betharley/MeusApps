package com.betharley.mobile.appprofissoes.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.appprofissoes.ProfissoesActivity;
import com.betharley.mobile.appprofissoes.R;
import com.betharley.mobile.appprofissoes.model.Area;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorArea extends RecyclerView.Adapter<AdaptadorArea.ViewHolderArea> {

    private Context context;
    private ArrayList<Area> lista;

    public AdaptadorArea(Context context, ArrayList<Area> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolderArea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_areas, parent, false);
        ViewHolderArea viewHolderArea = new ViewHolderArea(view);

        return viewHolderArea;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderArea holder, final int position) {

        Area area = lista.get(position);
        holder.item_area_nome.setText( area.getNome() );
        holder.item_area_imagem.setImageResource( area.getImagem() );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProfissoes = new Intent(context, ProfissoesActivity.class);
                intentProfissoes.putExtra( "profissao", lista.get(position).getNome() );
                context.startActivity( intentProfissoes );
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolderArea extends RecyclerView.ViewHolder{
        ImageView item_area_imagem;
        TextView  item_area_nome;

        public ViewHolderArea(@NonNull View itemView) {
            super(itemView);

            item_area_imagem = itemView.findViewById(R.id.item_area_imagem);
            item_area_nome = itemView.findViewById(R.id.item_area_nome);
        }
    }
}
