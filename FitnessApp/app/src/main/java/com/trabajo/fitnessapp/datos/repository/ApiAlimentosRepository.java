package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.api.AlimentosRetrofit;
import com.trabajo.fitnessapp.datos.api.ApiAlimentosService;
import com.trabajo.fitnessapp.datos.dto.ApiAlimentosDTO;
import com.trabajo.fitnessapp.datos.dto.ApiAlimentosResponse;
import com.trabajo.fitnessapp.datos.dto.ApiHints;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class ApiAlimentosRepository {

    private static final String APP_ID = "8012224d";
    private static final String APP_KEY = "f76d81e9514964e660b62295cd3ada32";
    private final ApiAlimentosService apiAlimentosService;
    public ApiAlimentosRepository() {
        this.apiAlimentosService = AlimentosRetrofit.getClient().create(ApiAlimentosService.class);
    }

    public LiveData<List<ApiAlimentosDTO>> buscarAlimentos(String nombre) {
        MutableLiveData<List<ApiAlimentosDTO>> listaAlimentosConsulta = new MutableLiveData<>();

        apiAlimentosService.buscarAlimentos(APP_ID, APP_KEY, nombre).enqueue(new Callback<ApiAlimentosResponse>() {
            @Override
            public void onResponse(Call<ApiAlimentosResponse> call, Response<ApiAlimentosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ApiAlimentosDTO> listaVacia = new ArrayList<>();

                    if (response.body().getHints() != null) {
                        for (ApiHints hint : response.body().getHints()) {
                            ApiAlimentosDTO alimento = new ApiAlimentosDTO();

                            alimento.setNombre(hint.getFood().getLabel());
                            alimento.setImageUrl(hint.getFood().getImage());

                            alimento.setCalorias(hint.getFood().getNutrients().getCalorias().floatValue());
                            alimento.setProteinas(hint.getFood().getNutrients().getProteinas().floatValue());
                            alimento.setCarbohidratos(hint.getFood().getNutrients().getCarbohidratos().floatValue());
                            alimento.setGrasas(hint.getFood().getNutrients().getGrasas().floatValue());

                            listaVacia.add(alimento);
                        }
                    }
                    listaAlimentosConsulta.setValue(listaVacia);
                } else {
                    Log.e("API_ALIMENTOS", "Error: " + response.code());
                    listaAlimentosConsulta.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ApiAlimentosResponse> call, Throwable t) {
                Log.e("API_ALIMENTOS", "Error buscando los alimentos en la base de datos" + t.getMessage());
                listaAlimentosConsulta.setValue(null);
            }
        });
        return listaAlimentosConsulta;
    }

}
