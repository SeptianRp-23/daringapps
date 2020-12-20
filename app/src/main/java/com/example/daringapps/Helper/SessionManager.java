package com.example.daringapps.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.daringapps.Views.LoginActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    //user
    private static final String PREF_NAME = "LOGIN";
    public static final String LOGIN = "IS_LOGIN";
    public static final String LOGED = "IS_LOGED";
    public static final String USERNAME = "USERNAME";
    public static final String NAMA = "NAMA";
    public static final String KELAS = "KELAS";
    public static final String LEVEL = "LEVEL";
    public static final String NISN = "NISN";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession (String username, String nama, String kelas, String level, String nisn){
        editor.putBoolean(LOGIN, true);
        editor.putBoolean(LOGED, true);
        editor.putString(USERNAME, username);
        editor.putString(NAMA, nama);
        editor.putString(KELAS, kelas);
        editor.putString(LEVEL, level);
        editor.putString(NISN, nisn);
        editor.apply();
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(NAMA, sharedPreferences.getString(NAMA, null));
        user.put(KELAS, sharedPreferences.getString(KELAS, null));
        user.put(NISN, sharedPreferences.getString(NISN, null));

        return user;
    }
}
