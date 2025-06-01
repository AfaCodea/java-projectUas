package com.universitas.perpustakaan.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.universitas.perpustakaan.model.Mahasiswa;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.util.List;
import java.util.Vector;

public class PanelMahasiswa extends JPanel {
    private Perpustakaan perpustakaan;
    private JTextField txtNama, txtId;
    private JButton btnTambah, btnDelete;
    private JTable tabelMahasiswa;
    private DefaultTableModel tableModel;

    public PanelMahasiswa(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 10));

        // Panel Input Mahasiswa
        JPanel panelInputMahasiswa = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInputMahasiswa.setBorder(BorderFactory.createTitledBorder("Registrasi"));

        panelInputMahasiswa.add(new JLabel("Nama Mahasiswa:"));
        txtNama = new JTextField();
        panelInputMahasiswa.add(txtNama);

        panelInputMahasiswa.add(new JLabel("ID Mahasiswa:"));
        txtId = new JTextField();
        panelInputMahasiswa.add(txtId);

        btnTambah = new JButton("Tambah Mahasiswa");
        panelInputMahasiswa.add(btnTambah);
        
        btnDelete = new JButton("Hapus Mahasiswa");
        panelInputMahasiswa.add(btnDelete);

        add(panelInputMahasiswa, BorderLayout.NORTH);

        // Panel Tabel
        String[] columnNames = {"ID", "Nama"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat sel tidak dapat diedit
            }
        };
        tabelMahasiswa = new JTable(tableModel);
        JScrollPane scrollPaneTabel = new JScrollPane(tabelMahasiswa);
        scrollPaneTabel.setBorder(BorderFactory.createTitledBorder("Daftar Mahasiswa"));
        add(scrollPaneTabel, BorderLayout.CENTER);

        // Action Listeners
        btnTambah.addActionListener(e -> tambahMahasiswaAction());
        btnDelete.addActionListener(e -> deleteMahasiswaAction());

        // Muat data awal
        refreshTabelMahasiswa();
    }

    private void tambahMahasiswaAction() {
        String nama = txtNama.getText();
        String id = txtId.getText();

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
        txtNama.setText("");
        txtId.setText("");
    }

    private void deleteMahasiswaAction() {
        int selectedRow = tabelMahasiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Mahasiswa yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idMahasiswa = (String) tableModel.getValueAt(selectedRow, 0);
        String namaMahasiswa = (String) tableModel.getValueAt(selectedRow, 1);

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
        tableModel.setRowCount(0);
        List<Mahasiswa> daftarMahasiswa = perpustakaan.getSemuaMahasiswa();
        for (Mahasiswa mahasiswa : daftarMahasiswa) {
            Vector<Object> row = new Vector<>();
            row.add(mahasiswa.getIdMahasiswa());
            row.add(mahasiswa.getNama());
            tableModel.addRow(row);
        }
    }
}