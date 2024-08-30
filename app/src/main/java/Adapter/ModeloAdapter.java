package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.xpcell.R;
import Model.Modelo;
import java.util.List;

public class ModeloAdapter extends RecyclerView.Adapter<ModeloAdapter.ModeloViewHolder> {
    private List<Modelo> modelos;

    public ModeloAdapter(List<Modelo> modelos) {
        this.modelos = modelos;
    }

    @NonNull
    @Override
    public ModeloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modelo, parent, false);
        return new ModeloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModeloViewHolder holder, int position) {
        Modelo modelo = modelos.get(position);
        holder.nombreModelo.setText(modelo.getNombre_modelo()); // Asegúrate de que el método existe
    }

    @Override
    public int getItemCount() {
        return modelos.size();
    }

    public static class ModeloViewHolder extends RecyclerView.ViewHolder {
        TextView nombreModelo;

        public ModeloViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreModelo = itemView.findViewById(R.id.nombre_modelo);
        }
    }
}

