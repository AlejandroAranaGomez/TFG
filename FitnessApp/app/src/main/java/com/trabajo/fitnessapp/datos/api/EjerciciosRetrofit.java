package com.trabajo.fitnessapp.datos.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EjerciciosRetrofit {

    private static final String URL = "https://exercisedb.p.rapidapi.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES) // Tiempo para conectar
                .readTimeout(1, TimeUnit.MINUTES)    // Tiempo para esperar datos
                .writeTimeout(1, TimeUnit.MINUTES)   // Tiempo para enviar datos
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
