package com.example.consumoapi2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libapijavier.DataLibrary;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText inputDataEditText;
    private Button loadDataButton;
    private TextView mJsonTxtView;
    private RecyclerView mRecyclerView;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas y configurar el RecyclerView
        inputDataEditText = findViewById(R.id.inputData);
        loadDataButton = findViewById(R.id.btnLoadData);
        mJsonTxtView = findViewById(R.id.jsonText);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        startTime = System.currentTimeMillis();

        // Configurar el evento de clic del botón para cargar datos
        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullUrl = inputDataEditText.getText().toString();
                cargarDatos(fullUrl);
            }
        });

        // Cargar datos con una URL vacía al inicio
        cargarDatos("");
    }

    // Método para cargar datos desde la API
    private void cargarDatos(String fullUrl) {
        String baseUrl = obtenerBaseUrl(fullUrl);
        String endpoint = obtenerEndpoint(fullUrl);

        // Construir Retrofit y realizar la llamada a la API
        Retrofit retrofit = construirRetrofit(baseUrl);
        if (retrofit == null) {
            return;
        }

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        Call<List<Object>> call = interfaceApi.getData(endpoint);
        realizarLlamadaApi(call);
    }

    // Método para obtener la URL base de la API
    private String obtenerBaseUrl(String fullUrl) {
        if (fullUrl.trim().isEmpty()) {
            mostrarMensajeEmergente("Ingrese una URL válida");
            return "";
        }

        int endpointIndex = fullUrl.indexOf("get/");
        if (endpointIndex != -1) {
            return fullUrl.substring(0, endpointIndex + 4); // Incluyendo "get/"
        } else {
            mostrarMensajeEmergente("URL no válida. Debe contener 'get/' en la parte de la API.");
            return "";
        }
    }

    // Método para obtener el endpoint de la API
    private String obtenerEndpoint(String fullUrl) {
        int endpointIndex = fullUrl.indexOf("get/");
        if (endpointIndex != -1) {
            return fullUrl.substring(endpointIndex + 4); // Excluyendo "get/"
        } else {
            return "";
        }
    }

    // Método para construir un objeto Retrofit
    private Retrofit construirRetrofit(String baseUrl) {
        if (baseUrl.isEmpty()) {
            return null;
        }

        Log.d("Retrofit", "URL base final: " + baseUrl);

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // Método para realizar la llamada a la API y gestionar la respuesta
    private void realizarLlamadaApi(Call<List<Object>> call) {
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    procesarRespuestaExitosa(response.body());
                } else {
                    mostrarMensajeEmergente("Error en la respuesta: " + response.code());
                }
                calcularTiempoCarga();
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                mostrarMensajeEmergente("Error al cargar los datos: " + t.getMessage());
            }
        });
    }

    // Método para procesar la respuesta exitosa de la API
    private void procesarRespuestaExitosa(List<?> dataList) {
        assert dataList != null;

        // Limitar la lista a 25 elementos y mostrar utilizando la librería DataLibrary
        List<?> truncatedList = dataList.subList(0, Math.min(dataList.size(), 25));
        DataLibrary.showData(MainActivity.this, truncatedList.toString(), "Datos de la API");
        // Convertir la lista truncada a una cadena JSON
        String jsonData = new Gson().toJson(truncatedList);
        // Convierte la lista completa a formato JSON
        //String jsonData = new Gson().toJson(dataList);
        // Mostrar la cadena JSON en el TextView
        mJsonTxtView.setText(jsonData);
        calcularTiempoCarga();
    }

    // Método para calcular y mostrar el tiempo de carga de datos
    private void calcularTiempoCarga() {
        long endTime = System.currentTimeMillis();
        double elapsedTimeSeconds = (double) (endTime - startTime) / 1000.0;
        mostrarMensajeEmergente("Data cargada en " + elapsedTimeSeconds + " segundos");
    }

    // Método para mostrar un mensaje emergente (Toast)
    private void mostrarMensajeEmergente(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
