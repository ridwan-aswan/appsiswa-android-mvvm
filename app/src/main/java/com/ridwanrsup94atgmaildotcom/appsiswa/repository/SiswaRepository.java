package com.ridwanrsup94atgmaildotcom.appsiswa.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ridwanrsup94atgmaildotcom.appsiswa.api.APIClient;
import com.ridwanrsup94atgmaildotcom.appsiswa.api.IService;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.ServerResponse;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.Siswa;
import java.util.List;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */

public class SiswaRepository {
    private static final String TAG = "SiswaRepository";
    private static final SiswaRepository instance = new SiswaRepository();
    private IService service;

    public static SiswaRepository getInstance() {
        return instance;
    }

    private SiswaRepository() {
        service = APIClient.getClient().create(IService.class);
    }

    public LiveData<List<Siswa>> setListSiswa() {

        final MutableLiveData<List<Siswa>> data = new MutableLiveData<>();

        service.getAll().enqueue(new Callback<List<Siswa>>() {

            @Override
            public void onResponse(Call<List<Siswa>> call, Response<List<Siswa>> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Siswa>> call, Throwable t) {
                data.setValue(null);
            }

        });

        return data;

    }

    public LiveData<ServerResponse> saveData(RequestBody obj) {

        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        service.saveDataSiswa(obj).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

        return  data;
    }

    public LiveData<ServerResponse> detleteSiswa(long id) {

        final MutableLiveData<ServerResponse> data = new MutableLiveData<>();

        service.deleteSiswa(id).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

        return  data;
    }

}
