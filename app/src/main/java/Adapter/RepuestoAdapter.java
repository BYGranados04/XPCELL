package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xpcell.R;

import java.util.List;

import Model.Repuesto;

public class RepuestoAdapter extends RecyclerView.Adapter<RepuestoAdapter.RepuestoViewHolder> {
    private List<Repuesto> repuestos;

    public RepuestoAdapter(List<Repuesto> repuestos) {
        this.repuestos = repuestos;
    }

    @NonNull
    @Override
    public RepuestoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repuesto, parent, false);
        return new RepuestoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepuestoViewHolder holder, int position) {
        Repuesto repuesto = repuestos.get(position);
        holder.nombreRepuesto.setText(repuesto.getNombre_repuesto());
        holder.precio.setText(String.valueOf(repuesto.getPrecio()));
        holder.stock.setText(String.valueOf(repuesto.getStock()));
        holder.tipoRepuesto.setText(repuesto.getTipo_repuesto());
        holder.modelo.setText(repuesto.getModelo());
    }

    @Override
    public int getItemCount() {
        return repuestos.size();
    }

    public static class RepuestoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreRepuesto;
        TextView precio;
        TextView stock;
        TextView tipoRepuesto;
        TextView modelo;

        public RepuestoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreRepuesto = itemView.findViewById(R.id.nombre_repuesto);
            precio = itemView.findViewById(R.id.precio);
            stock = itemView.findViewById(R.id.stock);
            tipoRepuesto = itemView.findViewById(R.id.tipo_repuesto);
            modelo = itemView.findViewById(R.id.modelo);
        }
    }
}


