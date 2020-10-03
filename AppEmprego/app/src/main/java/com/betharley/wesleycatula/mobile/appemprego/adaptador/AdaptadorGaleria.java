package com.betharley.wesleycatula.mobile.appemprego.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.wesleycatula.mobile.appemprego.AbrirSiteActivity;
import com.betharley.wesleycatula.mobile.appemprego.ComentarioActivity;
import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.activity.MainActivity;
import com.betharley.wesleycatula.mobile.appemprego.model.Foto;
import com.betharley.wesleycatula.mobile.appemprego.model.Galeria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AdaptadorGaleria extends RecyclerView.Adapter<AdaptadorGaleria.ViewHolderGaleria> {

    private Context context;
    private ArrayList<Galeria> listaGaleria;
    private String keyUsuario;
    public AdaptadorGaleria(Context context, ArrayList<Galeria> listaGaleria) {
        this.context = context;
        this.listaGaleria = listaGaleria;
    }

    @NonNull
    @Override
    public ViewHolderGaleria onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( context ).inflate(R.layout.item_foto_layout, parent, false);
        ViewHolderGaleria viewHolderFoto = new ViewHolderGaleria(view);
        return viewHolderFoto;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderGaleria holder, int position) {

        final Galeria galeria = listaGaleria.get( position );
        //holder.item_foto_galeria.setImageResource( "foto.getFoto()" );

        Picasso.get()
                .load(galeria.getImagem())
                .placeholder(R.drawable.profile)
                .into( holder.item_galeria );

        if( this.keyUsuario != null ){
            holder.item_likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "LIKE", Toast.LENGTH_SHORT).show();
                    DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference()
                            .child("Likes").child(galeria.getKey()).child(keyUsuario);
                    likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if( dataSnapshot.exists() ){
                                //holder.item_likes.setImageResource(R.drawable.icone_dislike);
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Likes").child(galeria.getKey()).child(keyUsuario)
                                        .removeValue();
                            }else{
                                //holder.item_likes.setImageResource(R.drawable.icone_like);
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Likes").child(galeria.getKey())
                                        .child(keyUsuario).setValue("true");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            holder.item_comentar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "COMENTAR", Toast.LENGTH_SHORT).show();
                    //IR PARA ACTIVITY COMENTÁIO
                    Intent intentComentario = new Intent(context, ComentarioActivity.class);
                    intentComentario.putExtra( "imagemClicada", galeria );
                    context.startActivity( intentComentario );
                }
            });
        }else{
           Toast.makeText(context, "No momento você não tem autorização", Toast.LENGTH_SHORT).show();
        }

        //QUANTIDADE DE LIKES DE CADA IMAGEM
        FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(galeria.getKey())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if( dataSnapshot.exists() ){
                            String totalLikes = String.valueOf( dataSnapshot.getChildrenCount() );
                            holder.likes_quantidade.setText( totalLikes + "  Likes" );
                        }else{
                            holder.likes_quantidade.setText( "0  Likes" );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //VERIFICAR SE ESTA COM/SEM LIKES EM CADA IMAGEM
        FirebaseDatabase.getInstance().getReference().child("Likes")
                .child(galeria.getKey()).child(keyUsuario)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if( dataSnapshot.exists() ){
                            holder.item_likes.setImageResource(R.drawable.icone_like);
                        }else{
                            holder.item_likes.setImageResource(R.drawable.icone_dislike);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //VERIFICA A QUANTIDADE DE LIKES
        FirebaseDatabase.getInstance().getReference()
            .child("Comentarios").child( galeria.getKey() )
            .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( dataSnapshot.exists() ){
                    String quantidade = String.valueOf( dataSnapshot.getChildrenCount() );
                    holder.comentario_quantidade.setText( quantidade+ "  Comentários" );
                }else{
                    holder.comentario_quantidade.setText(  "0  Comentários" );
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaGaleria.size();
    }

    public void setkeyUsuario(String keyUsuario){
        this.keyUsuario = keyUsuario;
    }

    public static class ViewHolderGaleria extends RecyclerView.ViewHolder{
        ImageView item_galeria;
        ImageView item_likes;
        ImageView item_comentar;
        TextView  likes_quantidade;
        TextView  comentario_quantidade;

        public ViewHolderGaleria(@NonNull View itemView) {
            super(itemView);
            item_galeria = itemView.findViewById(R.id.item_galeria_imagem);
            item_likes = itemView.findViewById(R.id.item_foto_like);
            item_comentar = itemView.findViewById(R.id.item_foto_comentar);

            likes_quantidade = itemView.findViewById(R.id.item_likes_quantidade);
            comentario_quantidade = itemView.findViewById(R.id.item_quantidade);
        }
    }
}
