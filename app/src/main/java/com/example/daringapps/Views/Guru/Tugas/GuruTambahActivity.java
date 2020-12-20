package com.example.daringapps.Views.Guru.Tugas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.daringapps.Helper.Server;
import com.example.daringapps.R;
import com.example.daringapps.Views.Murid.Tugas.TugasActivity;
import com.example.daringapps.Views.Murid.Tugas.TugasDetailActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GuruTambahActivity extends AppCompatActivity {

    TextView tanggal;
    Spinner spMapel, spKelas;
    MaterialEditText mt1, mt2, mt3, mt4, mt5, mt6, mt7, mt8, mt9, mt10;
    Button btSimpan;
    String myFormat = "dd MMMM yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    private String InsertAPI = Server.URL_API + "insertSoal.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_tambah);

        tanggal = findViewById(R.id.tanggal);
        spMapel = findViewById(R.id.listmapel);
        spKelas = findViewById(R.id.listkelas);
        mt1 = findViewById(R.id.p1);
        mt2 = findViewById(R.id.p2);
        mt3 = findViewById(R.id.p3);
        mt4 = findViewById(R.id.p4);
        mt5 = findViewById(R.id.p5);
        mt6 = findViewById(R.id.p6);
        mt7 = findViewById(R.id.p7);
        mt8 = findViewById(R.id.p8);
        mt9 = findViewById(R.id.p9);
        mt10 = findViewById(R.id.p10);
        btSimpan = findViewById(R.id.btSimpan);

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spMapel.getSelectedItem().equals("Pilih") || spKelas.getSelectedItem().equals("Pilih"))
                {
                    showAlert();
                }
                else
                {
                    InsertData();
                }
            }
        });

        showDialog();
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("PETUNJUK!");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Tentukan Mata Pelajaran dan Kelas Serta Berikan 10 Pertanyaan Sesuai Dengan Mata Pelajaran yang Sudah Di Pilih!")
                .setIcon(R.drawable.ic_peringatan)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        tanggal.setText(str1);
//        etTanggal.setEnabled(false);
        //endv
    }


    private void showAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("PERINGATAN!");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Pertanyaan Belum Lengkap, Harap Dilengkapi!")
                .setIcon(R.drawable.ic_warning)
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void InsertData() {
        final String textmapel = spMapel.getSelectedItem().toString().trim();
        final String textkelas = spKelas.getSelectedItem().toString().trim();
        final String texttgl = tanggal.getText().toString().trim();

        final String j1 = mt1.getText().toString().trim();
        final String j2 = mt2.getText().toString().trim();
        final String j3 = mt3.getText().toString().trim();
        final String j4 = mt4.getText().toString().trim();
        final String j5 = mt5.getText().toString().trim();
        final String j6 = mt6.getText().toString().trim();
        final String j7 = mt7.getText().toString().trim();
        final String j8 = mt8.getText().toString().trim();
        final String j9 = mt9.getText().toString().trim();
        final String j10 = mt10.getText().toString().trim();
        final String j11 = "aktiv";

//        final MediaPlayer mp_simpan = MediaPlayer.create(this, R.raw.data_disimpan);
//        final MediaPlayer mp_gagal = MediaPlayer.create(this, R.raw.data_blm_lengkap);
//        final MediaPlayer mp_error = MediaPlayer.create(this, R.raw.koneksi_error);

        final ProgressDialog progressDialog = new ProgressDialog(GuruTambahActivity.this);
        progressDialog.setMessage("Loading . . .");

        if (j1.isEmpty() || j2.isEmpty() || j3.isEmpty() || j4.isEmpty() || j5.isEmpty() ||
                j6.isEmpty() || j7.isEmpty() || j8.isEmpty() || j9.isEmpty() || j10.isEmpty()) {
//            Toast.makeText(GuruTambahActivity.this, "Data Belum Lengkap!", Toast.LENGTH_SHORT).show();
//            mp_gagal.start();
            showAlert();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, InsertAPI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
                                Toast.makeText(GuruTambahActivity.this, "Berhasil!", Toast.LENGTH_SHORT).show();
//                                mp_simpan.start();
                                progressDialog.dismiss();
                                Intent intent = new Intent(GuruTambahActivity.this, GuruTugasActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(GuruTambahActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
//                                mp_error.start();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(GuruTambahActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                            mp_error.start();
                            progressDialog.dismiss();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mapel", textmapel);
                    params.put("kelas", textkelas);
                    params.put("tanggal", texttgl);
                    params.put("soal1", j1);
                    params.put("soal2", j2);
                    params.put("soal3", j3);
                    params.put("soal4", j4);
                    params.put("soal5", j5);
                    params.put("soal6", j6);
                    params.put("soal7", j7);
                    params.put("soal8", j8);
                    params.put("soal9", j9);
                    params.put("soal10", j10);
                    params.put("status", j11);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(GuruTambahActivity.this);
            requestQueue.add(request);
        }
    }
}