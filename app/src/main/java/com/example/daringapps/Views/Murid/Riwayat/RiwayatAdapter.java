package com.example.daringapps.Views.Murid.Riwayat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.daringapps.R;
import java.util.List;

public class RiwayatAdapter extends ArrayAdapter<DataRiwayat> {

    Context context;
    List<DataRiwayat> arrayListDataRiwayat;

    public RiwayatAdapter(@NonNull Context context, List<DataRiwayat> arrayListDataRiwayat) {
        super(context, R.layout.custom_list_riwayat, arrayListDataRiwayat);

        this.context = context;
        this.arrayListDataRiwayat = arrayListDataRiwayat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_riwayat, null, true);

        TextView txtMapel = view.findViewById(R.id.list_mapel);
        TextView txtTanggal = view.findViewById(R.id.list_tgl);
        TextView txtNilai = view.findViewById(R.id.list_nilai);
        TextView txtStat = view.findViewById(R.id.list_stat);
        ImageView statOpen = view.findViewById(R.id.unCheckStat);
        ImageView statClosed = view.findViewById(R.id.checkStat);
//
        txtMapel.setText(arrayListDataRiwayat.get(position).getMapel());
        txtTanggal.setText(arrayListDataRiwayat.get(position).getTanggal());
        txtNilai.setText(arrayListDataRiwayat.get(position).getNilai());
        txtStat.setText(arrayListDataRiwayat.get(position).getStatus());

        final String status = txtStat.getText().toString();

        if (status.equals("closed")){
            statOpen.setVisibility(View.GONE);
            statClosed.setVisibility(View.VISIBLE);
        }
        else{
            statOpen.setVisibility(View.VISIBLE);
            statClosed.setVisibility(View.GONE);
        }

        return view;
    }
}
