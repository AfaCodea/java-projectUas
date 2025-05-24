package com.perusahaananda.perpustakaan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 1. Class: Merepresentasikan mahasiswa perpustakaan.
 */
public class Mahasiswa {
    // 2. Variables
    // 3. Access Modifiers: private untuk enkapsulasi
    private String nama;
    private String idMahasiswa;
    private List<Buku> riwayatPinjam; // Contoh sederhana riwayat

    // 4. Constructors
    public Mahasiswa(String nama, String idMahasiswa) {
        this.nama = nama;
        this.idMahasiswa = idMahasiswa;
        this.riwayatPinjam = new ArrayList<>();
    }

    // 2. Methods
    // 6. Encapsulation: Getter dan Setter
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIdAnggota() {
        return idMahasiswa;
    }

    public void setIdAnggota(String idMahasiswa) {
        this.idMahasiswa = idMahasiswa;
    }

    public List<Buku> getRiwayatPinjam() {
        return riwayatPinjam;
    }

    // Metode untuk manajemen riwayat
    public void tambahKeRiwayat(Buku buku) {
        this.riwayatPinjam.add(buku);
    }

    // Overriding toString untuk representasi string yang lebih baik
    @Override
    public String toString() {
        return nama + " (ID: " + idMahasiswa + ")";
    }

    public Object getIdMahasiswa() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getIdMahasiswa'");
    }
}