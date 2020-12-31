package com.example.daringapps.Views.Guru.Absensi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
import com.example.daringapps.Helper.Server;
import com.example.daringapps.Helper.SessionManager;
import com.example.daringapps.R;
import com.example.daringapps.Views.Guru.DashboardGuruActivity;
import com.example.daringapps.Views.Guru.Tugas.GuruTugasActivity;
import com.example.daringapps.Views.Guru.Tugas.GuruTugasAdapter;
import com.example.daringapps.Views.Guru.Tugas.ItemTugas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuruAbsensiActivity extends AppCompatActivity {

    String getKelas, getNisn;
    ListView listData;
    AbsensiAdapter absensiAdapter;
    public static ArrayList<DataAbsensi> dataAbsensiArrayList = new ArrayList<>();
    private String rekapApi = Server.URL_API + "getRekap.php";
    DataAbsensi dataAbsensi;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    ImageButton btBack;
    private long backPressedTime;
    private Toast backToast;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_absensi);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getNisn = user.get(SessionManager.NISN);

        btBack = findViewById(R.id.btBack);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuruAbsensiActivity.this, DashboardGuruActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

//        Toast.makeText(this, "Kelas :"+getKelas, Toast.LENGTH_SHORT).show();

        listData = findViewById(R.id.listAbsen);
        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                startActivity(new Intent(getApplicationContext(), GuruDetailAbsensi.class)
                        .putExtra("position", position));
            }
        });
        absensiAdapter = new AbsensiAdapter(this, dataAbsensiArrayList);
        listData.setAdapter(absensiAdapter);

        receiveData();
    }
    public void receiveData(){
        final ProgressDialog progressDialog = new ProgressDialog(GuruAbsensiActivity.this);
        progressDialog.setMessage("Loading . . .");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, rekapApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataAbsensiArrayList.clear();
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

                                    if (jsonArray.length() == 0) {
                                        Toast.makeText(GuruAbsensiActivity.this, "Maaf Sedang Bermasalah!", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    } else {
                                        dataAbsensi = new DataAbsensi(id, nisn, nama, kelas, tanggal);
                                        dataAbsensiArrayList.add(dataAbsensi);
                                        absensiAdapter.notifyDataSetChanged();
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
                        Toast.makeText(GuruAbsensiActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nisn", getNisn);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GuruAbsensiActivity.this, DashboardGuruActivity.class));
    }
}