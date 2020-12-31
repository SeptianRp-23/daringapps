package com.example.daringapps.Views.Murid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.daringapps.Views.Guru.DashboardGuruActivity;
import com.example.daringapps.Views.LoginActivity;
import com.example.daringapps.Views.Murid.Riwayat.RiwayatActivity;
import com.example.daringapps.Views.Murid.Tugas.TugasActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DashboardMuridActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    String getNisn, getNama, getKelas, getUser;
    ImageButton Logout, btUbah, btSimpan;
    private long backPressedTime;
    private Toast backToast;
    TextView date,time, status, txtUser, ket;
    MaterialEditText mtNisn, mtUname, mtNama, mtKelas, mtTelp;
    String myFormat = "dd MMMM yyyy";
    String myDate = "H:m";
    SimpleDateFormat sdfDate = new SimpleDateFormat(myDate);
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    private String InsertAbsen = Server.URL_API + "absenMurid.php";
    private String UpdateMasuk = Server.URL_API + "update_masuk.php";
    private String UpdatePulang = Server.URL_API + "update_pulang.php";
    private String CheckStatus = Server.URL_API + "getStatus.php";
    private String GetUserAPI = Server.URL_API + "dataUser.php";
    private String EditAPI = Server.URL_API + "editUser.php";
    AlertDialog.Builder builder;
    LinearLayout btMasuk, btPulang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        getNisn = user.get(SessionManager.NISN);
        getNama = user.get(SessionManager.NAMA);
        getKelas = user.get(SessionManager.KELAS);
        getUser = user.get(SessionManager.USERNAME);

        txtUser = findViewById(R.id.txtUsername);
        txtUser.setText(getNama);

        status = findViewById(R.id.status);

        date = findViewById(R.id.datetime);

        mtNama = findViewById(R.id.mtNama);
        mtNisn = findViewById(R.id.mtNisn);
        mtKelas = findViewById(R.id.mtKelas);
        mtTelp = findViewById(R.id.mtTelp);
        mtUname = findViewById(R.id.mtUname);
        btSimpan = findViewById(R.id.btsimpan);
        btUbah = findViewById(R.id.bt_ubah);
        time = findViewById(R.id.time);
        ket = findViewById(R.id.txtKet);
//        btAbsen = findViewById(R.id.absent);

        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        String str2 = sdfDate.format(c1.getTime());
        date.setText(str1);
        time.setText(str2);
