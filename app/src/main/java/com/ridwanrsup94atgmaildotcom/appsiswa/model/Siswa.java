package com.ridwanrsup94atgmaildotcom.appsiswa.model;
import com.google.gson.annotations.SerializedName;
import com.ridwanrsup94atgmaildotcom.appsiswa.helper.Const;

import java.io.Serializable;

/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */
public class Siswa implements Serializable {

    @SerializedName(Const.KEY_ID)
    long detailID;

    @SerializedName(Const.KEY_NAMA)
    String nama;

    @SerializedName(Const.KEY_ALAMAT)
    String alamat;

    @SerializedName(Const.KEY_TEMPAT_LAHIR)
    String tempatLahir;

    @SerializedName(Const.KEY_TANGGAL_LAHIR)
    String tanggalLahir;

    @SerializedName(Const.KEY_JENIS_KELAMIN)
    String jenisKekamin;

    @SerializedName(Const.KEY_CREATED_DATE)
    String createdDate;

    @SerializedName(Const.KEY_FOTO)
    String foto;

    @SerializedName(Const.KEY_UMUR)
    String umur;

    public long getDetailID() {
        return detailID;
    }

    public void setDetailID(long detailID) {
        this.detailID = detailID;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTempatLahir() {
        return tempatLahir;
    }

    public void setTempatLahir(String tempatLahir) {
        this.tempatLahir = tempatLahir;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getJenisKekamin() {
        return jenisKekamin;
    }

    public void setJenisKekamin(String jenisKekamin) {
        this.jenisKekamin = jenisKekamin;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }
}
