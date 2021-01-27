package com.example.daringapps.Views;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.daringapps.Views.Murid.DashboardMuridActivity;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText etName, etPass;
    private Button btLogin;
    SessionManager sessionManager;
    private long backPressedTime;
    private Toast backToast;
    SharedPreferences sharedPreferences;
    CheckBox loginstate;
    TextView btRegist;
    private String LoginAPI = Server.URL_API + "login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        final MediaPlayer mpmulai = MediaPlayer.create(this, R.raw.ucapan);
        mpmulai.start();

        etName = findViewById(R.id.username);
        etPass = findViewById(R.id.password);
        btLogin = findViewById(R.id.btn_login);
        loginstate = findViewById(R.id.state);
        btRegist = findViewById(R.id.daftar);

        String loginstatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");
        if (loginstatus.equals("LoggedIn")){
            startActivity(new Intent(LoginActivity.this, DashboardMuridActivity.class));
        }
        else if (loginstatus.equals("LoggedOn")){
            startActivity(new Intent(LoginActivity.this, DashboardGuruActivity.class));
        }

        btRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(0,0);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = etName.getText().toString().trim();
                String mPass = etPass.getText().toString().trim();

                if (etName.getText().toString().length()==0){
                    etName.setError("Null Email");
                }
                else if (etPass.getText().toString().length()==0){
                    etPass.setError("Null Password");
                }
                else{
                    loginProces(mEmail, mPass);
                }
            }
        });

        checkPermission();
    }

    private void checkPermission(){

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(DashboardGuruActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
//                Toast.makeText(DashboardGuruActivity.this, "Permission not given", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(LoginActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                .check();
    }

    private void loginProces(final String username, final String password) {
        final MediaPlayer mpberhasil = MediaPlayer.create(this, R.raw.log_berhasil);
        final MediaPlayer mpgagal = MediaPlayer.create(this, R.raw.log_gagal);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        final Handler handler = new Handler();
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, LoginAPI,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    JSONArray jsonArray = jsonObject.getJSONArray("login");
                                    if (success.equals("1")) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String username = object.getString("username").trim();
                                            String nama = object.getString("nama").trim();
                                            String kelas = object.getString("kelas").trim();
                                            String level = object.getString("level").trim();
                                            String nisn = object.getString("nisn").trim();


//                                            mpmulai.start();

                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            if (loginstate.isChecked() && level.equals("murid")){
                                                editor.putString(getResources().getString(R.string.prefLoginState),"LoggedIn");
                                            }else if (loginstate.isChecked() && level.equals("guru")){
                                                editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOn");
                                            }
                                            else {
                                                editor.putString(getResources().getString(R.string.prefLoginState),"LoggedOut");
                                            }

                                            if (level.equals("murid")) {
                                                sessionManager.createSession(username, nama, kelas, level, nisn);
                                                editor.apply();
                                                final Intent inte = new Intent(LoginActivity.this, DashboardMuridActivity.class);
                                                inte.putExtra("kelas", kelas);
                                                inte.putExtra("nama", nama);
                                                inte.putExtra("nisn", nisn);
                                                mpberhasil.start();
                                                startActivity(inte);
                                                finish();
                                            }else if (level.equals("guru")){
                                                sessionManager.createSession(username, nama, kelas, level, nisn);
                                                editor.apply();
                                                mpberhasil.start();
                                                final Intent in = new Intent(LoginActivity.this, DashboardGuruActivity.class);
                                                in.putExtra("nama", nama);
                                                in.putExtra("nisn", nisn);
                                                startActivity(in);
                                                finish();
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    mpgagal.start();
                                    Toast.makeText(LoginActivity.this, "Error, Email Or Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                mpgagal.start();
                                Toast.makeText(LoginActivity.this, "Error, Check Connection" +error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                requestQueue.add(stringRequest);
            }
        }, 2000);

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