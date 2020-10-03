package com.betharley.mobile.ecommerceonline.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.ecommerceonline.ComentariosActivity;
import com.betharley.mobile.ecommerceonline.ProdutoDetalhesActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.helper.UsuarioFirebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorProduto extends RecyclerView.Adapter<AdaptadorProduto.VHProduto>{
    private ArrayList<Produto> lista;
    private Context contexto;

    public AdaptadorProduto(ArrayList<Produto> lista, Context contexto){
        this.lista = lista;
        this.contexto = contexto;
    }

    @Override
    public VHProduto onCreateViewHolder(ViewGroup parent, int typeView){
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_produto, parent, false );
        VHProduto vhProduto = new VHProduto(view);
        return vhProduto;
    }

    @Override
    public void onBindViewHolder(final VHProduto holder, int position){
        final Produto produto = lista.get( position );

        holder.produto_nome.setText( produto.getNome() );
        holder.produto_descricao.setText( produto.getDescricao() );
        holder.produto_data_hora.setText( produto.getData() + "  " + produto.getHora() );
        holder.produto_preco.setText( "R$: " + String.valueOf( produto.getPreco() ) );

        if( produto.getFoto() != null ){
            Picasso.get().load( produto.getFoto() ).into( holder.produto_foto );
        }

        holder.produto_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, ProdutoDetalhesActivity.class);
                intent.putExtra("produto", produto);
                contexto.startActivity( intent );
            }
        });

        //QUANTIDADE DE LIKES DO PRODUTO
        recuperarTotalLikes(holder, produto);

        //VERIFICAR SE JÁ FOI DADO LIKE NO PRODUTO PELO USUARIO LOGADO
        DatabaseReference verLike = Firebase.getDatabase()
                .child( "Curtidas" )
                .child( produto.getId() );
        verLike.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.hasChild( UsuarioFirebase.getIdUsuario() ) ){
                    holder.likeButtonFeed.setLiked( true );
                }else {
                    holder.likeButtonFeed.setLiked( false );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //ADICIONAR LIKE E VERIFICAR A QUANTIDADE DE LIKE
        holder.likeButtonFeed.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                DatabaseReference likeRef = Firebase.getDatabase()
                        .child( "Curtidas" )
                        .child( produto.getId() )
                        .child( UsuarioFirebase.getIdUsuario()  );

                likeRef.setValue( UsuarioFirebase.getIdUsuario() );
                recuperarTotalLikes(holder, produto);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                DatabaseReference removerLike = Firebase.getDatabase()
                        .child( "Curtidas" )
                        .child( produto.getId() )
                        .child( UsuarioFirebase.getIdUsuario()  );

                removerLike.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        recuperarTotalLikes(holder, produto);
                        //mensagem( "removerLike" );
                    }
                });

            }
        });

        //ABRE A TELA DE COMENTÁRIOS
        holder.produto_comentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentComentarios = new Intent(contexto, ComentariosActivity.class);
                intentComentarios.putExtra( "produto", produto );
                contexto.startActivity( intentComentarios );
            }
        });

        //CARREGAR OS DADOS DO VENDEDOR NO PRODUTO NA PARTE DE CIMA
        DatabaseReference vendedorRef = Firebase.getDatabase()
                .child("Usuarios")
                .child( produto.getVendedor().getId() );
        vendedorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.exists() ){
                    String nome = dataSnapshot.child("nome").getValue().toString();
                    String foto = dataSnapshot.child("foto").getValue().toString();
                    if( foto != null ){
                        Picasso.get().load( foto ).into( holder.item_produto_usuario_foto );
                    }
                    holder.item_produto_usuario_nome.setText( nome );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //EXCLUIR O PRODUTO DO APLICATIVO
        if(UsuarioFirebase.getIdUsuario().equalsIgnoreCase( produto.getVendedor().getId() )){
            holder.item_produto_usuario_deletar.setVisibility( View.VISIBLE);
            holder.item_produto_usuario_deletar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                    builder.setTitle( "Excluir" );
                    builder.setMessage( "Deseja Excluir esse Produto do Aplicativo ?" );
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference deletarRef = Firebase.getDatabase()
                                    .child("Produtos")
                                    .child( produto.getId() );

                            deletarRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if( task.isSuccessful() ){

                                        //REMOVER AS CURTIDAS DO DATABASE
                                        DatabaseReference curtidasRef = Firebase.getDatabase()
                                                .child("Curtidas")
                                                .child( produto.getId() );
                                        curtidasRef.removeValue();

                                        //REMOVER A IMAGEM DO STORAGE
                                        StorageReference storageRef = Firebase.getStorage()
                                                .child( produto.getVendedor().getId() )
                                                .child( "Produtos" )
                                                .child( produto.getId()+ ".jpg" );
                                        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mensagem("Produto Excluido com Sucesso.");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //mensagem("Erro ao Excluir Produto. Tente novamente");
                                            }
                                        });

                                    }else {
                                        mensagem("Erro ao Excluir Produto. Tente novamente");
                                    }
                                }
                            });

                        }
                    }).setNegativeButton("Não", null);
                    builder.show();
                }
            });
        }

    }



    @Override
    public int getItemCount(){
        return lista.size();
    }

    public class VHProduto extends RecyclerView.ViewHolder{
        TextView produto_nome; //
        ImageView produto_foto;
        TextView produto_qtd_likes;
        TextView produto_qtd_comment;
        TextView produto_preco; //
        LikeButton likeButtonFeed;
        TextView produto_comentario;
        TextView produto_descricao; //
        TextView produto_data_hora; //

        CircleImageView item_produto_usuario_foto; //
        TextView item_produto_usuario_nome; //
        TextView item_produto_usuario_deletar; //

        public VHProduto(View view){
            super(view);
            produto_nome = view.findViewById(R.id.item_produto_nome);
            produto_foto = view.findViewById(R.id.item_produto_foto);
            produto_qtd_likes = view.findViewById(R.id.item_produto_qtd_likes);
            produto_qtd_comment = view.findViewById(R.id.item_produto_qtd_comment);
            produto_preco = view.findViewById(R.id.item_produto_preco);
            likeButtonFeed = view.findViewById(R.id.item_likeButtonFeed);
            produto_comentario = view.findViewById(R.id.item_produto_comentario);
            produto_descricao = view.findViewById(R.id.item_produto_descricao);
            produto_data_hora = view.findViewById(R.id.item_produto_data_hora);

            item_produto_usuario_foto = view.findViewById(R.id.item_produto_usuario_foto);
            item_produto_usuario_nome = view.findViewById(R.id.item_produto_usuario_nome);
            item_produto_usuario_deletar = view.findViewById(R.id.item_produto_usuario_deletar);
        }
    }

    private void recuperarTotalLikes(final VHProduto holder, final Produto produto) {

        DatabaseReference curtidasRef = Firebase.getDatabase()
                .child( "Curtidas" )
                .child( produto.getId() );
        curtidasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int quantidade = (int) dataSnapshot.getChildrenCount();
                holder.produto_qtd_likes.setText( String.valueOf( quantidade ) + " Likes"  );
                //mensagem( quantidade+"" );
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void mensagem(String texto){
        Toast.makeText( contexto, texto, Toast.LENGTH_LONG).show();
    }
}
