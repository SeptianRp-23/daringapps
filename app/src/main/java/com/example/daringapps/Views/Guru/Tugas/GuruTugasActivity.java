package com.example.daringapps.Views.Guru.Tugas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.example.daringapps.Views.Murid.Tugas.TugasActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GuruTugasActivity extends AppCompatActivity {

    String getKelas;
    ListView listData;
    GuruTugasAdapter guruTugasAdapter;
    public static ArrayList<ItemTugas> itemTugasArrayList = new ArrayList<>();
    private String DataPesananApi = Server.URL_API + "getTugas.php";
    ItemTugas itemTugas;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    CardView tambah;
    private long backPressedTime;
    private Toast backToast;
    Handler mHandler;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_tugas);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getKelas = user.get(SessionManager.KELAS);

//        Toast.makeText(this, "Kelas :"+getKelas, Toast.LENGTH_SHORT).show();

        listData = findViewById(R.id.listGuru);
        guruTugasAdapter = new GuruTugasAdapter(this, itemTugasArrayList);
        listData.setAdapter(guruTugasAdapter);

        tambah = findViewById(R.id.add);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuruTugasActivity.this, GuruTambahActivity.class));
            }
        });

        receiveData();
    }

    public void receiveData(){
        StringRequest request = new StringRequest(Request.Method.POST, DataPesananApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        itemTugasArrayList.clear();
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
                                        Toast.makeText(GuruTugasActivity.this, "Maaf Sedang Bermasalah!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        itemTugas = new ItemTugas(id, mapel, kelas, tanggal, soal1, soal2, soal3, soal4, soal5, soal6, soal7, soal8, soal9, soal10, status);
                                        itemTugasArrayList.add(itemTugas);
                                        guruTugasAdapter.notifyDataSetChanged();
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
                        Toast.makeText(GuruTugasActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kelas", getKelas);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}