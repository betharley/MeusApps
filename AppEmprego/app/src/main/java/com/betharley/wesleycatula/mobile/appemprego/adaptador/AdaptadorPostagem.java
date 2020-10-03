package com.betharley.wesleycatula.mobile.appemprego.adaptador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.betharley.wesleycatula.mobile.appemprego.R;
import com.betharley.wesleycatula.mobile.appemprego.activity.VisualizarActivity;
import com.betharley.wesleycatula.mobile.appemprego.model.Postagem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdaptadorPostagem extends RecyclerView.Adapter<AdaptadorPostagem.ViewHolderPostagem>{

    private ArrayList<Postagem> lista;
    private Context contexto;
    private TextView txtCarregarMais;
    private AdicionarListener adicionarListener;

    public AdaptadorPostagem(ArrayList<Postagem> lista, Context contexto, TextView txtCarregarMais, AdicionarListener adicionarListener) {
        this.lista = lista;
        this.contexto = contexto;
        this.txtCarregarMais = txtCarregarMais;
        this.adicionarListener = adicionarListener;
    }

    @NonNull
    @Override
    public ViewHolderPostagem onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from( contexto ).inflate(R.layout.item_postagem, parent, false);
        final ViewHolderPostagem viewPostagem = new ViewHolderPostagem(view);
/*
        viewPostagem.view_container_postagem.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intentPostagem = new Intent(contexto, AnimeActivity.class);
                Postagem postagem = lista.get( viewType );

                //intentPostagem.putExtra("postagem", postagem);
                intentPostagem.putExtra("anime_name", lista.get(viewPostagem.getAdapterPosition()).getTitle());
                intentPostagem.putExtra("anime_description", lista.get(viewPostagem.getAdapterPosition()).getContent());
                intentPostagem.putExtra("anime_category", lista.get(viewPostagem.getAdapterPosition()).getCategoria());
                intentPostagem.putExtra("anime_studio", lista.get(viewPostagem.getAdapterPosition()).getData());
                intentPostagem.putExtra("anime_episode", lista.get(viewPostagem.getAdapterPosition()).getExcerpt());
                intentPostagem.putExtra("anime_rating", lista.get(viewPostagem.getAdapterPosition()).getSource_url());
                intentPostagem.putExtra("anime_img", lista.get(viewPostagem.getAdapterPosition()).getSource_url());


                contexto.startActivity( intentPostagem );
            }
        });
 */
        return viewPostagem;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPostagem holder, int position) {
        final Postagem postagem = lista.get( position );

        //holder.categoria.setText( postagem.getCategoria() );
        holder.title.setText( postagem.getTitle() );
        holder.conteudo.setText( postagem.getExcerpt() );
        String data = postagem.getData().substring(0, 10);
        data = data.replace("-", "/");
        holder.data.setText( "Por Wesley Morais, " + data );

        //String html = "<html><body>Hello World WebView</body></html>";
        //holder.webView.loadData(html, "text/html", "UTF-8");
        String parteHtml = postagem.getExcerpt();
        holder.webView.loadData(parteHtml, "text/html", "UTF-8");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, VisualizarActivity.class);
                intent.putExtra( "postagem", postagem);
                contexto.startActivity( intent );
            }
        });

        holder.post_ler_mais.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(contexto, VisualizarActivity.class);
                intent.putExtra( "postagem", postagem);
                contexto.startActivity( intent );
            }
        });

        //CARREGAR A IMAGEM
        Glide.with( contexto )
                .load( postagem.getSource_url() )
                .into( holder.imagem );

        if(position == lista.size() - 1){
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
        return lista.size();
    }

    public static class ViewHolderPostagem extends RecyclerView.ViewHolder{
        TextView categoria;
        TextView title;
        ImageView imagem;
        TextView conteudo;
        TextView data;
        TextView post_ler_mais;

        CardView view_container_postagem;
        WebView webView;


        public ViewHolderPostagem(@NonNull View itemView) {
            super(itemView);

            //categoria   = itemView.findViewById(R.id.post_categoria);
            title       = itemView.findViewById(R.id.post_titulo);
            imagem      = itemView.findViewById(R.id.post_imagem);
            conteudo    = itemView.findViewById(R.id.post_conteudo);
            data        = itemView.findViewById(R.id.post_data);
            post_ler_mais = itemView.findViewById(R.id.post_ler_mais);

            view_container_postagem        = itemView.findViewById(R.id.view_container_postagem);
            webView        = itemView.findViewById(R.id.webView_postagem);
        }
    }

    public interface AdicionarListener{
        public void onAdicionar(int tamanho);
    }
}
