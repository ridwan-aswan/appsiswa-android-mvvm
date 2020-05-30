package com.ridwanrsup94atgmaildotcom.appsiswa.adapters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ridwanrsup94atgmaildotcom.appsiswa.api.ApiServer;
import com.ridwanrsup94atgmaildotcom.appsiswa.databinding.ItemListSiswaBinding;
import com.ridwanrsup94atgmaildotcom.appsiswa.helper.Helper;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.Siswa;
import com.ridwanrsup94atgmaildotcom.appsiswa.R;

import java.text.ParseException;
import java.util.List;

/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.RecyclerViewHolder > {

    private List<Siswa> siswaList;
    private Context context;
    private  OnItemClick onItemClick;
    private Helper helper = new Helper();
    ItemListSiswaBinding bind;

    public interface OnItemClick { void getSiswa (Siswa siswa); }

    public SiswaAdapter(Context context, List<Siswa> list) {
        this.siswaList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        bind = ItemListSiswaBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new RecyclerViewHolder(bind);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {

        final Siswa siswa = siswaList.get(position);
        holder.bindViews(siswa);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public int getItemCount() {
        return (null != siswaList ? siswaList.size() : 0);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RecyclerViewHolder (@NonNull ItemListSiswaBinding view) {
            super(view.getRoot());

            view.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Siswa siswa = siswaList.get(getAdapterPosition());
                    onItemClick.getSiswa(siswa);
                }
            });
        }


        @SuppressLint("SetTextI18n")
        void  bindViews (Siswa siswa) {

            try {


                bind.tvListNama.setText(siswa.getNama().toUpperCase() + " ( " + siswa.getJenisKekamin() + " )" );
                bind.tvListAlamat.setText(siswa.getAlamat().toUpperCase());
                bind.tvListCreated.setText("Registered Date " +siswa.getCreatedDate());
                bind.tvListUmur.setText(siswa.getUmur() + " TAHUN");
                bind.tvListTglLahir.setText("Tgl.Lahir : " + helper.setDateParsing(siswa.getTanggalLahir()));

                Glide.with(context)
                        .load(ApiServer.BASE_URL_IMAGE + siswa.getFoto())
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.ic_camera)
                                .error(R.drawable.ic_camera)
                                .diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(bind.imageList);

            } catch (ParseException e) {

            }

        }

    }
}