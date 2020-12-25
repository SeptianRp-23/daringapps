package com.example.daringapps.Views.Guru.Laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.daringapps.R;
import com.example.daringapps.Views.Guru.DashboardGuruActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

public class GuruLaporanActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static List<PDFModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_laporan);

        recyclerView = findViewById(R.id.RV);
        list = new ArrayList<>();
        list.add(new PDFModel("Data_Pengguna","https://mydbskripsi.000webhostapp.com/daring-pdf/files/dataPengguna.pdf"));
        list.add(new PDFModel("Data_Siswa", "https://mydbskripsi.000webhostapp.com/daring-pdf/files/dataSiswa.pdf"));
        list.add(new PDFModel("Tugas_Harian", "https://mydbskripsi.000webhostapp.com/daring-pdf/files/tugasHarian.pdf"));
        list.add(new PDFModel("Absensi_Harian", "https://mydbskripsi.000webhostapp.com/daring-pdf/files/absenHarian.pdf"));
        list.add(new PDFModel("Absensi_All", "https://mydbskripsi.000webhostapp.com/daring-pdf/files/absenAll.pdf"));
        list.add(new PDFModel("Data_Tugas", "https://mydbskripsi.000webhostapp.com/daring-pdf/files/dataTugas.pdf"));

        recyclerView.setLayoutManager(new GridLayoutManager(GuruLaporanActivity.this, 2));

        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(GuruLaporanActivity.this,PDFActivity.class);
                //intent.putExtra("url",list.get(position).getPdfUrl());
                intent.putExtra("position",position);
                startActivity(intent);
            }
        };
        PDFAdapter adapter = new PDFAdapter(list,this,itemClickListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GuruLaporanActivity.this, DashboardGuruActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}