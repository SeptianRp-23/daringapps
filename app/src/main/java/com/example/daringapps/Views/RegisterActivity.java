package com.example.daringapps.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Button btn_regist;
    private EditText etPass;
    private MaterialEditText etUname, etNama, etKelas;
    private String etLevel = "murid";
    private String RegistAPI = Server.URL_API + "register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUname = findViewById(R.id.reg_username);
        etNama = findViewById(R.id.reg_nama);
        etKelas = findViewById(R.id.reg_kelas);
        etPass = findViewById(R.id.reg_pass);
        btn_regist = findViewById(R.id.btn_register);

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
            }
        });
    }

    private void InsertData() {
        final String txtUname = etUname.getText().toString().trim();
        final String txtNama = etNama.getText().toString().trim();
        final String txtKelas = etKelas.getText().toString().trim();
        final String txtLevel = etLevel;
        final String txtPass = etPass.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading . . .");

        if (txtNama.isEmpty() || txtKelas.isEmpty() || txtUname.isEmpty() || txtPass.isEmpty() ) {
            Toast.makeText(RegisterActivity.this, "Data Belum Lengkap!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, RegistAPI,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("success")) {
                                Toast.makeText(RegisterActivity.this, "Berhasil Mendaftar!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if(response.equalsIgnoreCase("duplicated")){
                                Toast.makeText(RegisterActivity.this, "Username Sudah Terpakai!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Gagal, Terjadi Masalah!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Error Connection" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", txtUname);
                    params.put("nama", txtNama);
                    params.put("kelas", txtKelas);
                    params.put("password", txtPass);
                    params.put("level", txtLevel);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(request);
        }
    }
}