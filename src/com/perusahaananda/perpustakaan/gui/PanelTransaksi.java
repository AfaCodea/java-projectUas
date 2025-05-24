package com.perusahaananda.perpustakaan.gui;

import com.perusahaananda.perpustakaan.model.Mahasiswa;
import com.perusahaananda.perpustakaan.model.Buku;
import com.perusahaananda.perpustakaan.model.Peminjaman;
import com.perusahaananda.perpustakaan.service.Perpustakaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class PanelTransaksi extends JPanel {
    private Perpustakaan perpustakaan;
    private PanelBuku panelBuku; // Untuk refresh tabel buku setelah transaksi

    private JComboBox<Buku> comboBuku;
    private JComboBox<Mahasiswa> comboAnggota;
    private JButton btnPinjam, btnKembali, btnRefreshTransaksi;
    private JTextField txtIsbnUntukKembali;

    private JTable tabelTransaksi;
    private DefaultTableModel tableModelTransaksi;

    public PanelTransaksi(Perpustakaan perpustakaan, PanelBuku panelBuku) {
        this.perpustakaan = perpustakaan;
        this.panelBuku = panelBuku;
        setLayout(new BorderLayout(10, 10));

        // Panel Peminjaman
        JPanel panelPeminjaman = new JPanel(new GridLayout(3, 2, 5, 5));
        panelPeminjaman.setBorder(BorderFactory.createTitledBorder("Proses Peminjaman"));

        panelPeminjaman.add(new JLabel("Pilih Buku (Tersedia):"));
        comboBuku = new JComboBox<>();
        panelPeminjaman.add(comboBuku);

        panelPeminjaman.add(new JLabel("Pilih Anggota:"));
        comboAnggota = new JComboBox<>();
        panelPeminjaman.add(comboAnggota);

        btnPinjam = new JButton("Pinjam Buku");
        panelPeminjaman.add(btnPinjam);
        panelPeminjaman.add(new JLabel()); // Placeholder

        // Panel Pengembalian
        JPanel panelPengembalian = new JPanel(new GridLayout(2, 2, 5, 5));
        panelPengembalian.setBorder(BorderFactory.createTitledBorder("Proses Pengembalian"));

        panelPengembalian.add(new JLabel("ISBN Buku yang Dikembalikan:"));
        txtIsbnUntukKembali = new JTextField();
        panelPengembalian.add(txtIsbnUntukKembali);

        btnKembali = new JButton("Kembalikan Buku");
        panelPengembalian.add(btnKembali);
        panelPengembalian.add(new JLabel()); // Placeholder

        // Gabungkan panel input transaksi
        JPanel panelInputTransaksi = new JPanel(new BorderLayout(5,5));
        panelInputTransaksi.add(panelPeminjaman, BorderLayout.NORTH);
        panelInputTransaksi.add(panelPengembalian, BorderLayout.CENTER);

        add(panelInputTransaksi, BorderLayout.NORTH);

        // Panel Tabel Transaksi (Buku yang sedang dipinjam)
        String[] columnNamesTransaksi = {"ISBN Buku", "Judul Buku", "ID Anggota", "Nama Anggota", "Tgl Pinjam", "Tgl Kembali Diharapkan"};
        tableModelTransaksi = new DefaultTableModel(columnNamesTransaksi, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelTransaksi = new JTable(tableModelTransaksi);
        JScrollPane scrollPaneTabelTransaksi = new JScrollPane(tabelTransaksi);
        scrollPaneTabelTransaksi.setBorder(BorderFactory.createTitledBorder("Daftar Buku Sedang Dipinjam"));
        add(scrollPaneTabelTransaksi, BorderLayout.CENTER);

        // Panel Tombol Bawah
        JPanel panelTombolBawah = new JPanel();
        btnRefreshTransaksi = new JButton("Refresh Data Transaksi & Pilihan");
        panelTombolBawah.add(btnRefreshTransaksi);
        add(panelTombolBawah, BorderLayout.SOUTH);

        // Action Listeners
        btnPinjam.addActionListener(e -> prosesPeminjamanAction());
        btnKembali.addActionListener(e -> prosesPengembalianAction());
        btnRefreshTransaksi.addActionListener(e -> refreshData());

        // Muat data awal untuk ComboBox dan Tabel
        refreshData();
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

    private void refreshTabelTransaksi() {
        tableModelTransaksi.setRowCount(0);
        List<Peminjaman> daftarPinjamAktif = perpustakaan.getDaftarPeminjamanAktif();
        for (Peminjaman p : daftarPinjamAktif) {
            Vector<Object> row = new Vector<>();
            row.add(p.getBuku().getIsbn());
            row.add(p.getBuku().getJudul());
            row.add(p.getAnggota().getIdAnggota());
            row.add(p.getAnggota().getNama());
            row.add(p.getTanggalPinjam().toString());
            row.add(p.getTanggalKembaliDiharapkan() != null ? p.getTanggalKembaliDiharapkan().toString() : "N/A");
            tableModelTransaksi.addRow(row);
        }
    }

    public void refreshData() {
        loadComboBoxData();
        refreshTabelTransaksi();
        if (panelBuku != null) {
            panelBuku.refreshTabelBuku(); // Refresh juga tabel buku di PanelBuku
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
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal meminjam buku. Cek konsol untuk detail.", "Error Peminjaman", JOptionPane.ERROR_MESSAGE);
        }
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
            refreshData();
            txtIsbnUntukKembali.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengembalikan buku. Cek konsol untuk detail.", "Error Pengembalian", JOptionPane.ERROR_MESSAGE);
        }
    }
}