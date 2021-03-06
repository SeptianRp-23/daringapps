package com.example.daringapps.Views.Guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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
import com.example.daringapps.Helper.SessionManager;
import com.example.daringapps.R;
import com.example.daringapps.Views.Guru.Absensi.GuruAbsensiActivity;
import com.example.daringapps.Views.Guru.Aktivitas.GuruListTugasActivity;
import com.example.daringapps.Views.Guru.Laporan.GuruLaporanActivity;
import com.example.daringapps.Views.Guru.Tugas.GuruTugasActivity;
import com.example.daringapps.Views.LoginActivity;
import com.example.daringapps.Views.RegisterActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.daringapps.Helper.Server.URL_PDF;

public class DashboardGuruActivity extends AppCompatActivity {

    RelativeLayout tugas, absen, aktivitas, laporan;
    SharedPreferences sharedPreferences, sharedPreferences2;
    SessionManager sessionManager;
    String getKelas, getNama, getNisn, getUNama;
    AlertDialog.Builder builder;
    private long backPressedTime;
    private Toast backToast;
    ImageView btSub;
    TextView date,uname, status;
    String myFormat = "dd MMMM yyyy";
    Button btUpdate, btClose, dtSimpan, dtClose;
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
    private String InsertAbsen = Server.URL_API + "absenguru.php";
    private String UpdateMasuk = Server.URL_API + "update_masuk.php";
    private String UpdatePulang = Server.URL_API + "update_pulang.php";
    private String CheckStatus = Server.URL_API + "getStatus.php";
    private String getUserData = Server.URL_API + "dataUser.php";
    private String InsertRekap = Server.URL_API + "insertRekap.php";
    private String UserAPI = Server.URL_API + "registerUser.php";
    private String updateUser = Server.URL_API + "update_user.php";

    Dialog dialog, dialog2, dialog3;
    MaterialEditText duID, duUname, duNama, duKelas, dtName, dtUname, dtTelp;
    Spinner dtKelas, dtLevel;

    private String createdData1 = URL_PDF + "absenAll.php";
    private String createdData2 = URL_PDF + "absenSiswa.php";
    private String createdData3 = URL_PDF + "dataPengguna.php";
    private String createdData4 = URL_PDF + "dataSiswa.php";
    private String createdData5 = URL_PDF + "dataTugas.php";
    private String createdData6 = URL_PDF + "tugasHarian.php";
    LinearLayout btMasuk, btPulang;
    int stat;

//    private static final String[] STORAGE_PERMISSION = (Manifest.permission)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_guru);

        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getKelas = user.get(SessionManager.KELAS);
        getUNama = user.get(SessionManager.USERNAME);
        getNisn = user.get(SessionManager.NISN);
        getNama = user.get(SessionManager.NAMA);

        uname = findViewById(R.id.txtUsername);
        uname.setText(getNama);

        status = findViewById(R.id.status);
        btSub = findViewById(R.id.subMenu);
        builder = new AlertDialog.Builder(this);

        date = findViewById(R.id.datetime);
        //Set Tanggal
        Calendar c1 = Calendar.getInstance();
        String str1 = sdf.format(c1.getTime());
        date.setText(str1);
//        etTanggal.setEnabled(false);
        //endv

//        check();


        dialog = new Dialog(DashboardGuruActivity.this);
        dialog.setContentView(R.layout.dialog_update);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        duID = dialog.findViewById(R.id.gu_userid);
        duUname = dialog.findViewById(R.id.gu_uname);
        duNama = dialog.findViewById(R.id.gu_nama);
        duKelas = dialog.findViewById(R.id.gu_kelas);
        btUpdate = dialog.findViewById(R.id.btSimpan);
        btClose = dialog.findViewById(R.id.btClose);

        final MediaPlayer mptugas = MediaPlayer.create(this, R.raw.tugas);
        final MediaPlayer mpaktivitas = MediaPlayer.create(this, R.raw.aktivitas);
        final MediaPlayer mpabsen = MediaPlayer.create(this, R.raw.absensi);
        final MediaPlayer mplaporan = MediaPlayer.create(this, R.raw.laporan);

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update
                final String strUname = duUname.getText().toString();
                final String strName = duUname.getText().toString();
                final String strID = duID.getText().toString();

