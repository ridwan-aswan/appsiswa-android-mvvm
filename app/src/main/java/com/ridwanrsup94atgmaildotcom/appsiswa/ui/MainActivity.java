package com.ridwanrsup94atgmaildotcom.appsiswa.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ridwanrsup94atgmaildotcom.appsiswa.adapters.SiswaAdapter;
import com.ridwanrsup94atgmaildotcom.appsiswa.api.ApiServer;
import com.ridwanrsup94atgmaildotcom.appsiswa.BuildConfig;
import com.ridwanrsup94atgmaildotcom.appsiswa.helper.Const;
import com.ridwanrsup94atgmaildotcom.appsiswa.helper.Helper;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.ServerResponse;
import com.ridwanrsup94atgmaildotcom.appsiswa.model.Siswa;
import com.ridwanrsup94atgmaildotcom.appsiswa.R;
import com.ridwanrsup94atgmaildotcom.appsiswa.utils.FileCompressor;
import com.ridwanrsup94atgmaildotcom.appsiswa.viewmodel.MainActivityViewModel;
import com.ridwanrsup94atgmaildotcom.appsiswa.databinding.ActivityMainBinding;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private SiswaAdapter adapter;
    private MainActivityViewModel viewModel;
    private Helper helper;
    private long detail_id = 0;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GALLERY_PHOTO = 2;
    private File mPhotoFile;
    private FileCompressor mCompressor;
    private ActivityMainBinding  bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        viewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel.class);
        bind.rvMain.setHasFixedSize(true);
        bind.rvMain.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        initViews();
        loadDataSiswa();
        clearForm();

    }

    private void initViews() {

        helper = new Helper();
        mCompressor = new FileCompressor(MainActivity.this);
        bind.profileMainImage.setOnClickListener(v -> selectImage());

        bind.tvTglLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Objects.requireNonNull(MainActivity.this),
                        MainActivity.this, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        bind.tvTglLahir.setText(helper.getDate());
        bind.mainRefresh.setOnRefreshListener(() -> {
            loadDataSiswa();
            bind.mainRefresh.setRefreshing(false);
        });
        bind.mainSpin.setVisibility(View.INVISIBLE);
        bind.btnMainSave.setOnClickListener(v-> saveDataSiswa(detail_id));
        bind.btnMainCancel.setOnClickListener(v->clearForm());
        bind.btnMainDelete.setOnClickListener(v-> deleteSiswa(detail_id));
    }

    private void loadDataSiswa() {

        bind.mainSpin.setVisibility(View.VISIBLE);
        viewModel.getloadSiswa().observe(MainActivity.this, new Observer<List<Siswa>>() {
            @Override
            public void onChanged(List<Siswa> list) {

                adapter = new SiswaAdapter(MainActivity.this, list);
                bind.rvMain.setAdapter(adapter);
                bind.mainSpin.setVisibility(View.INVISIBLE);
                adapter.setOnItemClick(new SiswaAdapter.OnItemClick() {
                    @Override
                    public void getSiswa(Siswa siswa) {
                        getDetailSiswa(siswa);
                        detail_id = siswa.getDetailID();
                    }
                });
            }
        });

    }

    private void getDetailSiswa(Siswa siswa) {

        try {

            bind.tvNama.setText(siswa.getNama());
            bind.tvTmpLahir.setText(siswa.getTempatLahir());
            bind.tvTglLahir.setText(helper.setDateParsing(siswa.getTanggalLahir()));
            bind.tvAlamat.setText(siswa.getAlamat());
            if ("LAKI-LAKI".equals(siswa.getJenisKekamin())) {

                bind.swicthMain.setChecked(true);

            } else {

                bind.swicthMain.setChecked(false);

            }

            Glide.with(MainActivity.this)
                    .load(ApiServer.BASE_URL_IMAGE + siswa.getFoto())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_camera)
                            .error(R.drawable.ic_camera))
                            .into(bind.profileMainImage);
            bitmapToFile(siswa.getFoto());

        } catch (ParseException   e) {
            e.printStackTrace();
        }

    }


    private void deleteSiswa(long id) {

        if (id == 0) {
            Toast.makeText(this, "Tidak ada data yang dihapus !", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.deleteSiswa(id).observe(MainActivity.this, new Observer<ServerResponse>() {
            @Override
            public void onChanged(ServerResponse serverResponse) {

                Toast.makeText(MainActivity.this, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                clearForm();
                loadDataSiswa();

            }
        });
    }

    private void clearForm() {
        bind.tvNama.requestFocus();
        bind.tvNama.setText(null);
        bind.tvAlamat.setText(null);
        bind.tvTmpLahir.setText(null);
        bind.swicthMain.setChecked(false);
        bind.profileMainImage.setImageResource(R.drawable.ic_camera);
        mPhotoFile = null;
        detail_id = 0;
    }

    private void saveDataSiswa(long id) {

        String alert = "Lengkapi data !";
        String jenisKelamin;

        String nama = bind.tvNama.getText().toString();
        String alamat = bind.tvAlamat.getText().toString();
        String tempatLahir = bind.tvTmpLahir.getText().toString();
        String tanggalLahir = bind.tvTglLahir.getText().toString();

        if (bind.swicthMain.isChecked()) {

            jenisKelamin = "LAKI-LAKI";

        } else {

            jenisKelamin = "PEREMPUAN";
        }

        if (nama.isEmpty()) {
            bind.tvNama.setError(alert);
            bind.tvNama.requestFocus();
            return;
        }
        if (alamat.isEmpty()) {
            bind.tvAlamat.setError(alert);
            bind.tvAlamat.requestFocus();
            return;
        }

        if (tempatLahir.isEmpty()) {
            bind.tvTmpLahir.setError(alert);
            bind.tvTmpLahir.requestFocus();
            return;
        }

        if (mPhotoFile == null) {
            selectImage();
            return;
        }


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart(Const.KEY_ID, String.valueOf(id));
        builder.addFormDataPart(Const.KEY_NAMA, nama);
        builder.addFormDataPart(Const.KEY_ALAMAT, alamat);
        builder.addFormDataPart(Const.KEY_TEMPAT_LAHIR, tempatLahir);
        builder.addFormDataPart(Const.KEY_JENIS_KELAMIN, jenisKelamin);
        builder.addFormDataPart(Const.KEY_TANGGAL_LAHIR, helper.ConvertDate(tanggalLahir, "yyyy-MM-dd"));
        builder.addFormDataPart(Const.KEY_FILE, mPhotoFile.getName(), RequestBody.create(MultipartBody.FORM, mPhotoFile));

        RequestBody requestBody = builder.build();

        viewModel.saveSiswa(requestBody).observe(MainActivity.this, new Observer<ServerResponse>() {

            @Override
            public void onChanged(ServerResponse serverResponse) {

                Toast.makeText(MainActivity.this, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();

                clearForm();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadDataSiswa();
                    }
                },1000);


            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String sMonth = Integer.toString(month + 1);
        String sDay = Integer.toString(dayOfMonth);
        month++;
        if ((month + 1) < 10) {

            sMonth = "0" + month;
        }
        if (dayOfMonth < 10) {

            sDay = "0" + dayOfMonth;
        }

        @SuppressLint("DefaultLocale") String date = String.format("%s/%s/%d", sDay, sMonth, year);
        bind.tvTglLahir.setText(date);

    }

    private void bitmapToFile(String bitmap) {

        Glide.with(this)
                .asBitmap()
                .load(ApiServer.BASE_URL_IMAGE + bitmap)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        //create a file to write bitmap data
                        File f = new File(MainActivity.this.getCacheDir(), bitmap);

                        try {
                            f.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Bitmap bitmap = resource;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(f);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(bitmapdata);
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mPhotoFile = f;

                    }
                });
    }

    /**
     * Alert dialog for capture or select from galley
     */
    private void selectImage() {
        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galery",
                "Batal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Ambil Foto")) {
                requestStoragePermission(true);
            } else if (items[item].equals("Pilih dari Galery")) {
                requestStoragePermission(false);
            } else if (items[item].equals("Batal")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(MainActivity.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_camera)).into(bind.profileMainImage);
            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(MainActivity.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_camera)).into(bind.profileMainImage);

            }
        }
    }


    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }


    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
