package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xpcell.R;

import java.util.List;

import Model.Marca;

public class MarcaAdapter extends RecyclerView.Adapter<MarcaAdapter.MarcaViewHolder> {
    private List<Marca> marcas;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int idMarca);
    }

    public MarcaAdapter(List<Marca> marcas, OnItemClickListener onItemClickListener) {
        this.marcas = marcas;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MarcaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marca, parent, false);
        return new MarcaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarcaViewHolder holder, int position) {
        Marca marca = marcas.get(position);
        holder.nombreMarca.setText(marca.getNombre_marca());
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(marca.getId_marca()));
    }

    @Override
    public int getItemCount() {
        return marcas.size();
    }

    public static class MarcaViewHolder extends RecyclerView.ViewHolder {
        TextView nombreMarca;

        public MarcaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMarca = itemView.findViewById(R.id.nombre_marca);
        }
    }
}



