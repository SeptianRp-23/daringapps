package com.example.daringapps.Views.Guru.Tugas;

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

public class GuruTugasAdapter extends ArrayAdapter<ItemTugas> {
    Context context;
    List<ItemTugas> arrayListItemTugas;

    public GuruTugasAdapter(@NonNull Context context, List<ItemTugas> arrayListItemTugas) {
        super(context, R.layout.custom_tugas_guru, arrayListItemTugas);

        this.context = context;
        this.arrayListItemTugas = arrayListItemTugas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_tugas_guru, null, true);

        TextView txtMapel = view.findViewById(R.id.guru_mapel);
        TextView txtKelas = view.findViewById(R.id.guru_kelas);
        TextView txtTanggal = view.findViewById(R.id.guru_tgl);

        txtMapel.setText(arrayListItemTugas.get(position).getMapel());
        txtKelas.setText(arrayListItemTugas.get(position).getKelas());
        txtTanggal.setText(arrayListItemTugas.get(position).getTanggal());

        return view;
    }
}
