package com.example.libapijavier;
import android.content.Context;
import android.content.Intent;

public class DataLibrary {

    // Método para iniciar la actividad y mostrar la data
    public static <T> void showData(Context context, String dataList, String title) {
        Intent intent = new Intent(context, DataDisplayActivity.class);
        intent.putExtra("dataList", new DataWrapper<>(dataList, title));
        context.startActivity(intent);
    }

}