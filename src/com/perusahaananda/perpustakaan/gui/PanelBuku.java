package com.perusahaananda.perpustakaan.gui;

import com.perusahaananda.perpustakaan.model.Buku;
import com.perusahaananda.perpustakaan.service.Perpustakaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class PanelBuku extends JPanel {
    private Perpustakaan perpustakaan;
    private JTextField txtJudul, txtPengarang, txtIsbn;
    private JButton btnTambah, btnRefresh, btnCari;
    private JTable tabelBuku;
    private DefaultTableModel tableModel;

    public PanelBuku(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 10)); // Memberi sedikit jarak antar komponen

        // Panel Input
        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5)); // baris, kolom, hgap, vgap
        panelInput.setBorder(BorderFactory.createTitledBorder("Tambah/Cari Buku"));

        panelInput.add(new JLabel("Judul:"));
        txtJudul = new JTextField();
        panelInput.add(txtJudul);

        panelInput.add(new JLabel("Pengarang:"));
        txtPengarang = new JTextField();
        panelInput.add(txtPengarang);

        panelInput.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        panelInput.add(txtIsbn);

        btnTambah = new JButton("Tambah Buku");
        btnCari = new JButton("Cari Buku (by Judul)"); // Bisa juga cari by ISBN atau lainnya
        panelInput.add(btnTambah);
        panelInput.add(btnCari);

        add(panelInput, BorderLayout.NORTH);

        // Panel Tabel
        String[] columnNames = {"ISBN", "Judul", "Pengarang", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat sel tidak dapat diedit
            }
        };
        tabelBuku = new JTable(tableModel);
        JScrollPane scrollPaneTabel = new JScrollPane(tabelBuku);
        scrollPaneTabel.setBorder(BorderFactory.createTitledBorder("Daftar Buku"));
        add(scrollPaneTabel, BorderLayout.CENTER);

        // Panel Tombol Bawah
        JPanel panelTombolBawah = new JPanel();
        btnRefresh = new JButton("Refresh Daftar Buku");
        panelTombolBawah.add(btnRefresh);
        add(panelTombolBawah, BorderLayout.SOUTH);

        // Action Listeners
        btnTambah.addActionListener(e -> tambahBukuAction());
        btnRefresh.addActionListener(e -> refreshTabelBuku());
        btnCari.addActionListener(e -> cariBukuAction());

        // Muat data awal
        refreshTabelBuku();
    }

    private void tambahBukuAction() {
        String judul = txtJudul.getText();
        String pengarang = txtPengarang.getText();
        String isbn = txtIsbn.getText();

        if (judul.isEmpty() || pengarang.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (perpustakaan.cariBukuByIsbn(isbn) != null) {
            JOptionPane.showMessageDialog(this, "Buku dengan ISBN tersebut sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Buku bukuBaru = new Buku(judul, pengarang, isbn);
        perpustakaan.tambahBuku(bukuBaru);
        JOptionPane.showMessageDialog(this, "Buku berhasil ditambahkan!");
        refreshTabelBuku();
        // Kosongkan field
        txtJudul.setText("");
        txtPengarang.setText("");
        txtIsbn.setText("");
    }

    private void cariBukuAction() {
        String queryJudul = txtJudul.getText(); // Menggunakan field judul untuk pencarian
        if (queryJudul.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan judul buku yang ingin dicari.", "Info", JOptionPane.INFORMATION_MESSAGE);
            refreshTabelBuku(); // Tampilkan semua jika query kosong
            return;
        }

        List<Buku> hasilCari = perpustakaan.cariBukuByJudul(queryJudul);
        tableModel.setRowCount(0); // Kosongkan tabel
        if (hasilCari.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada buku yang cocok dengan judul '" + queryJudul + "'.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Buku buku : hasilCari) {
                Vector<Object> row = new Vector<>();
                row.add(buku.getIsbn());
                row.add(buku.getJudul());
                row.add(buku.getPengarang());
                row.add(buku.isTersedia() ? "Tersedia" : "Dipinjam");
                tableModel.addRow(row);
            }
        }
    }

    public void refreshTabelBuku() {
        tableModel.setRowCount(0); // Kosongkan tabel sebelum memuat data baru
        List<Buku> daftarBuku = perpustakaan.getSemuaBuku();
        for (Buku buku : daftarBuku) {
            Vector<Object> row = new Vector<>();
            row.add(buku.getIsbn());
            row.add(buku.getJudul());
            row.add(buku.getPengarang());
            row.add(buku.isTersedia() ? "Tersedia" : (buku.getPeminjam() != null ? "Dipinjam oleh " + buku.getPeminjam().getNama() : "Dipinjam"));
            tableModel.addRow(row);
        }
    }
}