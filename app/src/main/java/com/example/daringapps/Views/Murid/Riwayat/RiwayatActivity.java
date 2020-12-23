package com.example.daringapps.Views.Murid.Riwayat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
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
import com.example.daringapps.Views.Murid.DashboardMuridActivity;
import com.example.daringapps.Views.Murid.Tugas.TugasActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RiwayatActivity extends AppCompatActivity {

    String getNisn;
    ListView listData;
    RiwayatAdapter riwayatAdapter;
    public static ArrayList<DataRiwayat> dataRiwayatArrayList = new ArrayList<>();
    private String DataPesananApi = Server.URL_API + "getRiwayat.php";
    DataRiwayat dataRiwayat;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        //ButtomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.riwayat);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),
                                DashboardMuridActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.tugas:
                        startActivity(new Intent(getApplicationContext(),
                                TugasActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.riwayat:
//                        startActivity(new Intent(getApplicationContext(),
//                                RiwayatActivity.class));
//                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //End ButtomNavv

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getNisn = user.get(SessionManager.NISN);

//        Toast.makeText(this, "Kelas :"+getKelas, Toast.LENGTH_SHORT).show();

        listData = findViewById(R.id.list_tugas);
        riwayatAdapter = new RiwayatAdapter(this, dataRiwayatArrayList);
        listData.setAdapter(riwayatAdapter);

        receiveData();
    }

    public void receiveData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, DataPesananApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataRiwayatArrayList.clear();
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String id_tugas = object.getString("id_tugas");
                                    String mapel = object.getString("mapel");
                                    String id_siswa = object.getString("id_siswa");
                                    String nama = object.getString("nama");
                                    String kelas = object.getString("kelas");
                                    String tanggal = object.getString("tanggal");
                                    String nilai = object.getString("nilai");
                                    String status = object.getString("status");

                                    if (jsonArray.length() == 0) {
                                        progressDialog.dismiss();
                                        Toast.makeText(RiwayatActivity.this, "Maaf Sedang Bermasalah!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dataRiwayat = new DataRiwayat(id, id_tugas, mapel, id_siswa, nama, kelas, tanggal, nilai, status);
                                        dataRiwayatArrayList.add(dataRiwayat);
                                        riwayatAdapter.notifyDataSetChanged();
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
                        progressDialog.dismiss();
                        Toast.makeText(RiwayatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_siswa", getNisn);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}