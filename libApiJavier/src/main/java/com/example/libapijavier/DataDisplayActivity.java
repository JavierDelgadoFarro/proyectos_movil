package com.example.libapijavier;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DataDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        // Obtener referencias a las vistas
        TextView textView = findViewById(R.id.textView);
        TextView titleTextView = findViewById(R.id.titleTextView);

        // Obtener datos de la intención
        DataWrapper<?> dataWrapper = (DataWrapper<?>) getIntent().getSerializableExtra("dataList");

        // Verificar si los datos no son nulos
        if (dataWrapper != null) {
            // Obtener la cadena de datos
            String dataList = dataWrapper.getDataList();

            // Mostrar el título personalizado
            mostrarTitulo(titleTextView);

            // Configurar el texto en el TextView con saltos de línea
            configurarTexto(dataList, textView);
        }
    }

    // Función para mostrar el título personalizado
    private void mostrarTitulo(TextView titleTextView) {
        String title = "Información obtenida de la API";
        titleTextView.setText(title);
        titleTextView.setVisibility(View.VISIBLE);
    }

    // Función para configurar el texto en el TextView con saltos de línea
    private void configurarTexto(String dataList, TextView textView) {
        // Dividir la cadena de datos en un array utilizando saltos de línea como delimitador
        String[] dataArray = dataList.split("\n");

        // Construir la cadena formateada con saltos de línea
        StringBuilder formattedText = new StringBuilder();
        for (String dataItem : dataArray) {
            formattedText.append(dataItem).append("\n");
        }

        // Configurar el texto en el TextView
        textView.setText(formattedText.toString().trim());
    }
}