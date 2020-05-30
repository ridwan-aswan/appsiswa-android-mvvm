package com.ridwanrsup94atgmaildotcom.appsiswa.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ridwanrsup94atgmaildotcom.appsiswa.model.ServerResponse;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.Siswa;
import com.ridwanrsup94atgmaildotcom.appsiswa.repository.SiswaRepository;

import java.util.List;

import okhttp3.RequestBody;


/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */
public class MainActivityViewModel extends ViewModel {

    private SiswaRepository repository;

    public MainActivityViewModel() {
        super();
        repository = SiswaRepository.getInstance();
    }

    public LiveData<List<Siswa>> getloadSiswa() {
        return repository.setListSiswa();
    }

    public LiveData<ServerResponse> deleteSiswa(long id) {
        return repository.detleteSiswa(id);
    }

    public LiveData<ServerResponse> saveSiswa(RequestBody obj) {
        return repository.saveData(obj);
    }



}
