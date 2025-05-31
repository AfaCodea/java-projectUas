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
    private JButton btnTambah, btnDelete;
    private JTable tabelBuku;
    private DefaultTableModel tableModel;

    public PanelBuku(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 10)); // Memberi sedikit jarak antar komponen

        // Panel Input
        JPanel panelInput = new JPanel(new BorderLayout(5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Tambah/Cari Buku"));

        // Panel untuk form input
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.add(new JLabel("Judul:"));
        txtJudul = new JTextField();
        formPanel.add(txtJudul);

        formPanel.add(new JLabel("Pengarang:"));
        txtPengarang = new JTextField();
        formPanel.add(txtPengarang);

        formPanel.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        formPanel.add(txtIsbn);

        // Panel untuk button
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTambah = new JButton("Tambah Buku");
        btnDelete = new JButton("Delete Buku");
        panelButton.add(btnTambah);
        panelButton.add(btnDelete);

        // Tambahkan form dan button ke panel input
        panelInput.add(formPanel, BorderLayout.CENTER);
        panelInput.add(panelButton, BorderLayout.SOUTH);

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

        // Action Listeners
        btnTambah.addActionListener(e -> tambahBukuAction());
        btnDelete.addActionListener(e -> deleteBukuAction());

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

    private void deleteBukuAction() {
        int selectedRow = tabelBuku.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih buku yang ingin dihapus!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        Buku buku = perpustakaan.cariBukuByIsbn(isbn);

        if (buku != null) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin menghapus buku '" + buku.getJudul() + "'?", 
                "Konfirmasi Hapus", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (!buku.isTersedia()) {
                    JOptionPane.showMessageDialog(this, 
                        "Tidak dapat menghapus buku yang sedang dipinjam!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                perpustakaan.hapusBuku(isbn);
                JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
                refreshTabelBuku();
            }
        }
    }
}