package com.universitas.perpustakaan.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import com.universitas.perpustakaan.model.Buku;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Input
        JPanel panelInput = new JPanel(new BorderLayout(5, 5));
        panelInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Tambah/Cari Buku",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(60, 60, 60)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Panel untuk form input
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        formPanel.add(new JLabel("ISBN:"));
        txtIsbn = new JTextField();
        txtIsbn.setPreferredSize(new Dimension(txtIsbn.getPreferredSize().width, 30));
        formPanel.add(txtIsbn);
        
        formPanel.add(new JLabel("Judul:"));
        txtJudul = new JTextField();
        txtJudul.setPreferredSize(new Dimension(txtJudul.getPreferredSize().width, 30));
        formPanel.add(txtJudul);
        
        formPanel.add(new JLabel("Pengarang:"));
        txtPengarang = new JTextField();
        txtPengarang.setPreferredSize(new Dimension(txtPengarang.getPreferredSize().width, 30));
        formPanel.add(txtPengarang);
        

        // Panel untuk button
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTambah = new JButton("Tambah Buku");
        btnDelete = new JButton("Delete Buku");
        
        // Styling tombol tambah
        btnTambah.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTambah.setBackground(new Color(52, 152, 219));
        btnTambah.setForeground(Color.WHITE);
        btnTambah.setFocusPainted(false);
        btnTambah.setBorderPainted(true);
        btnTambah.setOpaque(true);
        btnTambah.setContentAreaFilled(true);
        btnTambah.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTambah.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Hover effect untuk tombol tambah
        btnTambah.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnTambah.setBackground(new Color(41, 128, 185));
            }

            public void mouseExited(MouseEvent evt) {
                btnTambah.setBackground(new Color(52, 152, 219));
            }
            
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    btnTambah.setBackground(new Color(41, 128, 185));
                    btnTambah.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                }
            }
            
            public void mouseReleased(MouseEvent evt) {
                btnTambah.setBackground(new Color(52, 152, 219));
                btnTambah.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });
        
        // Styling tombol delete
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(true);
        btnDelete.setOpaque(true);
        btnDelete.setContentAreaFilled(true);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        
        // Hover effect untuk tombol delete
        btnDelete.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnDelete.setBackground(new Color(192, 57, 43));
            }

            public void mouseExited(MouseEvent evt) {
                btnDelete.setBackground(new Color(231, 76, 60));
            }
            
            public void mousePressed(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    btnDelete.setBackground(new Color(192, 57, 43));
                    btnDelete.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLoweredBevelBorder(),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                    ));
                }
            }
            
            public void mouseReleased(MouseEvent evt) {
                btnDelete.setBackground(new Color(231, 76, 60));
                btnDelete.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

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
                return false;
            }
        };
        tabelBuku = new JTable(tableModel);
        
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
        
        JScrollPane scrollPaneTabel = new JScrollPane(tabelBuku);
        scrollPaneTabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Daftar Buku",
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
        
        add(tablePanel, BorderLayout.CENTER);

        // Action Listeners
        btnTambah.addActionListener(e -> tambahBukuAction());
        btnDelete.addActionListener(e -> deleteBukuAction());

        // Add action listeners for Enter key
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        actionMap.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtJudul.isFocusOwner() || txtPengarang.isFocusOwner() || txtIsbn.isFocusOwner() || btnTambah.isFocusOwner()) {
                    btnTambah.doClick();
                } else if (tabelBuku.isFocusOwner() || btnDelete.isFocusOwner()) {
                    btnDelete.doClick();
                }
            }
        });

        // Initial load
        refreshTabelBuku();

        // Styling untuk tabel
        tabelBuku.setShowGrid(true);
        tabelBuku.setGridColor(new Color(220, 220, 220));
        tabelBuku.setRowHeight(30);
        tabelBuku.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Header styling
        tabelBuku.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelBuku.getTableHeader().setBackground(new Color(240, 240, 240));
        tabelBuku.getTableHeader().setForeground(new Color(60, 60, 60));
        tabelBuku.getTableHeader().setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Selection styling
        tabelBuku.setSelectionBackground(new Color(230, 230, 230));
        tabelBuku.setSelectionForeground(new Color(0, 0, 0));
        
        // Table properties
        tabelBuku.setFillsViewportHeight(true);
        tabelBuku.setRowSelectionAllowed(true);
        tabelBuku.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelBuku.setIntercellSpacing(new Dimension(0, 0));
        tabelBuku.setRowMargin(0);
        
        // Cell renderer untuk styling sel
        tabelBuku.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

    private void tambahBukuAction() {
        String isbn = txtIsbn.getText();
        String judul = txtJudul.getText();
        String pengarang = txtPengarang.getText();

        if (isbn.isEmpty() || judul.isEmpty() || pengarang.isEmpty()) {
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
        txtIsbn.setText("");
        txtJudul.setText("");
        txtPengarang.setText("");
    }

    public void refreshTabelBuku() {
        tableModel.setRowCount(0);
        List<Buku> daftarBuku = perpustakaan.getSemuaBuku();
        for (Buku buku : daftarBuku) {
            Vector<Object> row = new Vector<>();
            row.add(buku.getIsbn());
            row.add(buku.getJudul());
            row.add(buku.getPengarang());
            row.add(buku.isTersedia() ? "Tersedia" : "Dipinjam");
            tableModel.addRow(row);
        }
    }

    private void deleteBukuAction() {
        int selectedRow = tabelBuku.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Buku yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String isbn = (String) tableModel.getValueAt(selectedRow, 0);
        String judul = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus buku " + judul + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (perpustakaan.hapusBuku(isbn)) {
                JOptionPane.showMessageDialog(this, "Buku berhasil dihapus!");
                refreshTabelBuku();
            }
        }
    }

    private void filterTable(String searchTerm) {
        // Reset tabel
        tableModel.setRowCount(0);
        List<Buku> daftarBuku = perpustakaan.getSemuaBuku();
        
        // Filter buku berdasarkan pencarian
        for (Buku buku : daftarBuku) {
            if (buku.getIsbn().toLowerCase().contains(searchTerm.toLowerCase()) ||
                buku.getJudul().toLowerCase().contains(searchTerm.toLowerCase()) ||
                buku.getPengarang().toLowerCase().contains(searchTerm.toLowerCase()) ||
                (buku.isTersedia() ? "Tersedia" : "Dipinjam").toLowerCase().contains(searchTerm.toLowerCase())) {
                Vector<Object> row = new Vector<>();
                row.add(buku.getIsbn());
                row.add(buku.getJudul());
                row.add(buku.getPengarang());
                row.add(buku.isTersedia() ? "Tersedia" : "Dipinjam");
                tableModel.addRow(row);
            }
        }
    }
}