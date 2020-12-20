package com.example.daringapps.Views.Murid.Riwayat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.daringapps.R;
import com.example.daringapps.Views.Murid.DashboardMuridActivity;
import com.example.daringapps.Views.Murid.Tugas.TugasActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RiwayatActivity extends AppCompatActivity {

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
    }
}