package com.example.daringapps.Views.Guru.Absensi;

public class DataAbsensi {
    public String id, nisn, nama, kelas, tanggal;

    public DataAbsensi(){

    }

    public DataAbsensi(String id, String nisn, String nama, String kelas, String tanggal) {
        this.id = id;
        this.nisn = nisn;
        this.nama = nama;
        this.kelas = kelas;
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
