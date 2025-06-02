package com.universitas.perpustakaan.gui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.universitas.perpustakaan.model.Buku;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to main panel
        setBackground(Color.WHITE);

        // Panel Input Pengembalian (matching PanelBuku structure)
        JPanel panelInputPengembalian = new JPanel(new BorderLayout(5, 5)); // Use BorderLayout for main input panel
        panelInputPengembalian.setBackground(Color.WHITE);
        panelInputPengembalian.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Proses Pengembalian", // Titled border with same style as PanelBuku
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(60, 60, 60)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding inside titled border
        ));

        // Panel for form fields (matching PanelBuku form layout)
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 5, 5)); // GridLayout for label and text field
        formPanel.setBackground(Color.WHITE);

        JLabel lblIsbn = new JLabel("ISBN Buku yang Dikembalikan:");
        lblIsbn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(lblIsbn);

        txtIsbnUntukKembali = new JTextField();
        txtIsbnUntukKembali.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtIsbnUntukKembali.setPreferredSize(new Dimension(txtIsbnUntukKembali.getPreferredSize().width, 30)); // Consistent height
        txtIsbnUntukKembali.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), // Thin border around text field
            BorderFactory.createEmptyBorder(0, 5, 0, 5) // Internal padding
        ));
        formPanel.add(txtIsbnUntukKembali);

        // Panel for button (matching PanelBuku button panel structure and styling)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // FlowLayout aligned right
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1), // Thin light grey border
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding
        ));
        
        btnKembali = new JButton("Kembalikan Buku");
        
        // Styling tombol kembali (matching PanelBuku button style)
        btnKembali.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnKembali.setBackground(new Color(52, 152, 219)); // Blue color
        btnKembali.setForeground(Color.WHITE);
        btnKembali.setFocusPainted(false);
        btnKembali.setBorderPainted(true);
        btnKembali.setOpaque(true);
        btnKembali.setContentAreaFilled(true);
        btnKembali.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Hover effect (matching PanelBuku button hover effect)
        btnKembali.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnKembali.setBackground(new Color(41, 128, 128)); // Slightly darker blue on hover
            }

            public void mouseExited(MouseEvent evt) {
                btnKembali.setBackground(new Color(52, 152, 219)); // Original blue
            }
            
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    btnKembali.setBackground(new Color(41, 128, 128)); // Darker blue on press
                    btnKembali.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(), // Lowered effect on press
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                }
            }
            
            public void mouseReleased(MouseEvent evt) {
                btnKembali.setBackground(new Color(52, 152, 219)); // Return to original blue
                btnKembali.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(), // Return to raised effect
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        buttonPanel.add(btnKembali);

        // Add form and button panels to the main input panel
        panelInputPengembalian.add(formPanel, BorderLayout.CENTER);
        panelInputPengembalian.add(buttonPanel, BorderLayout.SOUTH);

        add(panelInputPengembalian, BorderLayout.NORTH);

        // Action Listener
        btnKembali.addActionListener(e -> prosesPengembalianAction());

        // No initial data load needed for this panel
    }

    private void prosesPengembalianAction() {
        String isbn = txtIsbnUntukKembali.getText().trim();
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