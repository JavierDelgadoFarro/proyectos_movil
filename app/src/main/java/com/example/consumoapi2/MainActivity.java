package com.example.consumoapi2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ProgressBar progressBar;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputDataEditText = findViewById(R.id.inputData);
        loadDataButton = findViewById(R.id.btnLoadData);
        mJsonTxtView = findViewById(R.id.jsonText);
        mRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        startTime = System.currentTimeMillis();

        loadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullUrl = inputDataEditText.getText().toString();
                // Mostrar ProgressBar al hacer clic en el botón
                progressBar.setVisibility(View.VISIBLE);
                cargarDatos(fullUrl);
            }
        });

        cargarDatos("");
    }

    private void cargarDatos(String fullUrl) {
        String baseUrl = obtenerBaseUrl(fullUrl);
        String endpoint = obtenerEndpoint(fullUrl);

        Retrofit retrofit = construirRetrofit(baseUrl);
        if (retrofit == null) {
            // Ocultar ProgressBar si hay un error
            progressBar.setVisibility(View.GONE);
            return;
        }

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        Call<List<Object>> call = interfaceApi.getData(endpoint);
        realizarLlamadaApi(call);
    }

    private String obtenerBaseUrl(String fullUrl) {
        if (fullUrl.trim().isEmpty()) {
            mostrarMensajeEmergente("Ingrese una URL válida");
            // Ocultar ProgressBar si hay un error
            progressBar.setVisibility(View.GONE);
            return "";
        }

        int endpointIndex = fullUrl.indexOf("get/");
        if (endpointIndex != -1) {
            return fullUrl.substring(0, endpointIndex + 4);
        } else {
            mostrarMensajeEmergente("URL no válida. Debe contener 'get/' en la parte de la API.");
            // Ocultar ProgressBar si hay un error
            progressBar.setVisibility(View.GONE);
            return "";
        }
    }


    private String obtenerEndpoint(String fullUrl) {
        int endpointIndex = fullUrl.indexOf("get/");
        if (endpointIndex != -1) {
            return fullUrl.substring(endpointIndex + 4);
        } else {
            return "";
        }
    }

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

    private void realizarLlamadaApi(Call<List<Object>> call) {
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                Log.d("API Response", "Response code: " + response.code());

                if (response.isSuccessful()) {
                    procesarRespuestaExitosa(response.body());
                } else {
                    mostrarMensajeEmergente("Error en la respuesta: " + response.code());
                }

                calcularTiempoCarga();
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Log.e("API Error", "Error al cargar los datos", t);
                mostrarMensajeEmergente("Error al cargar los datos: " + t.getMessage());
            }
        });
    }
    /*private void procesarRespuestaExitosa(List<?> dataList) {
        assert dataList != null;

        // Obtén los primeros 56 registros (o menos si la lista es más corta)
        List<?> primerosRegistros = dataList.subList(0, Math.min(dataList.size(), 56));

        // Convertir la lista a una cadena JSON
        String jsonData = new Gson().toJson(primerosRegistros);

        // Mostrar la cadena JSON en el TextView
        mJsonTxtView.setText(jsonData);

        // Configurar el adaptador del RecyclerView con los primeros registros
        DataAdapter adapter = new DataAdapter(this, primerosRegistros);
        mRecyclerView.setAdapter(adapter);

        // Mostrar un mensaje si se encontró la data y el número de registros
        mostrarMensajeEmergente("Se encontró la data. Número de registros: " + primerosRegistros.size());

        // Mostrar los datos usando la biblioteca
        DataLibrary.showData(this, jsonData, "Información obtenida de la API");

        calcularTiempoCarga();
    }*/

    private void procesarRespuestaExitosa(List<?> dataList) {
        assert dataList != null;

        // Obtén todos los registros

        // Convertir la lista a una cadena JSON
        String jsonData = new Gson().toJson(dataList);

        // Mostrar la cadena JSON en el TextView
        mJsonTxtView.setText(jsonData);

        // Configurar el adaptador del RecyclerView con todos los registros
        DataAdapter adapter = new DataAdapter(this, dataList);
        mRecyclerView.setAdapter(adapter);

        // Mostrar un mensaje si se encontró la data y el número de registros
        mostrarMensajeEmergente("Se encontró la data. Número de registros: " + dataList.size());

        // Mostrar los datos usando la biblioteca
        DataLibrary.showData(this, jsonData, "Información obtenida de la API");

        calcularTiempoCarga();
    }


    private void calcularTiempoCarga() {
        long endTime = System.currentTimeMillis();
        double elapsedTimeSeconds = (double) (endTime - startTime) / 1000.0;
        mostrarMensajeEmergente("Data cargada en " + elapsedTimeSeconds + " segundos");
    }

    private void mostrarMensajeEmergente(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}
