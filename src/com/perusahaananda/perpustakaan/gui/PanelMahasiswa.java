package com.perusahaananda.perpustakaan.gui;

import com.perusahaananda.perpustakaan.model.Mahasiswa;
import com.perusahaananda.perpustakaan.service.Perpustakaan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class PanelMahasiswa extends JPanel {
    private Perpustakaan perpustakaan;
    private JTextField txtNamaMahasiswa, txtIdMahasiswa;
    private JButton btnTambahMahasiswa, btnRefreshMahasiswa, btnHapusMahasiswa;
    private JTable tabelMahasiswa;
    private DefaultTableModel tableModelMahasiswa;

    public PanelMahasiswa(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 10));

        // Panel Input Mahasiswa
        JPanel panelInputMahasiswa = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInputMahasiswa.setBorder(BorderFactory.createTitledBorder("Registrasi"));

        panelInputMahasiswa.add(new JLabel("Nama Mahasiswa:"));
        txtNamaMahasiswa = new JTextField();
        panelInputMahasiswa.add(txtNamaMahasiswa);

        panelInputMahasiswa.add(new JLabel("ID Mahasiswa:"));
        txtIdMahasiswa = new JTextField();
        panelInputMahasiswa.add(txtIdMahasiswa);

        btnTambahMahasiswa = new JButton("Tambah Mahasiswa");
        panelInputMahasiswa.add(btnTambahMahasiswa);
        
        btnHapusMahasiswa = new JButton("Hapus Mahasiswa");
        panelInputMahasiswa.add(btnHapusMahasiswa);

        add(panelInputMahasiswa, BorderLayout.NORTH);

        // Panel Tabel Mahasiswa
        String[] columnNamesMahasiswa = {"ID Mahasiswa", "Nama Mahasiswa"};
        tableModelMahasiswa = new DefaultTableModel(columnNamesMahasiswa, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelMahasiswa = new JTable(tableModelMahasiswa);
        JScrollPane scrollPaneTabelMahasiswa = new JScrollPane(tabelMahasiswa);
        scrollPaneTabelMahasiswa.setBorder(BorderFactory.createTitledBorder("Daftar Mahasiswa"));
        add(scrollPaneTabelMahasiswa, BorderLayout.CENTER);

        // Panel Tombol Bawah
        JPanel panelTombolBawah = new JPanel();
        btnRefreshMahasiswa = new JButton("Refresh Daftar Mahasiswa");
        panelTombolBawah.add(btnRefreshMahasiswa);
        add(panelTombolBawah, BorderLayout.SOUTH);

        // Action Listeners
        btnTambahMahasiswa.addActionListener(e -> tambahMahasiswaAction());
        btnRefreshMahasiswa.addActionListener(e -> refreshTabelMahasiswa());
        btnHapusMahasiswa.addActionListener(e -> hapusMahasiswaAction());

        // Muat data awal
        refreshTabelMahasiswa();
    }

    private void tambahMahasiswaAction() {
        String nama = txtNamaMahasiswa.getText();
        String id = txtIdMahasiswa.getText();

        if (nama.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan ID Mahasiswa harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (perpustakaan.cariMahasiswaById(id) != null) {
             JOptionPane.showMessageDialog(this, "Mahasiswa dengan ID tersebut sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Mahasiswa mahasiswaBaru = new Mahasiswa(nama, id);
        perpustakaan.registrasiMahasiswa(mahasiswaBaru);
        JOptionPane.showMessageDialog(this, "Mahasiswa berhasil diregistrasi!");
        refreshTabelMahasiswa();
        // Kosongkan field
        txtNamaMahasiswa.setText("");
        txtIdMahasiswa.setText("");
    }

    private void hapusMahasiswaAction() {
        int selectedRow = tabelMahasiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Mahasiswa yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idMahasiswa = (String) tableModelMahasiswa.getValueAt(selectedRow, 0);
        String namaMahasiswa = (String) tableModelMahasiswa.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus mahasiswa " + namaMahasiswa + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (perpustakaan.hapusMahasiswa(idMahasiswa)) {
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil dihapus!");
                refreshTabelMahasiswa();
            }
        }
    }

    public void refreshTabelMahasiswa() {
        tableModelMahasiswa.setRowCount(0);
        List<Mahasiswa> daftarMahasiswa = perpustakaan.getSemuaMahasiswa();
        for (Mahasiswa mahasiswa : daftarMahasiswa) {
            Vector<Object> row = new Vector<>();
            row.add(mahasiswa.getIdMahasiswa());
            row.add(mahasiswa.getNama());
            tableModelMahasiswa.addRow(row);
        }
    }
}