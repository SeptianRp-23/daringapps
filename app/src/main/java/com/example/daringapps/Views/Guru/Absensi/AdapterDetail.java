package com.example.daringapps.Views.Guru.Absensi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.daringapps.R;

import java.util.List;

public class AdapterDetail extends ArrayAdapter<DataAbsensiDetail> {

    Context context;
    List<DataAbsensiDetail> arrayListDataAbsensiDtl;

    public AdapterDetail(@NonNull Context context, List<DataAbsensiDetail> arrayListDataAbsensiDtl) {
        super(context, R.layout.custom_absensi_dtl, arrayListDataAbsensiDtl);

        this.context = context;
        this.arrayListDataAbsensiDtl = arrayListDataAbsensiDtl;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_absensi_dtl, null, true);

        TextView txtNama = view.findViewById(R.id.absen_nama);
        TextView txtKelas = view.findViewById(R.id.absen_kelas);
//
        txtNama.setText(arrayListDataAbsensiDtl.get(position).getNama());
        txtKelas.setText(arrayListDataAbsensiDtl.get(position).getKelas());

        return view;
    }
}
