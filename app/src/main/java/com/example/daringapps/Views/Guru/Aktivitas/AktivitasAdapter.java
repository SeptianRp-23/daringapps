package com.example.daringapps.Views.Guru.Aktivitas;

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

public class AktivitasAdapter extends ArrayAdapter<DataAktivitas> {

    Context context;
    List<DataAktivitas> dataAktivitasList;

    public AktivitasAdapter(@NonNull Context context, List<DataAktivitas> dataAktivitasList) {
        super(context, R.layout.custom_jawab_tugas, dataAktivitasList);

        this.context = context;
        this.dataAktivitasList = dataAktivitasList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_jawab_tugas, null, true);

        TextView txtNama = view.findViewById(R.id.jawab_nama);
        TextView txtKelas = view.findViewById(R.id.jawab_kelas);
        TextView txtMapel = view.findViewById(R.id.jawab_mapel);

        txtNama.setText(dataAktivitasList.get(position).getNama());
        txtKelas.setText(dataAktivitasList.get(position).getKelas());
        txtMapel.setText(dataAktivitasList.get(position).getMapel());

        return view;
    }
}
