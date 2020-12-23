package com.example.daringapps.Views.Murid.Riwayat;

public class DataRiwayat {

    private String id, id_tugas, mapel, id_siswa, nama, kelas, tanggal, nilai, status;

    public DataRiwayat() {
    }

    public DataRiwayat(String id, String id_tugas, String mapel, String id_siswa, String nama, String kelas, String tanggal, String nilai, String status) {
        this.id = id;
        this.id_tugas = id_tugas;
        this.mapel = mapel;
        this.id_siswa = id_siswa;
        this.nama = nama;
        this.kelas = kelas;
        this.tanggal = tanggal;
        this.nilai = nilai;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_tugas() {
        return id_tugas;
    }

    public void setId_tugas(String id_tugas) {
        this.id_tugas = id_tugas;
    }

    public String getMapel() {
        return mapel;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }

    public String getId_siswa() {
        return id_siswa;
    }

    public void setId_siswa(String id_siswa) {
        this.id_siswa = id_siswa;
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

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
