package com.betharley.mobile.ecommerceonline.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.ecommerceonline.R;
import com.betharley.mobile.ecommerceonline.helper.Firebase;
import com.betharley.mobile.ecommerceonline.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdaptadorOrdem extends RecyclerView.Adapter<AdaptadorOrdem.ViewHolderOrdem>{
    private ArrayList<Produto> lista;
    private Context contexto;

    public AdaptadorOrdem(ArrayList<Produto> lista, Context contexto){
        this.lista = lista;
        this.contexto = contexto;
    }
    @Override
    public ViewHolderOrdem onCreateViewHolder(ViewGroup parent, int typeView){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_enviado, parent, false);
        ViewHolderOrdem ordem = new ViewHolderOrdem(view);
        return ordem;
    }

    @Override
    public void onBindViewHolder(ViewHolderOrdem holder, int position){
        final Produto produto = lista.get( position );

        holder.ordem_nome.setText( produto.getNome() );
        holder.ordem_data_hora.setText( "Adicionado em " + produto.getData() + " " + produto.getHora() );
        holder.ordem_nome_telefone.setText( "Nome: "+ produto.getUsuario().getNome() + "  Contato: " + produto.getUsuario().getTelefone() );
        holder.ordem_endereco.setText("Local e Horário de Entrega: " + produto.getUsuario().getEndereco() );

        holder.ordem_quantidade.setText( produto.getQuantidade()  +" X " );
        holder.ordem_preco.setText( "R$: " + produto.getPreco() );
        holder.ordem_preco_total.setText( "Total: R$: " + produto.getTotal() );
        holder.ordem_status.setText( "Status: " + produto.getStatus() );


        String imagem = produto.getFoto();
        if( imagem != null && !imagem.isEmpty() ){
            Picasso.get().load( imagem ).into( holder.ordem_imagem );
        }

        if( produto.getStatus().equals("Finalizado") || produto.getStatus().equals("Histórico") ){
            holder.ordem_apagar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mensagem("Botao apagar clicado");
                    removerProduto(produto);
                }
            });
        }


        /*holder.ordem_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mensagem("STATUS");
               // mudarStatusProduto("Recebido", ordem);
            }
        });*/

        holder.ordem_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] opcoes = new CharSequence[]
                        { "Processando", "Saiu para Entrega", "Entregue",
                                "Pago", "Finalizado" };

                AlertDialog.Builder dialog = new AlertDialog.Builder(contexto);
                dialog.setTitle( "Selecione a Opção do Produto:" );

                dialog.setItems(opcoes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if( i == 0 ){
                            mudarStatusProduto("Preparrando", produto);
                        }else if( i == 1 ){
                            mudarStatusProduto("Saiu para Entrega", produto);
                        }else if( i == 2 ){
                            mudarStatusProduto("Entregue", produto);
                        }else if( i == 3 ){
                            mudarStatusProduto("Pago", produto);
                        }else if( i == 4 ){
                            mudarStatusProduto("Finalizado", produto);
                        }
                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount(){
        return lista.size();
    }

    public class ViewHolderOrdem extends RecyclerView.ViewHolder{
        ImageView ordem_imagem;
        TextView ordem_nome;
        TextView ordem_quantidade;
        TextView ordem_preco;
        TextView ordem_preco_total;
        TextView ordem_data_hora;
        TextView ordem_nome_telefone;
        TextView ordem_endereco;
        TextView ordem_status;
        TextView ordem_apagar;

        public ViewHolderOrdem(View view){
            super(view);
            ordem_imagem        = view.findViewById(R.id.item_ordem_imagem);
            ordem_nome          = view.findViewById(R.id.item_ordem_nome);
            ordem_quantidade    = view.findViewById(R.id.item_ordem_quantidade);
            ordem_preco         = view.findViewById(R.id.item_ordem_preco);
            ordem_preco_total   = view.findViewById(R.id.item_ordem_preco_total);
            ordem_data_hora     = view.findViewById(R.id.item_ordem_data_hora);
            ordem_nome_telefone = view.findViewById(R.id.item_ordem_nome_telefone);
            ordem_endereco      = view.findViewById(R.id.item_ordem_endereco);
            ordem_status        = view.findViewById(R.id.item_ordem_status);
            ordem_apagar        = view.findViewById(R.id.item_ordem_deletar);
        }
    }

    private void mudarStatusProduto(final String status, final Produto produto){
        DatabaseReference statusRef = Firebase.getDatabase()
                .child("Enviados")
                .child( produto.getVendedor().getId() )
                .child( produto.getId() );

        final HashMap<String, Object> mapa = new HashMap<>();
        mapa.put( "status", status );
        statusRef.updateChildren( mapa )
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        DatabaseReference statusUsuarioRef = Firebase.getDatabase()
                                .child( "Pedidos" )
                                .child( produto.getUsuario().getId() )
                                .child( produto.getId() );

                        statusUsuarioRef.updateChildren( mapa ).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if( task.isSuccessful() ){
                                    mensagem( "Produto atualizado para " + status );
                                }
                            }
                        });
                    }
                });
    }

    private void removerProduto(final Produto produto){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle("Apagar Produto");
        builder.setMessage( "Desenha realmente apagar esse produto ?" );
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference removerRef = Firebase.getDatabase()
                        .child( "Enviados" )
                        .child( produto.getVendedor().getId() )
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