                if (strID.isEmpty() || strName.isEmpty() || strUname.isEmpty()){
                    Toast.makeText(DashboardGuruActivity.this, "Field Kosong", Toast.LENGTH_SHORT).show();
                }
                else{
                    UpdateUser();
                }
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //closed
                dialog.dismiss();
            }
        });

        dialog2 = new Dialog(DashboardGuruActivity.this);
        dialog2.setContentView(R.layout.dialog_tambah);
        dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.bg));
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.setCancelable(false);

        dtName = dialog2.findViewById(R.id.dt_uname);
        dtUname = dialog2.findViewById(R.id.dt_uname);
        dtTelp = dialog2.findViewById(R.id.dt_telp);
        dtKelas = dialog2.findViewById(R.id.dt_spkelas);
        dtLevel = dialog2.findViewById(R.id.dt_spLevel);
        dtSimpan = dialog2.findViewById(R.id.dt_simpan);
        dtClose = dialog2.findViewById(R.id.dt_close);

        dtSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saved
                final String reg_uname = dtUname.getText().toString();
                final String reg_name = dtName.getText().toString();
                final String reg_kelas = dtKelas.getSelectedItem().toString();
                final String reg_telp = dtTelp.getText().toString();
                final String reg_level = dtLevel.getSelectedItem().toString();

                if (reg_uname.isEmpty() || reg_name.isEmpty() || reg_kelas.equals("Pilih") || reg_telp.isEmpty() || reg_level.equals("Pilih")){
                    Toast.makeText(DashboardGuruActivity.this, "Belum Lengkap", Toast.LENGTH_SHORT).show();
                }
                else{
                    InsertUser();
                }

            }
        });

        dtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

        btSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(DashboardGuruActivity.this, v);
                popup.setOnMenuItemClickListener(DashboardGuruActivity.this::OnMenuItemClickListener);
                popup.inflate(R.menu.option_adm);
                popup.show();
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
                Logout();
            }
        });

        tugas = findViewById(R.id.tugas_guru);
        tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mptugas.start();
                startActivity(new Intent(DashboardGuruActivity.this, GuruTugasActivity.class));
                overridePendingTransition(0,0);
            }
        });

        absen = findViewById(R.id.absensi_siswa);
        absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpabsen.start();
                startActivity(new Intent(DashboardGuruActivity.this, GuruAbsensiActivity.class));
                overridePendingTransition(0,0);
            }
        });

        aktivitas = findViewById(R.id.aktivitas_siswa);
        aktivitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpaktivitas.start();
                startActivity(new Intent(DashboardGuruActivity.this, GuruListTugasActivity.class));
                overridePendingTransition(0,0);
            }
        });

        laporan = findViewById(R.id.laporan_siswa);
        laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mplaporan.start();
                AbsenAll();
                final ProgressDialog progressDialog = new ProgressDialog(DashboardGuruActivity.this);
                progressDialog.setMessage("Tunggu Sebentar . . .");
                progressDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        startActivity(new Intent(DashboardGuruActivity.this, GuruLaporanActivity.class));
                    }
                }, 5000);
            }
        });

//        btLogout = findViewById(R.id.logout);

        getUserDetail();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.option_adm, menu);
