package com.betharley.wesleycatula.mobile.appemprego.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.wesleycatula.mobile.appemprego.AbrirSiteActivity;
import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.activity.VisualizarActivity;
import com.betharley.wesleycatula.mobile.appemprego.model.Vaga;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorVaga extends RecyclerView.Adapter<AdaptadorVaga.ViewHolderPostagem>{

    private ArrayList<Vaga> listaVaga;
    private Context contexto;
    private AdicionarListener adicionarListener;

    public AdaptadorVaga(ArrayList<Vaga> listaVaga, Context contexto, AdicionarListener adicionarListener) {
        this.listaVaga = listaVaga;
        this.contexto = contexto;
        this.adicionarListener = adicionarListener;
    }

    @NonNull
    @Override
    public ViewHolderPostagem onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from( contexto ).inflate(R.layout.item_vaga, parent, false);
        final ViewHolderPostagem viewVaga = new ViewHolderPostagem(view);
        return viewVaga;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPostagem holder, int position) {
        final Vaga vaga = listaVaga.get( position );

        String parteHtml = vaga.getDescription();
        holder.webView_postagem.loadData(parteHtml, "text/html", "UTF-8");

        holder.titulo.setText( vaga.getJobtitle() );
        //holder.descricao.setText( vaga.getDescription() );
        holder.localidade.setText( vaga.getFormattedLocation() );
        holder.data_hora.setText( vaga.getDate() );
        //holder.data.setText( "Por Wesley Morais, " + data );

        holder.nome_empresa.setText( vaga.getCompany() );

        //CARREGAR A IMAGEM

        Picasso.get()
                .load( vaga.getLogo() )
                .placeholder(R.drawable.emprego_centro )
                .into( holder.imagem_empresa );


        holder.link_vaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(contexto, "LINK CLICADO " + vaga.getUrl(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(contexto, AbrirSiteActivity.class);
                intent.putExtra( "siteClicado", vaga.getUrl() );
                contexto.startActivity( intent );
            }
        });

/*
        holder.webView_postagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, VisualizarActivity.class);
                intent.putExtra( "vagaClicada", vaga);
                contexto.startActivity( intent );
            }
        });
        */



        if(position == listaVaga.size() - 5 ){
            //MainActivity.JSON_URL = "https://www.wesleycatula.com/wp-json/wp/v2/posts?page=2";
            //notifyDataSetChanged();
            //Toast.makeText(contexto, "" + lista.size() , Toast.LENGTH_LONG).show();
            //txtCarregarMais.setVisibility( View.VISIBLE );
            //txtCarregarMais.setText("Funcionou");
            adicionarListener.onAdicionar(1);
        }else {
            //txtCarregarMais.setVisibility( View.GONE );
        }

    }

    @Override
    public int getItemCount() {
        return listaVaga.size();
    }

    public static class ViewHolderPostagem extends RecyclerView.ViewHolder{
        WebView webView_postagem;
        CircleImageView imagem_empresa;
        TextView nome_empresa;

        TextView titulo;
        //TextView descricao;
        TextView localidade;
        TextView data_hora;
        TextView link_vaga;

        CardView view_container_vaga;

        public ViewHolderPostagem(@NonNull View itemView) {
            super(itemView);
            webView_postagem = itemView.findViewById(R.id.webView_postagem);

            imagem_empresa       = itemView.findViewById(R.id.item_vaga_imagem_empresa);
            nome_empresa      = itemView.findViewById(R.id.item_vaga_nome_empresa);

            titulo    = itemView.findViewById(R.id.item_vaga_titulo);
            //descricao        = itemView.findViewById(R.id.item_vaga_descricao);
            localidade = itemView.findViewById(R.id.item_vaga_localidade);
            data_hora = itemView.findViewById(R.id.item_vaga_data_hora);
            link_vaga = itemView.findViewById(R.id.item_vaga_link_vaga);

            view_container_vaga        = itemView.findViewById(R.id.view_container_vaga);
        }
    }

    public interface AdicionarListener{
        public void onAdicionar(int tamanho);
    }
}
