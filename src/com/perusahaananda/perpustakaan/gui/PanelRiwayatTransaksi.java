package com.perusahaananda.perpustakaan.gui;

import com.perusahaananda.perpustakaan.model.Peminjaman;
import com.perusahaananda.perpustakaan.service.Perpustakaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

// Renamed from PanelTransaksi to PanelRiwayatTransaksi
public class PanelRiwayatTransaksi extends JPanel {
    private Perpustakaan perpustakaan;

    private JTable tabelTransaksi;
    private DefaultTableModel tableModelTransaksi;
    private JButton btnRefreshTransaksi;

    // Constructor updated to not require PanelBuku
    public PanelRiwayatTransaksi(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 10));

        // Panel Tabel Transaksi (Riwayat Peminjaman dan Pengembalian)
        String[] columnNamesTransaksi = {"ISBN Buku", "Judul Buku", "ID Anggota", "Nama Anggota", "Tgl Pinjam", "Tgl Kembali Aktual"};
        tableModelTransaksi = new DefaultTableModel(columnNamesTransaksi, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelTransaksi = new JTable(tableModelTransaksi);
        JScrollPane scrollPaneTabelTransaksi = new JScrollPane(tabelTransaksi);
        scrollPaneTabelTransaksi.setBorder(BorderFactory.createTitledBorder("Riwayat Transaksi Peminjaman/Pengembalian"));
        add(scrollPaneTabelTransaksi, BorderLayout.CENTER);

        // Panel Tombol Bawah
        JPanel panelTombolBawah = new JPanel();
        btnRefreshTransaksi = new JButton("Refresh Riwayat Transaksi");
        panelTombolBawah.add(btnRefreshTransaksi);
        add(panelTombolBawah, BorderLayout.SOUTH);

        // Action Listener
        btnRefreshTransaksi.addActionListener(e -> refreshTabelTransaksi());

        // Muat data awal
        refreshTabelTransaksi();
    }

    // Made public to be callable from other panels for refreshing
    public void refreshTabelTransaksi() {
        tableModelTransaksi.setRowCount(0);
        List<Peminjaman> semuaTransaksi = perpustakaan.getSemuaTransaksiPeminjaman(); // Get all transactions
        for (Peminjaman p : semuaTransaksi) {
            Vector<Object> row = new Vector<>();
            row.add(p.getBuku().getIsbn());
            row.add(p.getBuku().getJudul());
            row.add(p.getAnggota().getIdAnggota());
            row.add(p.getAnggota().getNama());
            row.add(p.getTanggalPinjam().toString());
            row.add(p.getTanggalKembaliAktual() != null ? p.getTanggalKembaliAktual().toString() : "Belum Dikembalikan");
            tableModelTransaksi.addRow(row);
        }
    }

    // refreshData method no longer needed as its logic is now in refreshTabelTransaksi
    // @Override
    // public void refreshData() {
    //     refreshTabelTransaksi();
    // }
}