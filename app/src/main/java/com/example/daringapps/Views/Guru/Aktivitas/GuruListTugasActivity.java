package com.example.daringapps.Views.Guru.Aktivitas;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.example.daringapps.Views.Guru.DashboardGuruActivity;
import com.rengwuxian.materialedittext.MaterialEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GuruListTugasActivity extends AppCompatActivity {

    MaterialEditText mtTgl;
    ImageButton ibTgl;
    Spinner spKelas, spmapel;
    Button btFilter;
    String myFormat = "dd MMMM yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    Calendar myCalendar;
    ListView list;
    TextView notif;

    AktivitasAdapter aktivitasAdapter;
    public static ArrayList<DataAktivitas> dataAktivitasArrayList = new ArrayList<>();
    private String DataPesananApi = Server.URL_API + "getTugas.php";
    private String JawabanApi = Server.URL_API + "getJawaban.php";
    DataAktivitas dataAktivitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_list_tugas);

        mtTgl = findViewById(R.id.tanggaltugas);
        ibTgl = findViewById(R.id.tglbtn);
        spKelas = findViewById(R.id.listkelas);
        btFilter = findViewById(R.id.btnFilter);
        myCalendar = Calendar.getInstance();
        list = findViewById(R.id.list_jawab);
        notif = findViewById(R.id.txtNotif);
        spmapel = findViewById(R.id.listmapel);

        list = findViewById(R.id.list_jawab);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                startActivity(new Intent(getApplicationContext(), GuruJawabDetail.class)
                        .putExtra("position", position));
            }
        });

        aktivitasAdapter = new AktivitasAdapter(GuruListTugasActivity.this, dataAktivitasArrayList);
        list.setAdapter(aktivitasAdapter);

        ibTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(GuruListTugasActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//                        String formatTanggal = "dd-MM-yyyy";
//                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        mtTgl.setText(sdf.format(myCalendar.getTime()));
//                        Calendar c1 = Calendar.getInstance();
//                        String str1 = sdf.format(c1.getTime());
//                        mtTgl.setText(str1);
                    }
                },
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etTgl = mtTgl.getText().toString();
                String etKelas = spKelas.getSelectedItem().toString();
                String etMapel = spmapel.getSelectedItem().toString();

                if (etTgl.isEmpty() || etKelas.equals("Pilih") || etMapel.equals("Pilih")){
                    Toast.makeText(GuruListTugasActivity.this, "Parameter Belum Lengkap!", Toast.LENGTH_SHORT).show();
                }
                else{
                    receiveData();
                }
            }
        });


//        receiveData();
        dataAktivitasArrayList.clear();

    }

    public void receiveData(){
        final String tgl = mtTgl.getText().toString();
        final String kelas = spKelas.getSelectedItem().toString();
        final String mapel = spmapel.getSelectedItem().toString();
//        final String status = "open";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat Data . . .");
//        loading.setVisibility(View.VISIBLE);
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, JawabanApi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dataAktivitasArrayList.clear();
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
                                    String jawab1 = object.getString("jawab1");
                                    String jawab2 = object.getString("jawab2");
                                    String jawab3 = object.getString("jawab3");
                                    String jawab4 = object.getString("jawab4");
                                    String jawab5 = object.getString("jawab5");
                                    String jawab6 = object.getString("jawab6");
                                    String jawab7 = object.getString("jawab7");
                                    String jawab8 = object.getString("jawab8");
                                    String jawab9 = object.getString("jawab9");
                                    String jawab10 = object.getString("jawab10");
                                    String nilai = object.getString("nilai");
                                    String status = object.getString("status");

                                    if (jsonArray.length() < 1) {
                                        progressDialog.dismiss();
//                                        notif.setVisibility(View.VISIBLE);
                                        Toast.makeText(GuruListTugasActivity.this, "Maaf Sedang Bermasalah!", Toast.LENGTH_SHORT).show();
                                    } else {
//                                        notif.setVisibility(View.GONE);
                                        dataAktivitas = new DataAktivitas(id, id_tugas, mapel, id_siswa, nama, kelas, tanggal, jawab1, jawab2, jawab3, jawab4, jawab5, jawab6, jawab7, jawab8, jawab9, jawab10, nilai, status);
                                        dataAktivitasArrayList.add(dataAktivitas);
                                        aktivitasAdapter.notifyDataSetChanged();
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
                        Toast.makeText(GuruListTugasActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mapel", mapel);
                params.put("kelas", kelas);
                params.put("tanggal", tgl);
//                params.put("status", status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GuruListTugasActivity.this, DashboardGuruActivity.class));
    }

    public void back(View view) {
        startActivity(new Intent(GuruListTugasActivity.this, DashboardGuruActivity.class));
    }
}