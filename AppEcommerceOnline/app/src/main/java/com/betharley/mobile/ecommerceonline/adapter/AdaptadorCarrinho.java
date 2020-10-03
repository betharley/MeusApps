package com.betharley.mobile.ecommerceonline.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.ecommerceonline.ConfirmarEnviarActivity;
import com.betharley.mobile.ecommerceonline.EditarActivity;
import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;

public class AdaptadorCarrinho extends RecyclerView.Adapter<AdaptadorCarrinho.ViewHolderCarrinho>{

    private ArrayList<Produto> lista;
    private Context contexto;

    public AdaptadorCarrinho(Context contexto, ArrayList<Produto> lista){
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public ViewHolderCarrinho onCreateViewHolder(ViewGroup parent, int typeView){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrinho, parent, false);
        ViewHolderCarrinho item = new ViewHolderCarrinho(view);
        return item;
    }

    @Override
    public void onBindViewHolder(final ViewHolderCarrinho holder, int position){
        final Produto produto = lista.get( position );

        int valorTotal = produto.getQuantidade() * produto.getPreco();

        holder.carrinho_nome.setText( produto.getNome() );
        holder.carrinho_quantidade.setText( produto.getQuantidade() +" X " );
        holder.carrinho_preco.setText( "R$: " + produto.getPreco() );
        holder.carrinho_preco_total.setText( "Total: R$: " + valorTotal  );
        holder.carrinho_data_hora.setText( "Adicionado em " + produto.getData() +"  " + produto.getHora() );
        holder.carrinho_status.setText( "Status: " + produto.getStatus() );

        String imagem = produto.getFoto();
        if( imagem != null && !TextUtils.isEmpty(imagem) ){
            Picasso.get().load( imagem ).into( holder.carinho_imagem );
        }


        holder.carrinho_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, EditarActivity.class);
                intent.putExtra("produto", produto);
                contexto.startActivity( intent );
            }
        });

        holder.carrinho_remover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(contexto);
                dialog.setTitle("Remover Produto");
                dialog.setMessage( "Deseja realmente remover este produto do seu carinho ?" );

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Paper.init( contexto );
                        final DatabaseReference removerRef = Firebase.getDatabase()
                                .child( "Carrinho" )
                                .child( produto.getUsuario().getId() )
                                .child( produto.getId() );

                        removerRef.removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if( task.isSuccessful() ){
                                            Toast.makeText(contexto, "Produto removido com sucesso...", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
                dialog.setNegativeButton( "Não", null);
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        holder.carrinho_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, ConfirmarEnviarActivity.class);
                intent.putExtra("produto", produto);
                contexto.startActivity( intent );
            }
        });
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    public class ViewHolderCarrinho extends RecyclerView.ViewHolder {
        ImageView carinho_imagem;
        TextView carrinho_nome;
        TextView carrinho_quantidade;
        TextView carrinho_preco;
        TextView carrinho_data_hora;
        TextView carrinho_status;
        TextView carrinho_preco_total;

        TextView carrinho_editar;
        TextView carrinho_remover;
        TextView carrinho_enviar;

        public ViewHolderCarrinho(View view){
            super(view);

            carinho_imagem          = view.findViewById(R.id.item_carrinho_foto);
            carrinho_nome           = view.findViewById(R.id.item_carrinho_nome);
            carrinho_quantidade     = view.findViewById(R.id.item_carrinho_quantidade);
            carrinho_preco          = view.findViewById(R.id.item_carrinho_preco);
            carrinho_data_hora      = view.findViewById(R.id.item_carrinho_data_hora);
            carrinho_status         = view.findViewById(R.id.item_carrinho_status);
            carrinho_preco_total    = view.findViewById(R.id.item_carrinho_preco_total);

            carrinho_editar         = view.findViewById(R.id.item_carrinho_editar);
            carrinho_remover        = view.findViewById(R.id.item_carrinho_remover);
            carrinho_enviar         = view.findViewById(R.id.item_carrinho_enviar);
        }
    }
}
