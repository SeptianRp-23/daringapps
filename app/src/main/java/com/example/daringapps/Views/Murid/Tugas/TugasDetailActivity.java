package com.example.daringapps.Views.Murid.Tugas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.daringapps.Helper.SessionManager;
import com.example.daringapps.R;
import com.example.daringapps.Views.LoginActivity;
import com.example.daringapps.Views.RegisterActivity;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TugasDetailActivity extends AppCompatActivity {

    EditText et1, et2, et3, et4, et5, et6, et7, et8, et9, et10;
    MaterialEditText mt1, mt2, mt3, mt4, mt5, mt6, mt7, mt8, mt9, mt10;
    TextView tv_id, tv_mapel, tv_siswa, tv_nama, tv_tanggal, tv_kelas;
    String getNama, getNisn, getKelas;
    int position;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String myFormat = "dd MMMM yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    private String jawabAPI = Server.URL_API + "insertJawaban.php";
    Button bt_simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas_detail);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getNisn = user.get(SessionManager.NISN);
        getNama = user.get(SessionManager.NAMA);
        getKelas = user.get(SessionManager.KELAS);

        tv_nama = findViewById(R.id.tvNama);
        tv_kelas = findViewById(R.id.tvKelas);
        tv_tanggal = findViewById(R.id.tvTgl);
        tv_id = findViewById(R.id.tvID);
        tv_mapel = findViewById(R.id.tvMapel);
        tv_siswa = findViewById(R.id.id_siswa);
//
        et1 = findViewById(R.id.etSoal1);
        et2 = findViewById(R.id.etSoal2);
        et3 = findViewById(R.id.etSoal3);
        et4 = findViewById(R.id.etSoal4);
        et5 = findViewById(R.id.etSoal5);
        et6 = findViewById(R.id.etSoal6);
        et7 = findViewById(R.id.etSoal7);
        et8 = findViewById(R.id.etSoal8);
        et9 = findViewById(R.id.etSoal9);
        et10 = findViewById(R.id.etSoal10);
//
        mt1 = findViewById(R.id.mtJawab1);
        mt2 = findViewById(R.id.mtJawab2);
        mt3 = findViewById(R.id.mtJawab3);
        mt4 = findViewById(R.id.mtJawab4);
        mt5 = findViewById(R.id.mtJawab5);
        mt6 = findViewById(R.id.mtJawab6);
        mt7 = findViewById(R.id.mtJawab7);
        mt8 = findViewById(R.id.mtJawab8);
        mt9 = findViewById(R.id.mtJawab9);
        mt10 = findViewById(R.id.mtJawab10);
//
//
        tv_siswa.setText(getNisn);
        tv_nama.setText(getNama);
        tv_kelas.setText(getKelas);

        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        tv_tanggal.setText(str1);
//        etTanggal.setEnabled(false);
        //end
//
        //set Data
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        et1.setText(TugasActivity.dataTugasArrayList.get(position).getSoal1());
        et2.setText(TugasActivity.dataTugasArrayList.get(position).getSoal2());
        et3.setText(TugasActivity.dataTugasArrayList.get(position).getSoal3());
        et4.setText(TugasActivity.dataTugasArrayList.get(position).getSoal4());
        et5.setText(TugasActivity.dataTugasArrayList.get(position).getSoal5());
        et6.setText(TugasActivity.dataTugasArrayList.get(position).getSoal6());
        et7.setText(TugasActivity.dataTugasArrayList.get(position).getSoal7());
        et8.setText(TugasActivity.dataTugasArrayList.get(position).getSoal8());
        et9.setText(TugasActivity.dataTugasArrayList.get(position).getSoal9());
        et10.setText(TugasActivity.dataTugasArrayList.get(position).getSoal10());
        tv_id.setText(TugasActivity.dataTugasArrayList.get(position).getId());
        tv_mapel.setText(TugasActivity.dataTugasArrayList.get(position).getMapel());
//
        bt_simpan = findViewById(R.id.btKirim);
        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
            }
        });

    }

    private void InsertData() {
        final String textid = tv_id.getText().toString().trim();
        final String textmapel = tv_mapel.getText().toString().trim();
        final String textsiswa = tv_siswa.getText().toString().trim();
        final String textnama = tv_nama.getText().toString().trim();
        final String textkelas = tv_kelas.getText().toString().trim();
        final String texttgl = tv_tanggal.getText().toString().trim();

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

//        final MediaPlayer mp_simpan = MediaPlayer.create(this, R.raw.data_disimpan);
//        final MediaPlayer mp_gagal = MediaPlayer.create(this, R.raw.data_blm_lengkap);
//        final MediaPlayer mp_error = MediaPlayer.create(this, R.raw.koneksi_error);

        final ProgressDialog progressDialog = new ProgressDialog(TugasDetailActivity.this);
        progressDialog.setMessage("Loading . . .");

        if (textid.isEmpty() || textmapel.isEmpty() || textsiswa.isEmpty() || textnama.isEmpty() || texttgl.isEmpty() ) {
            Toast.makeText(TugasDetailActivity.this, "Data Belum Lengkap!", Toast.LENGTH_SHORT).show();
//            mp_gagal.start();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, jawabAPI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
                                Toast.makeText(TugasDetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                mp_simpan.start();
                                progressDialog.dismiss();
                                Intent intent = new Intent(TugasDetailActivity.this, TugasActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(TugasDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
//                                mp_error.start();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(TugasDetailActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                            mp_error.start();
                            progressDialog.dismiss();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_tugas", textid);
                    params.put("mapel", textmapel);
                    params.put("id_siswa", textsiswa);
                    params.put("nama", textnama);
                    params.put("kelas", textkelas);
                    params.put("tanggal", texttgl);
                    params.put("jawab1", j1);
                    params.put("jawab2", j2);
                    params.put("jawab3", j3);
                    params.put("jawab4", j4);
                    params.put("jawab5", j5);
                    params.put("jawab6", j6);
                    params.put("jawab7", j7);
                    params.put("jawab8", j8);
                    params.put("jawab9", j9);
                    params.put("jawab10", j10);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(TugasDetailActivity.this);
            requestQueue.add(request);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TugasDetailActivity.this, TugasActivity.class));
    }
}