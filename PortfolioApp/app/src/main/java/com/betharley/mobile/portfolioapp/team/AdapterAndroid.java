package com.betharley.mobile.portfolioapp.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betharley.mobile.portfolioapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterAndroid extends RecyclerView.Adapter<AdapterAndroid.AndroidViewHolder> {
    private List<Android> lista;
    private Context context;

    public AdapterAndroid(List<Android> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public AndroidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_android, parent, false);

        return new AndroidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AndroidViewHolder holder, int position) {
        Android android = lista.get( position );

        holder.name.setText( android.getName() );
        holder.ano.setText( context.getString(R.string.ano_de_lancamento) + "  " + android.getAno() );

        Glide.with( context ).load( android.getUrl() ).placeholder( R.drawable.image_padrao ).into( holder.imagem );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class AndroidViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageView imagem;
        TextView ano;

        public AndroidViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.item_android_name);
            imagem = itemView.findViewById(R.id.item_android_imagem);
            ano = itemView.findViewById(R.id.item_android_ano);
        }
    }
}
