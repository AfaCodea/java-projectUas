package com.universitas.perpustakaan.model;

import java.time.LocalDate;

/**
 * 9. Interface: Mendefinisikan kontrak untuk item yang dapat dipinjam.
 */
public interface DapatDipinjam {
    void pinjamItem(Mahasiswa anggota, LocalDate tanggalPinjam);
    void kembalikanItem();
    LocalDate getTanggalKembaliDiharapkan();
}