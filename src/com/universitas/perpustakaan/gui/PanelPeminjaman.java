package com.universitas.perpustakaan.gui;

import javax.swing.*;

import com.universitas.perpustakaan.model.Buku;
import com.universitas.perpustakaan.model.Mahasiswa;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanelPeminjaman extends JPanel {
    private Perpustakaan perpustakaan;
    private PanelRiwayatTransaksi panelRiwayatTransaksi; // Reference to refresh transaction history
    private PanelBuku panelBuku; // Reference to refresh book table status

    private JComboBox<Buku> comboBuku;
    private JComboBox<Mahasiswa> comboAnggota;
    private JButton btnPinjam;

    public PanelPeminjaman(Perpustakaan perpustakaan, PanelRiwayatTransaksi panelRiwayatTransaksi, PanelBuku panelBuku) {
        this.perpustakaan = perpustakaan;
        this.panelRiwayatTransaksi = panelRiwayatTransaksi;
        this.panelBuku = panelBuku;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Proses Peminjaman"));

        // Panel Input Peminjaman
        JPanel panelInputPeminjaman = new JPanel(new GridLayout(3, 2, 5, 5));

        panelInputPeminjaman.add(new JLabel("Pilih Buku (Tersedia):"));
        comboBuku = new JComboBox<>();
        panelInputPeminjaman.add(comboBuku);

        panelInputPeminjaman.add(new JLabel("Pilih Anggota:"));
        comboAnggota = new JComboBox<>();
        panelInputPeminjaman.add(comboAnggota);

        btnPinjam = new JButton("Pinjam Buku");
        panelInputPeminjaman.add(btnPinjam);
        panelInputPeminjaman.add(new JLabel()); // Placeholder

        add(panelInputPeminjaman, BorderLayout.NORTH);

        // Action Listener
        btnPinjam.addActionListener(e -> prosesPeminjamanAction());

        // Initial data load
        loadComboBoxData();
    }

    private void loadComboBoxData() {
        // Load Buku yang tersedia
        comboBuku.removeAllItems();
        List<Buku> bukuTersedia = perpustakaan.getSemuaBuku().stream()
                .filter(Buku::isTersedia)
                .toList();
        for (Buku buku : bukuTersedia) {
            comboBuku.addItem(buku);
        }

        // Load Anggota
        comboAnggota.removeAllItems();
        List<Mahasiswa> semuaAnggota = perpustakaan.getSemuaAnggota();
        for (Mahasiswa anggota : semuaAnggota) {
            comboAnggota.addItem(anggota);
        }
    }

    private void prosesPeminjamanAction() {
        Buku bukuTerpilih = (Buku) comboBuku.getSelectedItem();
        Mahasiswa anggotaTerpilih = (Mahasiswa) comboAnggota.getSelectedItem();

        if (bukuTerpilih == null || anggotaTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Pilih buku dan anggota terlebih dahulu!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sukses = perpustakaan.prosesPeminjaman(bukuTerpilih.getIsbn(), anggotaTerpilih.getIdAnggota(), LocalDate.now());
        if (sukses) {
            JOptionPane.showMessageDialog(this, "Buku '" + bukuTerpilih.getJudul() + "' berhasil dipinjam oleh " + anggotaTerpilih.getNama());
            // Refresh data di panel terkait setelah peminjaman
            loadComboBoxData(); // Refresh list buku tersedia
            if (panelRiwayatTransaksi != null) {
                panelRiwayatTransaksi.refreshTabelTransaksi(); // Refresh tabel transaksi
            }
             if (panelBuku != null) {
                panelBuku.refreshTabelBuku(); // Refresh tabel buku di PanelBuku
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal meminjam buku. Cek konsol untuk detail.", "Error Peminjaman", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        loadComboBoxData();
    }
} 