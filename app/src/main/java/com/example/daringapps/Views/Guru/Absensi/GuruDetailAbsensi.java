package com.example.daringapps.Views.Guru.Absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.example.daringapps.Views.Guru.Tugas.GuruTambahActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuruDetailAbsensi extends AppCompatActivity {


    int position;
    String getDate, getKelas, getWali;
    ListView listData;
    AdapterDetail adapterDetail;
    public static ArrayList<DataAbsensiDetail> dataAbsensiArrayListDtl = new ArrayList<>();
    private String getApi = Server.URL_API + "getAbsenDtl.php";
    DataAbsensiDetail dataAbsensiDetail;
    TextView txtTgl;
    ImageButton btBack, btReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_detail_absensi);

        txtTgl = findViewById(R.id.txtTgl);
        listData = findViewById(R.id.list_absen);
        btBack = findViewById(R.id.btBackdtl);
        btReload = findViewById(R.id.btReload);

        //SetData
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        getDate = (GuruAbsensiActivity.dataAbsensiArrayList.get(position).getTanggal());
        getKelas = (GuruAbsensiActivity.dataAbsensiArrayList.get(position).getKelas());
        getWali = (GuruAbsensiActivity.dataAbsensiArrayList.get(position).getNama());

        txtTgl.setText(getDate);

        adapterDetail = new AdapterDetail(this, dataAbsensiArrayListDtl);
        listData.setAdapter(adapterDetail);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuruDetailAbsensi.this, GuruAbsensiActivity.class));
            }
        });

        btReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveData();
            }
        });

        receiveData();
    }

    public void receiveData(){
        String Tanggal  = txtTgl.getText().toString();
        String Kelas  = getKelas;

        final ProgressDialog progressDialog = new ProgressDialog(GuruDetailAbsensi.this);
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, getApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataAbsensiArrayListDtl.clear();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String nisn = object.getString("nisn");
                                    String nama = object.getString("nama");
                                    String kelas = object.getString("kelas");
                                    String tanggal = object.getString("tanggal");
                                    String jam = object.getString("jam");
                                    String ket = object.getString("ket");

                                    if (jsonArray.length() == 0) {
                                        Toast.makeText(GuruDetailAbsensi.this, "Tidak Ada Data!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        dataAbsensiDetail = new DataAbsensiDetail(id, nisn, nama, kelas, tanggal, jam, ket);
                                        dataAbsensiArrayListDtl.add(dataAbsensiDetail);
                                        adapterDetail.notifyDataSetChanged();
                                        progressDialog.dismiss();
                                    }
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
                        Toast.makeText(GuruDetailAbsensi.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tanggal", Tanggal);
                params.put("kelas", Kelas);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GuruDetailAbsensi.this, GuruAbsensiActivity.class));
    }
}