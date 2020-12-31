package com.example.daringapps.Views.Murid.Tugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.daringapps.Controllers.DataTugas;
import com.example.daringapps.Controllers.TugasAdapter;
import com.example.daringapps.Helper.Server;
import com.example.daringapps.Helper.SessionManager;
import com.example.daringapps.R;
import com.example.daringapps.Views.Murid.DashboardMuridActivity;
import com.example.daringapps.Views.Murid.Riwayat.RiwayatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TugasActivity extends AppCompatActivity {

    String getKelas;
    ListView listData;
    TugasAdapter tugasAdapter;
    public static ArrayList<DataTugas> dataTugasArrayList = new ArrayList<>();
    private String DataPesananApi = Server.URL_API + "getTugas.php";
    DataTugas dataTugas;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    ImageView btBack;
    private long backPressedTime;
    private Toast backToast;
    Handler mHandler;
    String myFormat = "dd MMMM yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    String tgl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugas);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getKelas = user.get(SessionManager.KELAS);

//        Toast.makeText(this, "Kelas :"+getKelas, Toast.LENGTH_SHORT).show();

        listData = findViewById(R.id.list_tugas);
        tugasAdapter = new TugasAdapter(this, dataTugasArrayList);
        listData.setAdapter(tugasAdapter);


        receiveData();

        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                ProgressDialog progressDialog = new ProgressDialog(view.getContext());
//                SaveEditDetail();
                startActivity(new Intent(getApplicationContext(), TugasDetailActivity.class)
                        .putExtra("position", position));

////                final String status = this.tvSts.getText().toString().trim();
//                final ProgressDialog progressDialog = new ProgressDialog(AdmPesananTrackActivity.this);
//                progressDialog.setMessage("Please Wait...");
//                progressDialog.show();
            }
        });

        this.mHandler = new Handler();
        this.mHandler.postDelayed(m_Runnable,20000);

        //ButtomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.tugas);
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
//                        startActivity(new Intent(getApplicationContext(),
//                                TugasActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                    case R.id.riwayat:
                        startActivity(new Intent(getApplicationContext(),
                                RiwayatActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        //End ButtomNavv

        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                ProgressDialog progressDialog = new ProgressDialog(view.getContext());
//                SaveEditDetail();
                startActivity(new Intent(getApplicationContext(), TugasDetailActivity.class)
                        .putExtra("position", position));

////                final String status = this.tvSts.getText().toString().trim();
//                final ProgressDialog progressDialog = new ProgressDialog(AdmPesananTrackActivity.this);
//                progressDialog.setMessage("Please Wait...");
//                progressDialog.show();
            }
        });
        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        tgl = sdf.format(c1.getTime());
//        Toast.makeText(this, ""+tgl, Toast.LENGTH_SHORT).show();
    }

    private final Runnable m_Runnable = new Runnable(){
        public void run() {
            TugasActivity.this.mHandler.postDelayed(m_Runnable, 20000);
            receiveData();
//            progressDialog = new ProgressDialog(KurirDashboardActivity.this);
//            progressDialog.setMessage("Please Wait..."); // Setting Message
//            progressDialog.setTitle("Sync Activity"); // Setting Title
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//             //1800000
//            progressDialog.show(); // Display Progress Dialog
//            progressDialog.setCancelable(false);
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        Thread.sleep(3000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    progressDialog.dismiss();
//                }
//            }).start();
//            Toast.makeText(KurirDashboardActivity.this,"in runnable",Toast.LENGTH_SHORT).show();
        }

    };//runnable

    public void receiveData(){
        StringRequest request = new StringRequest(Request.Method.POST, DataPesananApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataTugasArrayList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String mapel = object.getString("mapel");
                                    String kelas = object.getString("kelas");
                                    String tanggal = object.getString("tanggal");
                                    String soal1 = object.getString("soal1");
                                    String soal2 = object.getString("soal2");
                                    String soal3 = object.getString("soal3");
                                    String soal4 = object.getString("soal4");
                                    String soal5 = object.getString("soal5");
                                    String soal6 = object.getString("soal6");
                                    String soal7 = object.getString("soal7");
                                    String soal8 = object.getString("soal8");
                                    String soal9 = object.getString("soal9");
                                    String soal10 = object.getString("soal10");
                                    String status = object.getString("status");

                                    if (jsonArray.length() == 0) {
                                        Toast.makeText(TugasActivity.this, "Maaf Sedang Bermasalah!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dataTugas = new DataTugas(id, mapel, kelas, tanggal, soal1, soal2, soal3, soal4, soal5, soal6, soal7, soal8, soal9, soal10, status);
                                        dataTugasArrayList.add(dataTugas);
                                        tugasAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TugasActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kelas", getKelas);
                params.put("tanggal", tgl);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        } else {
            backToast = Toast.makeText(this, "Tekan Lagi Untuk Keluar", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}