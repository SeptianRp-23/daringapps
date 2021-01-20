package com.example.daringapps.Views.Guru.Aktivitas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.example.daringapps.Views.Guru.Absensi.AdapterDetail;
import com.example.daringapps.Views.Guru.Absensi.DataAbsensiDetail;
import com.example.daringapps.Views.Guru.DashboardGuruActivity;
import com.example.daringapps.Views.Murid.DashboardMuridActivity;
import com.example.daringapps.Views.Murid.Tugas.TugasActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuruJawabDetail extends AppCompatActivity {

    int position;
    TextView tvID, jwbID;
    MaterialEditText mtName, mtKelas, mtMapel, mtNilai;
    MaterialEditText soal1, soal2, soal3, soal4, soal5, soal6, soal7, soal8, soal9, soal10;
    MaterialEditText jwb1, jwb2, jwb3, jwb4, jwb5, jwb6, jwb7, jwb8, jwb9, jwb10;
    private String getData = Server.URL_API + "getTugasDtl.php";
    private String Update = Server.URL_API + "update.php";
    Button btSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_jawab_detail);

        mtName = findViewById(R.id.mt_nama);
        mtMapel = findViewById(R.id.mt_mapel);
        mtKelas = findViewById(R.id.mt_kelas);
        tvID = findViewById(R.id.getID);
        jwbID = findViewById(R.id.idjwb);
        btSimpan = findViewById(R.id.btSubmit);
        mtNilai = findViewById(R.id.etNilai);

        soal1 = findViewById(R.id.soal1);
        soal2 = findViewById(R.id.soal2);
        soal3 = findViewById(R.id.soal3);
        soal4 = findViewById(R.id.soal4);
        soal5 = findViewById(R.id.soal5);
        soal6 = findViewById(R.id.soal6);
        soal7 = findViewById(R.id.soal7);
        soal8 = findViewById(R.id.soal8);
        soal9 = findViewById(R.id.soal9);
        soal10 = findViewById(R.id.soal10);

        jwb1 = findViewById(R.id.jawab1);
        jwb2 = findViewById(R.id.jawab2);
        jwb3 = findViewById(R.id.jawab3);
        jwb4 = findViewById(R.id.jawab4);
        jwb5 = findViewById(R.id.jawab5);
        jwb6 = findViewById(R.id.jawab6);
        jwb7 = findViewById(R.id.jawab7);
        jwb8 = findViewById(R.id.jawab8);
        jwb9 = findViewById(R.id.jawab9);
        jwb10 = findViewById(R.id.jawab10);

        //set Data
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        tvID.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getId_tugas());
        mtName.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getNama());
        mtMapel.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getMapel());
        mtKelas.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getKelas());
        jwb1.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab1());
        jwb2.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab2());
        jwb3.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab3());
        jwb4.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab4());
        jwb5.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab5());
        jwb6.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab6());
        jwb7.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab7());
        jwb8.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab8());
        jwb9.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab9());
        jwb10.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getJawab10());
        jwbID.setText(GuruListTugasActivity.dataAktivitasArrayList.get(position).getId());

//        Toast.makeText(this, ""+jwbID, Toast.LENGTH_SHORT).show();

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nilai = mtNilai.getText().toString();
                if (nilai.isEmpty()){
                    Toast.makeText(GuruJawabDetail.this, "Berikan Nilai!", Toast.LENGTH_SHORT).show();
                }
                else{
                    SaveEditDetail();
                }
            }
        });

//        Toast.makeText(this, ""+tvID.getText().toString(), Toast.LENGTH_SHORT).show();

        receiveData();
    }

    public void receiveData(){
        final String id = tvID.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat Data . . .");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, getData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id1 = object.getString("id");
                                    String jawab1 = object.getString("soal1");
                                    String jawab2 = object.getString("soal2");
                                    String jawab3 = object.getString("soal3");
                                    String jawab4 = object.getString("soal4");
                                    String jawab5 = object.getString("soal5");
                                    String jawab6 = object.getString("soal6");
                                    String jawab7 = object.getString("soal7");
                                    String jawab8 = object.getString("soal8");
                                    String jawab9 = object.getString("soal9");
                                    String jawab10 = object.getString("soal10");

                                    soal1.setText(jawab1);
                                    soal2.setText(jawab2);
                                    soal3.setText(jawab3);
                                    soal4.setText(jawab4);
                                    soal5.setText(jawab5);
                                    soal6.setText(jawab6);
                                    soal7.setText(jawab7);
                                    soal8.setText(jawab8);
                                    soal9.setText(jawab9);
                                    soal10.setText(jawab10);

                                    progressDialog.dismiss();

                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GuruJawabDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
//                params.put("status", status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void back(View view) {
        startActivity(new Intent(GuruJawabDetail.this, GuruListTugasActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GuruJawabDetail.this, GuruListTugasActivity.class));
    }

    private void SaveEditDetail() {
        final String id = this.jwbID.getText().toString().trim();
        final String nilai = this.mtNilai.getText().toString().trim();
        final String status = "closed";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(GuruJawabDetail.this, "Success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(GuruJawabDetail.this, GuruListTugasActivity.class));
//                                Logout();
//                                sessionManager.createSession(email, name, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(GuruJawabDetail.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(GuruJawabDetail.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("nilai", nilai);
                params.put("status", status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}