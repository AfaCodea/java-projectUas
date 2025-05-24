package com.perusahaananda.perpustakaan.model;

public abstract class ItemPerpustakaan {
    protected String judul;
    protected boolean tersedia = true;

    public ItemPerpustakaan(String judul) {
        this.judul = judul;
        this.tersedia = true;
    }

    public String getJudul() {
        return judul;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public abstract String getJenisItem();
    public abstract String getInfoDetail();
}