//        etTanggal.setEnabled(false);
        //end

        btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btUbah.setVisibility(View.GONE);
                btSimpan.setVisibility(View.VISIBLE);
                mtTelp.setEnabled(true);
                mtUname.setEnabled(true);
                mtNama.setEnabled(true);
            }
        });

        builder = new AlertDialog.Builder(this);
        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage("Peringatan !");
                //Setting message manually and performing action on button click
                builder.setMessage("Tekan Yes Untuk Merubah Data dan Kembali Ke Halaman Login ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                btUbah.setVisibility(View.VISIBLE);
                                btSimpan.setVisibility(View.GONE);
                                SaveEditDetail();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                                getUserDetail();
                                mtTelp.setEnabled(false);
                                mtUname.setEnabled(false);
                                mtNama.setEnabled(false);
                                btUbah.setVisibility(View.VISIBLE);
                                btSimpan.setVisibility(View.GONE);
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Peringatan !");
                alert.show();
            }
        });

        btMasuk = findViewById(R.id.absenMasuk);
        btMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
                StatusMulai();
            }
        });

        btPulang = findViewById(R.id.absenPulang);
        btPulang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
                StatusSelesai();
            }
        });

        Logout = findViewById(R.id.logout);
        builder = new AlertDialog.Builder(this);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage("Peringatan !");
                //Setting message manually and performing action on button click
                builder.setMessage("Apakah yakin ingin keluar ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Logout();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Peringatan !");
                alert.show();
            }
        });

        getUserDetail();


        //ButtomNav
        BottomNavigationView bottomNavigationView = findViewById(R.id.buttom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
//                        startActivity(new Intent(getApplicationContext(),
//                                DashboardMuridActivity.class));
//                        overridePendingTransition(0,0);
                        return true;

                    case R.id.tugas:
                        final String txt = ket.getText().toString();
                        if (txt.equals("Mulai")){
                            Toast.makeText(DashboardMuridActivity.this, "Kami Belum Absen!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),
                                    DashboardMuridActivity.class));
                            overridePendingTransition(0,0);
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(),
                                    TugasActivity.class));
                            overridePendingTransition(0,0);
                        }
                        return true;

                    case R.id.riwayat:
                        final String txt2 = ket.getText().toString();
                        if (txt2.equals("Mulai")){
                            Toast.makeText(DashboardMuridActivity.this, "Kami Belum Absen!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),
                                    DashboardMuridActivity.class));
                            overridePendingTransition(0,0);
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(),
                                    RiwayatActivity.class));
                            overridePendingTransition(0,0);
                        }
                        return true;
                }
                return false;
            }
        });
        //End ButtomNav
    }

    private void InsertData() {
        final String textnisn = getNisn;
        final String textnama = getNama;
        final String textkelas = getKelas;
        final String texttgl = date.getText().toString();
        final String textTime = time.getText().toString();
        final String txtKet = ket.getText().toString();

//        final MediaPlayer mp_simpan = MediaPlayer.create(this, R.raw.data_disimpan);
//        final MediaPlayer mp_gagal = MediaPlayer.create(this, R.raw.data_blm_lengkap);
//        final MediaPlayer mp_error = MediaPlayer.create(this, R.raw.koneksi_error);

        final ProgressDialog progressDialog = new ProgressDialog(DashboardMuridActivity.this);
        progressDialog.setMessage("Loading . . .");

        if (texttgl.isEmpty() ) {
            Toast.makeText(DashboardMuridActivity.this, "Belum Bisa Absen!", Toast.LENGTH_SHORT).show();
//            mp_gagal.start();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, InsertAbsen,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
                                Toast.makeText(DashboardMuridActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                mp_simpan.start();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(DashboardMuridActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
//                                mp_error.start();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DashboardMuridActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                            mp_error.start();
                            progressDialog.dismiss();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nisn", textnisn);
                    params.put("nama", textnama);
                    params.put("kelas", textkelas);
                    params.put("tanggal", texttgl);
                    params.put("jam", textTime);
                    params.put("ket", txtKet);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardMuridActivity.this);
            requestQueue.add(request);
        }
    }

    private void StatusMulai() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdateMasuk,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(DashboardMuridActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                getUserDetail();
                                btMasuk.setVisibility(View.GONE);
                                btPulang.setVisibility(View.VISIBLE);
//                                InsertRekap();
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(DashboardMuridActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(DashboardMuridActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
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
        requestQueue.add(stringRequest);

    }

    private void StatusSelesai() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UpdatePulang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(DashboardMuridActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                getUserDetail();
                                btPulang.setVisibility(View.GONE);
                                btMasuk.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(DashboardMuridActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(DashboardMuridActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
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
        requestQueue.add(stringRequest);

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

    private void Logout(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
        editor.apply();
        Intent i = new Intent(this, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void getUserDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GetUserAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")){

                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strNisn = object.getString("nisn").trim();
                                    String strUname = object.getString("username").trim();
                                    String strName = object.getString("nama").trim();
                                    String strkelas = object.getString("kelas").trim();
                                    String strTelp = object.getString("telp").trim();
                                    String strStatus = object.getString("status").trim();

                                    mtNisn.setText(strNisn);
                                    mtUname.setText(strUname);
                                    mtNama.setText(strName);
                                    mtKelas.setText(strkelas);
                                    mtTelp.setText(strTelp);
                                    status.setText(strStatus);
                                    if (strStatus.equals("Y")){
                                        btMasuk.setVisibility(View.GONE);
                                        btPulang.setVisibility(View.VISIBLE);
                                        ket.setText("Selesai");
                                    }
                                    else if(strStatus.equals("N")){
                                        btPulang.setVisibility(View.GONE);
                                        btMasuk.setVisibility(View.VISIBLE);
                                        ket.setText("Mulai");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DashboardMuridActivity.this, "Bermasalah! "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardMuridActivity.this, "Koneksi Error! "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", getUser);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    private void SaveEditDetail() {
        final String username = this.mtUname.getText().toString().trim();
        final String nama = this.mtNama.getText().toString().trim();
        final String kelas = this.mtKelas.getText().toString().trim();
        final String telp = this.mtTelp.getText().toString().trim();
        final String nisn = this.mtNisn.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EditAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(DashboardMuridActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                Logout();
//                                sessionManager.createSession(email, name, id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DashboardMuridActivity.this, "Error"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardMuridActivity.this, "Error"+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("nama", nama);
                params.put("kelas", kelas);
                params.put("telp", telp);
                params.put("nisn", nisn);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}