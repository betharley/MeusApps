package betharley.catempregos.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import betharley.catempregos.com.R;
import betharley.catempregos.com.model.Anuncio;

public class AdapterAnuncios extends RecyclerView.Adapter<AdapterAnuncios.MyViewHolder> {

    private Context contexto;
    private List<Anuncio> anuncios;

    public AdapterAnuncios(List<Anuncio> anuncio, Context context){
        this.anuncios = anuncio;
        this.contexto = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
        return new MyViewHolder( item );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Anuncio anuncio = anuncios.get( position );
        holder.empresa.setText( anuncio.getEmpresa() );
        holder.cargo.setText( anuncio.getCargo() );

        String descricaoCompleta = anuncio.getAnuncioCompleto();
                /*"" + anuncio.getDescricao() +
                "\n" + anuncio.getSalario() + " " + anuncio.getEmail() +
                " " + anuncio.getTelefone() + " " + anuncio.getSite() +
                "\n" + anuncio.getEstado() + " " + anuncio.getCategoria();*/

        holder.descricao.setText( descricaoCompleta );
        holder.data.setText( anuncio.getData() );
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView empresa;
        TextView cargo;
        TextView descricao;
        TextView data;

        public MyViewHolder(View itemView){
            super(itemView);

            empresa   = itemView.findViewById(R.id.item_lista_empresa);
            cargo     = itemView.findViewById(R.id.item_lista_cargo);
            descricao = itemView.findViewById(R.id.item_lista_descricao);
            data      = itemView.findViewById(R.id.item_lista_data);
        }
    }
}
