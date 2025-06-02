package com.universitas.perpustakaan.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.universitas.perpustakaan.model.Buku;
import com.universitas.perpustakaan.model.Mahasiswa;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to main panel
        setBackground(Color.WHITE);

        // Panel Input Peminjaman (matching PanelBuku structure)
        JPanel panelInputPeminjaman = new JPanel(new BorderLayout(5, 5)); // Use BorderLayout for main input panel
        panelInputPeminjaman.setBackground(Color.WHITE);
        panelInputPeminjaman.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Proses Peminjaman", // Titled border with same style as PanelBuku
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(60, 60, 60)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding inside titled border
        ));

        // Panel for form fields (matching PanelBuku form layout)
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5)); // GridLayout for labels and combo boxes
        formPanel.setBackground(Color.WHITE);

        JLabel lblBuku = new JLabel("Pilih Buku (Tersedia):");
        lblBuku.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(lblBuku);

        comboBuku = new JComboBox<>();
        comboBuku.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBuku.setPreferredSize(new Dimension(comboBuku.getPreferredSize().width, 30)); // Consistent height
        formPanel.add(comboBuku);

        JLabel lblAnggota = new JLabel("Pilih Mahasiswa:");
        lblAnggota.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(lblAnggota);

        comboAnggota = new JComboBox<>();
        comboAnggota.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboAnggota.setPreferredSize(new Dimension(comboAnggota.getPreferredSize().width, 30)); // Consistent height
        formPanel.add(comboAnggota);

        // Panel for button (matching PanelBuku button panel structure and styling)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // FlowLayout aligned right
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1), // Thin light grey border
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
        ));
        
        btnPinjam = new JButton("Pinjam Buku");
        
        // Styling tombol pinjam (matching PanelBuku button style)
        btnPinjam.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPinjam.setBackground(new Color(52, 152, 219));
        btnPinjam.setForeground(Color.WHITE);
        btnPinjam.setFocusPainted(false);
        btnPinjam.setBorderPainted(true);
        btnPinjam.setOpaque(true);
        btnPinjam.setContentAreaFilled(true);
        btnPinjam.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Hover effect (matching PanelBuku button hover effect)
        btnPinjam.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnPinjam.setBackground(new Color(41, 128, 128)); // Slightly darker blue on hover
            }

            public void mouseExited(MouseEvent evt) {
                btnPinjam.setBackground(new Color(52, 152, 219)); // Original blue
            }
            
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    btnPinjam.setBackground(new Color(41, 128, 128)); // Darker blue on press
                    btnPinjam.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(), // Lowered effect on press
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                }
            }
            
            public void mouseReleased(MouseEvent evt) {
                btnPinjam.setBackground(new Color(52, 152, 219)); // Return to original blue
                btnPinjam.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(), // Return to raised effect
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        buttonPanel.add(btnPinjam);

        // Add form and button panels to the main input panel
        panelInputPeminjaman.add(formPanel, BorderLayout.CENTER);
        panelInputPeminjaman.add(buttonPanel, BorderLayout.SOUTH);

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