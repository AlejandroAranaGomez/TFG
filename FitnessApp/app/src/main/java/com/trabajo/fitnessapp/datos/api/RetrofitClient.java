package com.trabajo.fitnessapp.datos.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Clase para la conexion con la api.
public class RetrofitClient {

    private static final String URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
