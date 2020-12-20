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
import com.example.daringapps.Views.Guru.Tugas.ItemTugas;

import java.util.List;

public class AbsensiAdapter extends ArrayAdapter<DataAbsensi> {

    Context context;
    List<DataAbsensi> arrayListDataAbsensi;

    public AbsensiAdapter(@NonNull Context context, List<DataAbsensi> arrayListDataAbsensi) {
        super(context, R.layout.custom_absensi, arrayListDataAbsensi);

        this.context = context;
        this.arrayListDataAbsensi = arrayListDataAbsensi;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_absensi, null, true);

        TextView txtTanggal = view.findViewById(R.id.absen_tanggal);
        TextView txtKelas = view.findViewById(R.id.absen_kelas);
        TextView txtWali = view.findViewById(R.id.absen_wali);
//
        txtTanggal.setText(arrayListDataAbsensi.get(position).getTanggal());
        txtKelas.setText(arrayListDataAbsensi.get(position).getKelas());
        txtWali.setText(arrayListDataAbsensi.get(position).getNama());

        return view;
    }
}
