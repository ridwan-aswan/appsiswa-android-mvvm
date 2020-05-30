package com.ridwanrsup94atgmaildotcom.appsiswa.api;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.ServerResponse;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.Siswa;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */
public interface IService {

    @GET("getAll")
    Call<List<Siswa>> getAll();

    @POST("saveSiswa/")
    Call<ServerResponse> saveDataSiswa(@Body RequestBody obj);

    @POST("deleteSiswa/")
    @FormUrlEncoded
    Call<ServerResponse> deleteSiswa(@Field("id") long detailID);
}
