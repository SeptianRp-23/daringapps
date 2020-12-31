package com.example.daringapps.Views.Guru.Absensi;

public class DataAbsensiDetail {

    public String id, nisn, nama, kelas, tanggal, jam, ket;

    public DataAbsensiDetail(){
    }

    public DataAbsensiDetail(String id, String nisn, String nama, String kelas, String tanggal, String jam, String ket) {
        this.id = id;
        this.nisn = nisn;
        this.nama = nama;
        this.kelas = kelas;
        this.tanggal = tanggal;
        this.jam = jam;
        this.ket = ket;
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

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }
}
