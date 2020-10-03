package com.betharley.mobile.ecommerceonline.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorPedidos extends RecyclerView.Adapter<AdaptadorPedidos.ViewHolderPedidos> {
    private ArrayList<Produto> lista;
    private Context contexto;

    public AdaptadorPedidos(ArrayList<Produto> lista, Context contexto){
        this.lista = lista;
        this.contexto = contexto;
    }

    @Override
    public ViewHolderPedidos onCreateViewHolder(ViewGroup parent, int typeView){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        ViewHolderPedidos produto = new ViewHolderPedidos(view);
        return produto;
    }
    @Override
    public void onBindViewHolder(ViewHolderPedidos holder, int position){
        final Produto produto = lista.get(position);

        //int valorTotal = pedido.getQuantidade() * pedido.getPreco();


        holder.pedido_nome.setText( produto.getNome() );
        holder.pedido_data_hora.setText( "Adicionado em " + produto.getData() + " " + produto.getHora() );
        holder.pedido_nome_contato.setText( "Nome: "+ produto.getUsuario().getNome() + "  Telefone: " + produto.getUsuario().getTelefone() );
        holder.pedido_endereco.setText( "Horário e Local da Entrega: " + produto.getUsuario().getEndereco() );
        holder.pedido_status.setText( "Status: " + produto.getStatus() );

        holder.pedido_quantidade.setText( produto.getQuantidade()  +" X " );
        holder.pedido_preco.setText( "R$: " + produto.getPreco() );
        holder.pedido_preco_total.setText( "Total: R$: " + produto.getTotal() );


        String imagem = produto.getFoto();
        if( imagem != null && !imagem.isEmpty() ){
            Picasso.get().load( imagem ).into( holder.pedido_imagem );
        }

        if( produto.getStatus().equals("Finalizado")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mensagem("Botao apagar clicado");
                    removerProduto(produto);
                }
            });
        }
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    public class ViewHolderPedidos extends RecyclerView.ViewHolder{
        ImageView pedido_imagem;
        TextView pedido_nome;
        TextView pedido_quantidade;
        TextView pedido_preco;
        TextView pedido_preco_total;
        TextView pedido_data_hora;
        TextView pedido_nome_contato;
        TextView pedido_endereco;
        TextView pedido_status;

        public ViewHolderPedidos(View view){
            super(view);

            pedido_imagem        = view.findViewById(R.id.item_pedido_imagem);
            pedido_nome          = view.findViewById(R.id.item_pedido_nome);
            pedido_quantidade    = view.findViewById(R.id.item_pedido_quantidade);
            pedido_preco         = view.findViewById(R.id.item_pedido_preco);
            pedido_preco_total   = view.findViewById(R.id.item_pedido_preco_total);
            pedido_data_hora     = view.findViewById(R.id.item_pedido_data_hora);
            pedido_nome_contato  = view.findViewById(R.id.item_pedido_nome_contato);
            pedido_endereco      = view.findViewById(R.id.item_pedido_endereco);
            pedido_status        = view.findViewById(R.id.item_pedido_status);
        }
    }

    private void removerProduto(final Produto produto){

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Apagar Produto");
        builder.setMessage("Deseja realmente apagar esse produto ?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference removerRef = Firebase.getDatabase()
                        .child( "Pedidos" )
                        .child( produto.getUsuario().getId() )
                        .child( produto.getId() );
                removerRef.removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if( task.isSuccessful() ){
                                    mensagem("Produto removido com sucesso...");
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    private void mensagem(String texto){
        Toast.makeText(contexto, texto, Toast.LENGTH_LONG).show();
    }
}
