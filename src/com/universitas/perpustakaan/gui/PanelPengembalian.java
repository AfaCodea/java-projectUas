package com.universitas.perpustakaan.gui;

import javax.swing.*;

import com.universitas.perpustakaan.model.Buku;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.util.List;

public class PanelPengembalian extends JPanel {
    private Perpustakaan perpustakaan;
    private PanelRiwayatTransaksi panelRiwayatTransaksi; // Reference to refresh transaction history
    private PanelBuku panelBuku; // Reference to refresh book table status

    private JTextField txtIsbnUntukKembali;
    private JButton btnKembali;

    public PanelPengembalian(Perpustakaan perpustakaan, PanelRiwayatTransaksi panelRiwayatTransaksi, PanelBuku panelBuku) {
        this.perpustakaan = perpustakaan;
        this.panelRiwayatTransaksi = panelRiwayatTransaksi;
        this.panelBuku = panelBuku;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Proses Pengembalian"));

        // Panel Input Pengembalian
        JPanel panelInputPengembalian = new JPanel(new GridLayout(2, 2, 5, 5));

        panelInputPengembalian.add(new JLabel("ISBN Buku yang Dikembalikan:"));
        txtIsbnUntukKembali = new JTextField();
        txtIsbnUntukKembali.setPreferredSize(new Dimension(txtIsbnUntukKembali.getPreferredSize().width, 30));
        panelInputPengembalian.add(txtIsbnUntukKembali);

        btnKembali = new JButton("Kembalikan Buku");
        panelInputPengembalian.add(btnKembali);
        panelInputPengembalian.add(new JLabel()); // Placeholder

        add(panelInputPengembalian, BorderLayout.NORTH);

        // Action Listener
        btnKembali.addActionListener(e -> prosesPengembalianAction());
    }

    private void prosesPengembalianAction() {
        String isbn = txtIsbnUntukKembali.getText();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan ISBN buku yang akan dikembalikan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Buku buku = perpustakaan.cariBukuByIsbn(isbn);
        if (buku == null) {
            JOptionPane.showMessageDialog(this, "Buku dengan ISBN " + isbn + " tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (buku.isTersedia()){
             JOptionPane.showMessageDialog(this, "Buku '" + buku.getJudul() + "' tidak sedang dipinjam.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean sukses = perpustakaan.prosesPengembalian(isbn);
        if (sukses) {
            JOptionPane.showMessageDialog(this, "Buku '" + buku.getJudul() + "' berhasil dikembalikan.");
            // Refresh data di panel terkait setelah pengembalian
            if (panelRiwayatTransaksi != null) {
                panelRiwayatTransaksi.refreshTabelTransaksi(); // Refresh tabel transaksi
            }
             if (panelBuku != null) {
                panelBuku.refreshTabelBuku(); // Refresh tabel buku di PanelBuku
            }
            txtIsbnUntukKembali.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengembalikan buku. Cek konsol untuk detail.", "Error Pengembalian", JOptionPane.ERROR_MESSAGE);
        }
    }

     public void refreshData() {
        // No specific data to refresh in this panel itself, data is updated via refresh calls to other panels
    }
} 