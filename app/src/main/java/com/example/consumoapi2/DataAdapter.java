package com.example.consumoapi2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private final List<?> dataList;  // Lista de datos que se mostrarán en el RecyclerView
    private final Context context;    // Contexto de la aplicación

    // Constructor de la clase DataAdapter
    public DataAdapter(Context context, List<?> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    // Método llamado cuando se necesita crear un nuevo ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el diseño del elemento de datos y crea un nuevo ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    // Método llamado para vincular datos a una vista específica en el ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Establece el texto de la TextView en el ViewHolder con el dato correspondiente
        holder.textView.setText(dataList.get(position).toString());
    }

    // Método que devuelve la cantidad total de elementos en la lista de datos
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Clase interna ViewHolder que representa cada elemento de datos en el RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;  // TextView en el diseño del elemento de datos

        // Constructor de la clase ViewHolder
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa la TextView con la referencia al TextView en el diseño
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
