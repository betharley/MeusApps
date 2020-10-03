package com.betharley.mobile.ecommerceonline.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.ecommerceonline.AlterarActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.betharley.mobile.ecommerceonline.vendedor.AdicionarActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class AdaptadorPublicar extends RecyclerView.Adapter<AdaptadorPublicar.VHPublicar>{

    private ArrayList<Produto> lista;
    private Context contexto;

    public AdaptadorPublicar(ArrayList<Produto> lista, Context contexto){
        this.lista = lista;
        this.contexto = contexto;
    }

    @Override
    public VHPublicar onCreateViewHolder(ViewGroup parent, int typeView){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicar, parent, false);
        VHPublicar vhPublicar = new VHPublicar(view);
        return vhPublicar;
    }

    @Override
    public void onBindViewHolder(final VHPublicar holder, final int position){
        final Produto produto = lista.get( position );

        holder.item_nome.setText( produto.getNome() );
        holder.item_data_hora.setText( produto.getData() + " "+ produto.getHora() );
        holder.item_preco.setText( "R$: " + String.valueOf( produto.getPreco() ) );
        holder.item_descricao.setText( produto.getDescricao() );

        if( produto.getFoto() != null && produto.getFoto() != "" ){
            Picasso.get().load( produto.getFoto() ).into( holder.item_foto );
        }

        holder.item_progressBar2.setVisibility( View.INVISIBLE );

        holder.item_botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.item_progressBar2.setVisibility( View.VISIBLE );
                holder.item_botao.setEnabled( false );

                if ( produto.publicar() ){
                    mensagem("Produto Publicado com Sucesso!!!");
                }else{
                    mensagem("Erro ao Publicar Produto, verifique sua Internet");
                    holder.item_progressBar2.setVisibility( View.INVISIBLE );
                    holder.item_botao.setEnabled( true );
                }
            }
        });

        holder.item_remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                builder.setTitle( "Excluir" );
                builder.setMessage( "Deseja excluir esse produto da sua lista ?" );
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( produto.remover() ){

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
                        }else{
                            mensagem("Erro ao remover produto. Tente novamente");
                        }
                    }
                }).setNegativeButton( "Não", null);
                builder.show();
            }
        });

        holder.item_alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mensagem("Ainda não programado");
                Intent intent = new Intent(contexto, AlterarActivity.class);

                //intent.putExtra( "chave", produto.getId() );
                intent.putExtra( "produto", produto );
                contexto.startActivity( intent );
            }
        });
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    public class VHPublicar extends RecyclerView.ViewHolder{
        TextView item_nome; //
        ImageView item_foto; //
        TextView item_data_hora; //
        TextView item_preco; //
        TextView item_descricao; //
        Button item_alterar;
        Button item_remover;
        Button item_botao;
        ProgressBar item_progressBar2;

        public VHPublicar(View view){
            super(view);

            item_nome = view.findViewById(R.id.item_publicar_nome);
            item_foto = view.findViewById(R.id.item_publicar_foto);
            item_data_hora = view.findViewById(R.id.item_publicar_data_hora);
            item_preco = view.findViewById(R.id.item_publicar_preco);
            item_descricao = view.findViewById(R.id.item_publicar_descricao);
            item_alterar = view.findViewById(R.id.item_publicar_alterar);
            item_remover = view.findViewById(R.id.item_publicar_remover);
            item_botao = view.findViewById(R.id.item_publicar_botao);
            item_progressBar2 = view.findViewById(R.id.item_publicar_progressBar2);
        }
    }

    private void mensagem(String texto){
        Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show();
    }
}
