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
    private JTable tabelAMahasiswa;
    private DefaultTableModel tableModelMahasiswa;

    public PanelMahasiswa(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 10));

        // Panel Input Anggota
        JPanel panelInputAnggota = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInputAnggota.setBorder(BorderFactory.createTitledBorder("Registrasi Anggota"));

        panelInputAnggota.add(new JLabel("Nama Mahasiswa:"));
        txtNamaMahasiswa = new JTextField();
        panelInputAnggota.add(txtNamaMahasiswa);

        panelInputAnggota.add(new JLabel("ID Mahasiswa:"));
        txtIdMahasiswa = new JTextField();
        panelInputAnggota.add(txtIdMahasiswa);

        btnTambahMahasiswa = new JButton("Tambah Mahasiswa");
        panelInputAnggota.add(btnTambahMahasiswa);
        
        btnHapusMahasiswa = new JButton("Hapus Anggota");
        panelInputAnggota.add(btnHapusMahasiswa);

        add(panelInputAnggota, BorderLayout.NORTH);

        // Panel Tabel Anggota
        String[] columnNamesAnggota = {"ID Mahasiswa", "Nama Mahasiswa"};
        tableModelMahasiswa = new DefaultTableModel(columnNamesAnggota, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelMahasiswa = new JTable(tableModelMahasiswa);
        JScrollPane scrollPaneTabelAnggota = new JScrollPane(tabelMahasiswa);
        scrollPaneTabelAnggota.setBorder(BorderFactory.createTitledBorder("Daftar Anggota"));
        add(scrollPaneTabelAnggota, BorderLayout.CENTER);

        // Panel Tombol Bawah
        JPanel panelTombolBawah = new JPanel();
        btnRefreshMahasiswa = new JButton("Refresh Daftar Anggota");
        panelTombolBawah.add(btnRefreshMahasiswa);
        add(panelTombolBawah, BorderLayout.SOUTH);

        // Action Listeners
        btnTambahAnggota.addActionListener(e -> tambahAnggotaAction());
        btnRefreshAnggota.addActionListener(e -> refreshTabelAnggota());
        btnHapusAnggota.addActionListener(e -> hapusAnggotaAction());

        // Muat data awal
        refreshTabelAnggota();
    }

    private void tambahAnggotaAction() {
        String nama = txtNamaAnggota.getText();
        String id = txtIdAnggota.getText();

        if (nama.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama dan ID Anggota harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (perpustakaan.cariAnggotaById(id) != null) {
             JOptionPane.showMessageDialog(this, "Anggota dengan ID tersebut sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Mahasiswa anggotaBaru = new Mahasiswa(nama, id);
        perpustakaan.registrasiAnggota(anggotaBaru);
        JOptionPane.showMessageDialog(this, "Anggota berhasil diregistrasi!");
        refreshTabelAnggota();
        // Kosongkan field
        txtNamaAnggota.setText("");
        txtIdAnggota.setText("");
    }

    private void hapusAnggotaAction() {
        int selectedRow = tabelAnggota.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih anggota yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idAnggota = (String) tableModelAnggota.getValueAt(selectedRow, 0);
        String namaAnggota = (String) tableModelAnggota.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus anggota " + namaAnggota + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (perpustakaan.hapusAnggota(idAnggota)) {
                JOptionPane.showMessageDialog(this, "Anggota berhasil dihapus!");
                refreshTabelAnggota();
            }
        }
    }

    public void refreshTabelAnggota() {
        tableModelAnggota.setRowCount(0);
        List<Mahasiswa> daftarAnggota = perpustakaan.getSemuaAnggota();
        for (Mahasiswa anggota : daftarAnggota) {
            Vector<Object> row = new Vector<>();
            row.add(anggota.getIdAnggota());
            row.add(anggota.getNama());
            tableModelAnggota.addRow(row);
        }
    }
}