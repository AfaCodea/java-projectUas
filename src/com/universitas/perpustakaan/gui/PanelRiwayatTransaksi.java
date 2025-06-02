package com.universitas.perpustakaan.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.universitas.perpustakaan.model.Peminjaman;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.util.List;
import java.util.Vector;

// Renamed from PanelTransaksi to PanelRiwayatTransaksi
public class PanelRiwayatTransaksi extends JPanel {
    private Perpustakaan perpustakaan;

    private JTable tabelTransaksi;
    private DefaultTableModel tableModel;
    private JButton btnRefreshTransaksi;

    // Constructor updated to not require PanelBuku
    public PanelRiwayatTransaksi(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Tabel
        String[] columnNames = {"ID Mahasiswa", "Nama Mahasiswa", "ISBN Buku", "Judul Buku", "Tanggal Pinjam", "Tanggal Kembali", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelTransaksi = new JTable(tableModel);
        
        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel searchLabel = new JLabel("🔍");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchLabel.setForeground(new Color(100, 100, 100));
        
        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setPreferredSize(new Dimension(150, 25));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(0, 5, 0, 5)
        ));
        
        // Placeholder text
        searchField.putClientProperty("JTextField.placeholderText", "Cari...");
        
        // Action listener untuk pencarian
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(searchField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(searchField.getText()); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(searchField.getText()); }
        });
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        
        JScrollPane scrollPaneTabel = new JScrollPane(tabelTransaksi);
        scrollPaneTabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Riwayat Transaksi Peminjaman/Pengembalian",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(60, 60, 60)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Panel untuk search dan tabel
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPaneTabel, BorderLayout.CENTER);
        
        // Panel untuk tombol refresh
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        btnRefreshTransaksi = new JButton("Refresh Riwayat Transaksi");
        
        // Styling tombol refresh
        btnRefreshTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefreshTransaksi.setBackground(new Color(52, 152, 219));
        btnRefreshTransaksi.setForeground(Color.WHITE);
        btnRefreshTransaksi.setFocusPainted(false);
        btnRefreshTransaksi.setBorderPainted(true);
        btnRefreshTransaksi.setOpaque(true);
        btnRefreshTransaksi.setContentAreaFilled(true);
        btnRefreshTransaksi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Hover effect
        btnRefreshTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRefreshTransaksi.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRefreshTransaksi.setBackground(new Color(52, 152, 219));
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                    btnRefreshTransaksi.setBackground(new Color(41, 128, 185));
                    btnRefreshTransaksi.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                }
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnRefreshTransaksi.setBackground(new Color(52, 152, 219));
                btnRefreshTransaksi.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });
        
        buttonPanel.add(btnRefreshTransaksi);
        
        // Action Listener untuk tombol refresh
        btnRefreshTransaksi.addActionListener(e -> {
            // Periksa dan perbarui status pengembalian buku
            List<Peminjaman> daftarTransaksi = perpustakaan.getSemuaTransaksiPeminjaman();
            for (Peminjaman transaksi : daftarTransaksi) {
                if (transaksi.getTanggalKembaliAktual() != null && !transaksi.getBuku().isTersedia()) {
                    // Jika buku sudah dikembalikan tapi status masih dipinjam
                    perpustakaan.prosesPengembalian(transaksi.getBuku().getIsbn());
                }
            }
            
            // Refresh tampilan tabel
            refreshTabelTransaksi();
            JOptionPane.showMessageDialog(this, "Data riwayat transaksi berhasil diperbarui!", 
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        });
        
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Muat data awal
        refreshTabelTransaksi();

        // Styling untuk tabel
        tabelTransaksi.setShowGrid(true);
        tabelTransaksi.setGridColor(new Color(220, 220, 220));
        tabelTransaksi.setRowHeight(30);
        tabelTransaksi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Header styling
        tabelTransaksi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelTransaksi.getTableHeader().setBackground(new Color(240, 240, 240));
        tabelTransaksi.getTableHeader().setForeground(new Color(60, 60, 60));
        tabelTransaksi.getTableHeader().setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Selection styling
        tabelTransaksi.setSelectionBackground(new Color(230, 230, 230));
        tabelTransaksi.setSelectionForeground(new Color(0, 0, 0));
        
        // Table properties
        tabelTransaksi.setFillsViewportHeight(true);
        tabelTransaksi.setRowSelectionAllowed(true);
        tabelTransaksi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelTransaksi.setIntercellSpacing(new Dimension(0, 0));
        tabelTransaksi.setRowMargin(0);
        
        // Cell renderer untuk styling sel
        tabelTransaksi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Set alignment
                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                
                // Set background
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                
                return c;
            }
        });
    }

    // Made public to be callable from other panels for refreshing
    public void refreshTabelTransaksi() {
        tableModel.setRowCount(0);
        List<Peminjaman> daftarTransaksi = perpustakaan.getSemuaTransaksiPeminjaman();
        
        for (Peminjaman transaksi : daftarTransaksi) {
            Vector<Object> row = new Vector<>();
            row.add(transaksi.getAnggota().getIdAnggota());
            row.add(transaksi.getAnggota().getNama());
            row.add(transaksi.getBuku().getIsbn());
            row.add(transaksi.getBuku().getJudul());
            row.add(transaksi.getTanggalPinjam());
            row.add(transaksi.getTanggalKembaliAktual() != null ? transaksi.getTanggalKembaliAktual() : "-");
            
            // Tampilkan status berdasarkan tanggal kembali dan status buku
            String status;
            if (transaksi.getTanggalKembaliAktual() != null) {
                status = transaksi.getBuku().isTersedia() ? "Dikembalikan" : "Dipinjam";
            } else {
                status = "Dipinjam";
            }
            row.add(status);
            
            tableModel.addRow(row);
        }
    }

    private void filterTable(String searchTerm) {
        // Reset tabel
        tableModel.setRowCount(0);
        List<Peminjaman> daftarTransaksi = perpustakaan.getSemuaTransaksiPeminjaman();
        
        // Filter transaksi berdasarkan pencarian
        for (Peminjaman transaksi : daftarTransaksi) {
            if (transaksi.getAnggota().getIdAnggota().toLowerCase().contains(searchTerm.toLowerCase()) ||
                transaksi.getAnggota().getNama().toLowerCase().contains(searchTerm.toLowerCase()) ||
                transaksi.getBuku().getIsbn().toLowerCase().contains(searchTerm.toLowerCase()) ||
                transaksi.getBuku().getJudul().toLowerCase().contains(searchTerm.toLowerCase()) ||
                (transaksi.getTanggalKembaliAktual() != null ? "Dikembalikan" : "Dipinjam").toLowerCase().contains(searchTerm.toLowerCase())) {
                Vector<Object> row = new Vector<>();
                row.add(transaksi.getAnggota().getIdAnggota());
                row.add(transaksi.getAnggota().getNama());
                row.add(transaksi.getBuku().getIsbn());
                row.add(transaksi.getBuku().getJudul());
                row.add(transaksi.getTanggalPinjam());
                row.add(transaksi.getTanggalKembaliAktual() != null ? transaksi.getTanggalKembaliAktual() : "-");
                row.add(transaksi.getTanggalKembaliAktual() != null ? "Dikembalikan" : "Dipinjam");
                tableModel.addRow(row);
            }
        }
    }

    // refreshData method no longer needed as its logic is now in refreshTabelTransaksi
    // @Override
    // public void refreshData() {
    //     refreshTabelTransaksi();
    // }
}