//        return true;
//    }
////
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.setting:
//                Toast.makeText(getApplicationContext(),"Buka Profil",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.tambah:
//                Toast.makeText(getApplicationContext(),"Tambah Teman",Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.logout:
//                Toast.makeText(getApplicationContext(),"Group",Toast.LENGTH_SHORT).show();
//                Logout();
//                break;
//        }
//        return true;
//    }

    public boolean OnMenuItemClickListener(MenuItem item){
        switch (item.getItemId()){
            case R.id.setting:
                dialog.show();
                getUserProfile();
                Toast.makeText(getApplicationContext(),"Buka Profil",Toast.LENGTH_SHORT).show();
                break;

            case R.id.tambah:
                dialog2.show();
                Toast.makeText(getApplicationContext(),"Tambah Teman",Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                Toast.makeText(getApplicationContext(),"Group",Toast.LENGTH_SHORT).show();
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
                break;
        }
        return true;
    }

//    onMenuItemClick()

    //    private void check(){
//        String cek = status.getText().toString();
//        int i = Integer.parseInt(cek);
//        if (i == 0){
//            btMasuk.setVisibility(View.VISIBLE);
//        }
//        else{
//            btPulang.setVisibility(View.VISIBLE);
//        }
//    }

    private void AbsenAll(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AbsenSiswa();
                        DataPengguna();
                        DataSiswa();
                        DataTugas();
                        TugasHarian();
                        Toast.makeText(DashboardGuruActivity.this, "PDF Ready...", Toast.LENGTH_SHORT).show();
//                                progressDialog.dismiss();
//                                startActivity(new Intent(AdmDashboardActivity.this, AdmLaporanActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
                        Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
        requestQueue.add(request);
    }


    private void AbsenSiswa(){
        String tgl = date.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, createdData2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 2", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =new HashMap<>();
                params.put("kelas", getKelas);
                params.put("tanggal", tgl);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
        requestQueue.add(request);
    }

    private void DataPengguna(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 3", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
        requestQueue.add(request);
    }

    private void DataSiswa(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 4", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
        requestQueue.add(request);
    }

    private void DataTugas(){
        StringRequest request = new StringRequest(Request.Method.POST, createdData5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 5", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
        requestQueue.add(request);
    }

    private void TugasHarian(){
        String tgl = date.getText().toString();
        StringRequest request = new StringRequest(Request.Method.POST, createdData6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(AdmDashboardActivity.this, "Laporan 6", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params =new HashMap<>();
                params.put("kelas", getKelas);
                params.put("tanggal", tgl);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
        requestQueue.add(request);
    }

    private void InsertData() {
        final String textnisn = getNisn;
        final String textnama = getNama;
        final String textkelas = getKelas;
        final String texttgl = date.getText().toString();

//        final MediaPlayer mp_simpan = MediaPlayer.create(this, R.raw.data_disimpan);
//        final MediaPlayer mp_gagal = MediaPlayer.create(this, R.raw.data_blm_lengkap);
//        final MediaPlayer mp_error = MediaPlayer.create(this, R.raw.koneksi_error);

        final ProgressDialog progressDialog = new ProgressDialog(DashboardGuruActivity.this);
        progressDialog.setMessage("Loading . . .");

        if (texttgl.isEmpty() ) {
            Toast.makeText(DashboardGuruActivity.this, "Belum Bisa Absen!", Toast.LENGTH_SHORT).show();
//            mp_gagal.start();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, InsertAbsen,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
                                Toast.makeText(DashboardGuruActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                                mp_simpan.start();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(DashboardGuruActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
//                                mp_error.start();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
            requestQueue.add(request);
        }
    }

    private void InsertRekap() {
        final String textnisn = getNisn;
        final String textnama = getNama;
        final String textkelas = getKelas;
        final String texttgl = date.getText().toString();

//        final MediaPlayer mp_simpan = MediaPlayer.create(this, R.raw.data_disimpan);
//        final MediaPlayer mp_gagal = MediaPlayer.create(this, R.raw.data_blm_lengkap);
//        final MediaPlayer mp_error = MediaPlayer.create(this, R.raw.koneksi_error);

//        final ProgressDialog progressDialog = new ProgressDialog(DashboardGuruActivity.this);
//        progressDialog.setMessage("Loading . . .");

        if (texttgl.isEmpty() ) {
            Toast.makeText(DashboardGuruActivity.this, "Belum Bisa Absen!", Toast.LENGTH_SHORT).show();
//            mp_gagal.start();
            return;
        } else {
//            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, InsertRekap,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
//                                Toast.makeText(DashboardGuruActivity.this, "Success", Toast.LENGTH_SHORT).show();
////                                mp_simpan.start();
//                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(DashboardGuruActivity.this, "Rekap Error!", Toast.LENGTH_SHORT).show();
////                                mp_error.start();
//                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
////                            mp_error.start();
//                            progressDialog.dismiss();
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
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
            requestQueue.add(request);
        }
    }

    private void getUserDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CheckStatus,
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

                                    String strId = object.getString("nisn").trim();
                                    String strStatus = object.getString("status").trim();

                                    status.setText(strStatus);
                                    if (strStatus.equals("Y")){
                                        btMasuk.setVisibility(View.GONE);
                                        btPulang.setVisibility(View.VISIBLE);
                                    }
                                    else if(strStatus.equals("N")){
                                        btPulang.setVisibility(View.GONE);
                                        btMasuk.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DashboardGuruActivity.this, "Bermasalah! "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardGuruActivity.this, "Koneksi Error! "+error.toString(), Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        getUserDetail();
//        check();
    }

    private void getUserProfile(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getUserData,
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

                                    String strId = object.getString("nisn").trim();
                                    String strUname = object.getString("username").trim();
                                    String strNama = object.getString("nama").trim();
                                    String strKelas = object.getString("kelas").trim();
                                    String strStatus = object.getString("status").trim();

                                    duID.setText(strId);
                                    duUname.setText(strUname);
                                    duNama.setText(strNama);
                                    duKelas.setText(strKelas);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(DashboardGuruActivity.this, "Bermasalah! "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DashboardGuruActivity.this, "Koneksi Error! "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", getUNama);
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
                                Toast.makeText(DashboardGuruActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                getUserDetail();
                                btMasuk.setVisibility(View.GONE);
                                btPulang.setVisibility(View.VISIBLE);
                                InsertRekap();
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(DashboardGuruActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(DashboardGuruActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(DashboardGuruActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                getUserDetail();
                                btPulang.setVisibility(View.GONE);
                                btMasuk.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(DashboardGuruActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(DashboardGuruActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
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

//    private void InsertUser() {
//        final String txtNama = dtName.getText().toString();
//        final String txtUnama = dtUname.getText().toString();
//        final String txtKelas = dtKelas.getSelectedItem().toString();
//        final String txtTelp = dtTelp.getText().toString();
//        final String txtLevel = dtLevel.getSelectedItem().toString();
//        final ProgressDialog progressDialog = new ProgressDialog(DashboardGuruActivity.this);
//        progressDialog.setMessage("Loading . . .");
//            progressDialog.show();
//            StringRequest request = new StringRequest(Request.Method.POST, InsertAbsen,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            if (response.equalsIgnoreCase("success")) {
//                                Toast.makeText(DashboardGuruActivity.this, "Success", Toast.LENGTH_SHORT).show();
////                                mp_simpan.start();
//                                progressDialog.dismiss();
//                                dialog2.dismiss();
//                            } else {
//                                Toast.makeText(DashboardGuruActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
////                                mp_error.start();
//                                progressDialog.dismiss();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
////                            mp_error.start();
//                            progressDialog.dismiss();
//                        }
//                    }
//            ){
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("username", txtUnama);
//                    params.put("nama", txtNama);
//                    params.put("kelas", txtKelas);
//                    params.put("telp", txtTelp);
//                    params.put("level", txtLevel);
//                    return params;
//                }
//            };
//            RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
//            requestQueue.add(request);
//    }

    private void InsertUser() {
        final String txtNama = dtName.getText().toString();
        final String txtUnama = dtUname.getText().toString();
        final String txtKelas = dtKelas.getSelectedItem().toString();
        final String txtTelp = dtTelp.getText().toString();
        final String txtLevel = dtLevel.getSelectedItem().toString();
        final ProgressDialog progressDialog = new ProgressDialog(DashboardGuruActivity.this);
        progressDialog.setMessage("Loading . . .");
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, UserAPI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
                                Toast.makeText(DashboardGuruActivity.this, "Berhasil Mendaftar!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                dialog2.dismiss();
                            } else if(response.equalsIgnoreCase("duplicated")){
                                Toast.makeText(DashboardGuruActivity.this, "Username Sudah Terpakai!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else {
                                Toast.makeText(DashboardGuruActivity.this, "Gagal, Terjadi Masalah!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DashboardGuruActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", txtUnama);
                    params.put("nama", txtNama);
                    params.put("kelas", txtKelas);
                    params.put("telp", txtTelp);
                    params.put("level", txtLevel);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardGuruActivity.this);
            requestQueue.add(request);
    }

    private void UpdateUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        final String strID = duID.getText().toString();
        final String struName = duUname.getText().toString();
        final String strName = duNama.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(DashboardGuruActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                getUserDetail();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            System.out.println(e.toString());
                            Toast.makeText(DashboardGuruActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                        Toast.makeText(DashboardGuruActivity.this, "Error Server", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nisn", strID);
                params.put("username", struName);
                params.put("nama", strName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
}