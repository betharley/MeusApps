package com.betharley.mobile.ecommerceonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.model.Comentario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorComentario extends RecyclerView.Adapter<AdaptadorComentario.VHComentario> {

    private ArrayList<Comentario> lista;
    private Context contexto;

    public AdaptadorComentario(ArrayList<Comentario> lista, Context context){
        this.lista = lista;
        this.contexto = context;
    }

    @Override
    public VHComentario onCreateViewHolder(ViewGroup parent, int typeView){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        VHComentario vhComentario = new VHComentario(view);
        return vhComentario;
    }
    @Override
    public void onBindViewHolder(VHComentario holder, int position){
        Comentario comentario = lista.get( position );

        holder.comentario_nome.setText( comentario.getNome() );
        holder.comentario_texto.setText( comentario.getTexto() );

        if( comentario.getFoto() != null ){
            Picasso.get().load( comentario.getFoto() ).into( holder.comentario_foto );
        }


    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    public class VHComentario extends RecyclerView.ViewHolder{
        CircleImageView comentario_foto;
        TextView comentario_nome;
        TextView comentario_texto;

        public VHComentario(View view){
            super(view);

            comentario_foto = view.findViewById(R.id.comentario_foto);
            comentario_nome = view.findViewById(R.id.comentario_nome);
            comentario_texto = view.findViewById(R.id.comentario_texto);
        }
    }
}
