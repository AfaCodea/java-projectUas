package com.perusahaananda.perpustakaan.model;

import java.time.LocalDate;
/**
 * 1. Class: Merepresentasikan transaksi peminjaman.
 * (Catatan: Dalam implementasi Buku saat ini, detail peminjaman sudah ada di Buku.
 * Kelas ini bisa lebih berguna jika ada atribut tambahan khusus untuk transaksi,
 * atau jika ingin memisahkan data transaksi dari data item.
 * Untuk contoh ini, kita akan membuatnya sederhana.)
 */
public class Peminjaman {
    // 2. Variables
    private Buku buku;
    private Mahasiswa anggota;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembaliAktual; // Tanggal ketika buku benar-benar dikembalikan

    // 4. Constructors
    public Peminjaman(Buku buku, Mahasiswa anggota, LocalDate tanggalPinjam) {
        this.buku = buku;
        this.anggota = anggota;
        this.tanggalPinjam = tanggalPinjam;
        // tanggalKembaliAktual akan diisi saat pengembalian
    }

    // 2. Methods
    // 6. Encapsulation
    public Buku getBuku() {
        return buku;
    }

    public Mahasiswa getAnggota() {
        return anggota;
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }

    public LocalDate getTanggalKembaliAktual() {
        return tanggalKembaliAktual;
    }

    public void setTanggalKembaliAktual(LocalDate tanggalKembaliAktual) {
        this.tanggalKembaliAktual = tanggalKembaliAktual;
    }

    public LocalDate getTanggalKembaliDiharapkan() {
        if (buku instanceof DapatDipinjam) {
            return ((DapatDipinjam) buku).getTanggalKembaliDiharapkan();
        }
        return null; // Jika buku tidak DapatDipinjam (seharusnya tidak terjadi)
    }

    @Override
    public String toString() {
        return "Peminjaman: " +
               "Buku=" + (buku != null ? buku.getJudul() : "N/A") +
               ", Anggota=" + (anggota != null ? anggota.getNama() : "N/A") +
               ", Tgl Pinjam=" + tanggalPinjam +
               ", Tgl Kembali Diharapkan=" + getTanggalKembaliDiharapkan() +
               (tanggalKembaliAktual != null ? ", Tgl Kembali Aktual=" + tanggalKembaliAktual : "");
    }
}