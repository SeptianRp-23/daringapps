package com.example.daringapps.Controllers;

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

public class TugasAdapter extends ArrayAdapter<DataTugas> {
    Context context;
    List<DataTugas> arrayListDataTugas;

    public TugasAdapter(@NonNull Context context, List<DataTugas> arrayListDataTugas) {
        super(context, R.layout.custom_list_tugas, arrayListDataTugas);

        this.context = context;
        this.arrayListDataTugas = arrayListDataTugas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_tugas, null, true);

        TextView txtMapel = view.findViewById(R.id.mapel);
        TextView txtKelas = view.findViewById(R.id.kelas);
        TextView txtTanggal = view.findViewById(R.id.tanggal);

        txtMapel.setText(arrayListDataTugas.get(position).getMapel());
        txtKelas.setText(arrayListDataTugas.get(position).getKelas());
        txtTanggal.setText(arrayListDataTugas.get(position).getTanggal());

        return view;
    }
}
