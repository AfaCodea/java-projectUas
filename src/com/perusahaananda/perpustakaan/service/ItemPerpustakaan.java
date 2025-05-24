package com.perusahaananda.perpustakaan.service;

/**
 * 8. Abstraction: Kelas abstrak sebagai dasar untuk item di perpustakaan.
 * Kelas ini tidak bisa diinstansiasi secara langsung.
 */
public abstract class ItemPerpustakaan {
    // 2. Variables
    // 3. Access Modifiers: protected agar bisa diakses oleh subclass
    protected String judul;
    protected boolean tersedia;

    // 4. Constructors
    public ItemPerpustakaan(String judul) {
        this.judul = judul;
        this.tersedia = true; // Defaultnya tersedia saat dibuat
    }

    // 2. Methods
    // 6. Encapsulation: Getter dan Setter
    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }

    // 8. Abstraction: Metode abstrak yang harus diimplementasikan oleh subclass
    public abstract String getJenisItem();

    // 7. Polymorphism (Overriding): Metode ini bisa di-override oleh subclass
    public String getInfoDetail() {
        return "Judul: " + judul + ", Tersedia: " + (tersedia ? "Ya" : "Tidak");
    }
}