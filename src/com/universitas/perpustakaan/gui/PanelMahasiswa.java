package com.universitas.perpustakaan.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import com.universitas.perpustakaan.model.Mahasiswa;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel Input
        JPanel panelInput = new JPanel(new BorderLayout(5, 5));
        panelInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Tambah/Cari Mahasiswa",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(60, 60, 60)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Panel untuk form input
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        formPanel.add(new JLabel("ID Mahasiswa:"));
        txtId = new JTextField();
        txtId.setPreferredSize(new Dimension(txtId.getPreferredSize().width, 30));
        formPanel.add(txtId);

        formPanel.add(new JLabel("Nama:"));
        txtNama = new JTextField();
        txtNama.setPreferredSize(new Dimension(txtNama.getPreferredSize().width, 30));
        formPanel.add(txtNama);

        // Panel untuk button
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTambah = new JButton("Tambah Mahasiswa");
        btnDelete = new JButton("Delete Mahasiswa");
        
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
        String[] columnNames = {"ID Mahasiswa", "Nama"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelMahasiswa = new JTable(tableModel);
        
        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(Color.WHITE);
        
        JLabel searchLabel = new JLabel("🔍");  // Unicode search symbol
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
        
        JScrollPane scrollPaneTabel = new JScrollPane(tabelMahasiswa);
        scrollPaneTabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Daftar Mahasiswa",
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
        btnTambah.addActionListener(e -> tambahMahasiswaAction());
        btnDelete.addActionListener(e -> deleteMahasiswaAction());

        // Add action listeners for Enter key
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        actionMap.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtId.isFocusOwner() || txtNama.isFocusOwner() || btnTambah.isFocusOwner()) {
                    btnTambah.doClick();
                } else if (tabelMahasiswa.isFocusOwner() || btnDelete.isFocusOwner()) {
                    btnDelete.doClick();
                }
            }
        });

        // Initial load
        refreshTabelMahasiswa();

        // Styling untuk tabel
        tabelMahasiswa.setShowGrid(true);
        tabelMahasiswa.setGridColor(new Color(220, 220, 220));
        tabelMahasiswa.setRowHeight(30);
        tabelMahasiswa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Header styling
        tabelMahasiswa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabelMahasiswa.getTableHeader().setBackground(new Color(240, 240, 240));
        tabelMahasiswa.getTableHeader().setForeground(new Color(60, 60, 60));
        tabelMahasiswa.getTableHeader().setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Selection styling
        tabelMahasiswa.setSelectionBackground(new Color(230, 230, 230));
        tabelMahasiswa.setSelectionForeground(new Color(0, 0, 0));
        
        // Table properties
        tabelMahasiswa.setFillsViewportHeight(true);
        tabelMahasiswa.setRowSelectionAllowed(true);
        tabelMahasiswa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelMahasiswa.setIntercellSpacing(new Dimension(0, 0));
        tabelMahasiswa.setRowMargin(0);
        
        // Cell renderer untuk styling sel
        tabelMahasiswa.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

    private void tambahMahasiswaAction() {
        String id = txtId.getText();
        String nama = txtNama.getText();

        if (id.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (perpustakaan.cariMahasiswaById(id) != null) {
             JOptionPane.showMessageDialog(this, "Mahasiswa dengan ID tersebut sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Mahasiswa mahasiswaBaru = new Mahasiswa(nama, id);
        perpustakaan.registrasiMahasiswa(mahasiswaBaru);
        JOptionPane.showMessageDialog(this, "Mahasiswa berhasil ditambahkan!");
        refreshTabelMahasiswa();
        // Kosongkan field
        txtId.setText("");
        txtNama.setText("");
    }

    public void refreshTabelMahasiswa() {
        tableModel.setRowCount(0);
        List<Mahasiswa> daftarMahasiswa = perpustakaan.getSemuaMahasiswa();
        for (Mahasiswa mahasiswa : daftarMahasiswa) {
            Vector<Object> row = new Vector<>();
            row.add(mahasiswa.getIdAnggota());
            row.add(mahasiswa.getNama());
            tableModel.addRow(row);
        }
    }

    private void deleteMahasiswaAction() {
        int selectedRow = tabelMahasiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Mahasiswa yang akan dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String nama = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus mahasiswa " + nama + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (perpustakaan.hapusMahasiswa(id)) {
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil dihapus!");
                refreshTabelMahasiswa();
            }
        }
    }

    private void filterTable(String searchTerm) {
        // Reset tabel
        tableModel.setRowCount(0);
        List<Mahasiswa> daftarMahasiswa = perpustakaan.getSemuaMahasiswa();
        
        // Filter mahasiswa berdasarkan pencarian
        for (Mahasiswa mahasiswa : daftarMahasiswa) {
            if (mahasiswa.getIdAnggota().toLowerCase().contains(searchTerm.toLowerCase()) ||
                mahasiswa.getNama().toLowerCase().contains(searchTerm.toLowerCase())) {
            Vector<Object> row = new Vector<>();
                row.add(mahasiswa.getIdAnggota());
            row.add(mahasiswa.getNama());
            tableModel.addRow(row);
            }
        }
    }
}