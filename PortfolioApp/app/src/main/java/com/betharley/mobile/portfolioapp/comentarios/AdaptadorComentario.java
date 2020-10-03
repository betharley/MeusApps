package com.betharley.mobile.portfolioapp.comentarios;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.portfolioapp.R;
import com.betharley.mobile.portfolioapp.portfolio.PortfolioItem;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdaptadorComentario extends RecyclerView.Adapter<AdaptadorComentario.ViewHolderComentario> {

    private ArrayList<Comentario> listaComentario;
    private Context context;
    private static String keyUsuario;
    private static PortfolioItem portfolioItem;

    public AdaptadorComentario(ArrayList<Comentario> listaComentario, Context context) {
        this.listaComentario = listaComentario;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderComentario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);

        return new ViewHolderComentario(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComentario holder, final int position) {
        final Comentario comentario = listaComentario.get( position );

        //USUARIO EU
        if( comentario.getKeyUsuario().equals( keyUsuario ) ){
            holder.layout_outro.setVisibility( View.GONE );
            holder.layout_eu.setVisibility( View.VISIBLE );

            holder.comment_hora_eu.setText( comentario.getTime() );
            holder.comment_texto_eu.setText( comentario.getTexto() );

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Deletar");
                    builder.setMessage( "Deseja excluir esse comentário ?" );
                    builder.setIcon(R.drawable.icone_delete_black_24dp);

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Comentarios").child( portfolioItem.getKey() )
                                    .child( comentario.getKeyComentario() )
                                    .removeValue();
                            //notifyItemChanged(position);
                            notifyItemMoved(1, position);
                        }
                    });
                    builder.setNegativeButton("Não", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

        }else{ // OUTRO
            holder.layout_eu.setVisibility( View.GONE );
            holder.layout_outro.setVisibility( View.VISIBLE );

            //CORTA A CHAVE DE QUEM FEZ O COMENTÁRIO
            String keyCortada = comentario.getKeyUsuario().substring( comentario.getKeyUsuario().length()-8, keyUsuario.length());

            holder.comment_hora_outro.setText( comentario.getTime() );
            holder.comment_texto_outro.setText( comentario.getTexto() );
            holder.comment_key.setText( keyCortada );

            holder.itemView.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {
        return listaComentario.size();
    }

    public void setPorfFolio(PortfolioItem portfolioItem){
        this.portfolioItem = portfolioItem;
    }
    public void setkeyUsuario(String keyUsuario){
        this.keyUsuario = keyUsuario;
    }

    public static class ViewHolderComentario extends RecyclerView.ViewHolder{
        LinearLayout layout_outro;
        LinearLayout layout_eu;
        TextView comment_texto_eu, comment_hora_eu, comment_key;
        TextView comment_texto_outro, comment_hora_outro;

        public ViewHolderComentario(@NonNull View itemView) {
            super(itemView);

            layout_outro = itemView.findViewById(R.id.layout_outro);
            layout_eu = itemView.findViewById(R.id.layout_eu);
            comment_texto_eu = itemView.findViewById(R.id.item_comment_texto_eu);
            comment_hora_eu = itemView.findViewById(R.id.item_comment_hora_eu);

            comment_texto_outro = itemView.findViewById(R.id.item_comment_texto_outro);
            comment_hora_outro = itemView.findViewById(R.id.item_comment_hora_outro);

            comment_key = itemView.findViewById(R.id.item_comment_key);

        }
    }
}
