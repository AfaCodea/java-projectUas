package com.universitas.perpustakaan.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.universitas.perpustakaan.util.Logger;

/**
 * 1. Class: Merepresentasikan sebuah buku.
 * 5. Inheritance: Buku adalah turunan dari ItemPerpustakaan.
 * Mengimplementasikan interface DapatDipinjam.
 */
public class Buku extends ItemPerpustakaan implements DapatDipinjam {
    // 2. Variables
    // 3. Access Modifiers: private untuk enkapsulasi
    private String pengarang;
    private String isbn;
    private LocalDate tanggalPinjam;
    private Mahasiswa peminjam;
    private final int LAMA_PEMINJAMAN_HARI = 14; // Konstanta lama peminjaman

    // 4. Constructors
    public Buku(String judul, String pengarang, String isbn) {
        super(judul); // Memanggil konstruktor superclass
        this.pengarang = pengarang;
        this.isbn = isbn;
        this.tanggalPinjam = null;
        this.peminjam = null;
    }

    // 2. Methods
    // 6. Encapsulation: Getter dan Setter
    public String getPengarang() {
        return pengarang;
    }

    public void setPengarang(String pengarang) {
        this.pengarang = pengarang;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Mahasiswa getPeminjam() {
        return peminjam;
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }

    // Implementasi metode dari interface DapatDipinjam
    @Override
    public void pinjamItem(Mahasiswa anggota, LocalDate tanggalPinjam) {
        if (isTersedia()) {
            this.tersedia = false;
            this.peminjam = anggota;
            this.tanggalPinjam = tanggalPinjam;
            Logger.log("Buku '" + getJudul() + "' berhasil dipinjam oleh " + anggota.getNama());
        } else {
            Logger.log("Maaf, buku '" + getJudul() + "' sedang tidak tersedia.");
        }
    }

    @Override
    public void kembalikanItem() {
        if (!isTersedia()) {
            this.tersedia = true;
            Logger.log("Buku '" + getJudul() + "' berhasil dikembalikan oleh " + (peminjam != null ? peminjam.getNama() : "seseorang") + ".");
            this.peminjam = null;
            this.tanggalPinjam = null;
        } else {
            Logger.log("Buku '" + getJudul() + "' memang sudah tersedia.");
        }
    }

    @Override
    public LocalDate getTanggalKembaliDiharapkan() {
        if (tanggalPinjam != null) {
            return tanggalPinjam.plus(LAMA_PEMINJAMAN_HARI, ChronoUnit.DAYS);
        }
        return null;
    }

    // 8. Abstraction: Implementasi metode abstrak dari superclass
    @Override
    public String getJenisItem() {
        return "Buku";
    }

    // 7. Polymorphism (Overriding): Meng-override metode dari superclass
    @Override
    public String getInfoDetail() {
        String detailSuper = "Judul: " + judul;
        String detailPeminjaman = "";
        if (!isTersedia() && peminjam != null && tanggalPinjam != null) {
            detailPeminjaman = ", Dipinjam oleh: " + peminjam.getNama() +
                               ", Tgl Pinjam: " + tanggalPinjam +
                               ", Tgl Kembali Diharapkan: " + getTanggalKembaliDiharapkan();
        }
        return detailSuper + ", Pengarang: " + pengarang + ", ISBN: " + isbn + detailPeminjaman;
    }

    // Overriding toString untuk representasi string yang lebih baik (berguna untuk ComboBox, dll)
    @Override
    public String toString() {
        return getJudul() + " (" + getIsbn() + ")";
    }
}