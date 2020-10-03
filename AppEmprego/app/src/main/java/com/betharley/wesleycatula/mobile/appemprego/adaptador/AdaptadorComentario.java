package com.betharley.wesleycatula.mobile.appemprego.adaptador;

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

import com.betharley.wesleycatula.mobile.appemprego.ComentarioActivity;
import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.model.Comentario;
import com.betharley.wesleycatula.mobile.appemprego.model.Galeria;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AdaptadorComentario extends RecyclerView.Adapter<AdaptadorComentario.ViewHolderComentario> {

    private ArrayList<Comentario> listaComentario;
    private Context context;
    private static String keyUsuario;
    private Galeria galeria;

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
    public void onBindViewHolder(@NonNull ViewHolderComentario holder, int position) {
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
                                    .child("Comentarios").child( galeria.getKey() )
                                    .child( comentario.getKeyComentario() )
                                    .removeValue();
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

            holder.comment_hora_outro.setText( comentario.getTime() );
            holder.comment_texto_outro.setText( comentario.getTexto() );

            holder.itemView.setOnClickListener(null);
        }




    }

    @Override
    public int getItemCount() {
        return listaComentario.size();
    }

    public void setGaleria(Galeria galeria){
        this.galeria = galeria;
    }
    public void setkeyUsuario(String keyUsuario){
        this.keyUsuario = keyUsuario;
    }

    public static class ViewHolderComentario extends RecyclerView.ViewHolder{
        LinearLayout layout_outro;
        LinearLayout layout_eu;
        TextView comment_texto_eu, comment_hora_eu;
        TextView comment_texto_outro, comment_hora_outro;

        public ViewHolderComentario(@NonNull View itemView) {
            super(itemView);

            layout_outro = itemView.findViewById(R.id.layout_outro);
            layout_eu = itemView.findViewById(R.id.layout_eu);
            comment_texto_eu = itemView.findViewById(R.id.item_comment_texto_eu);
            comment_hora_eu = itemView.findViewById(R.id.item_comment_hora_eu);

            comment_texto_outro = itemView.findViewById(R.id.item_comment_texto_outro);
            comment_hora_outro = itemView.findViewById(R.id.item_comment_hora_outro);

        }
    }
}
