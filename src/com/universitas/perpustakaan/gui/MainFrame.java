package com.universitas.perpustakaan.gui;

import javax.swing.*;

import com.universitas.perpustakaan.gui.PanelBuku;
import com.universitas.perpustakaan.gui.PanelMahasiswa;
import com.universitas.perpustakaan.gui.PanelRiwayatTransaksi;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;

/**
 * 10. GUI: Jendela utama aplikasi.
 */
public class MainFrame extends JFrame {
    private Perpustakaan perpustakaan; // Service untuk logika bisnis

    // Panel-panel
    private PanelBuku panelBuku;
    private PanelMahasiswa panelAnggota;
    private PanelRiwayatTransaksi panelRiwayatTransaksi;
    private JTextArea displayArea; // Untuk menampilkan informasi umum atau log sederhana

    public MainFrame(Perpustakaan perpustakaan) {
        this.perpustakaan = perpustakaan;

        setTitle("Sistem Manajemen Perpustakaan");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Tengah layar

        initComponents();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        panelBuku = new PanelBuku(perpustakaan);
        panelAnggota = new PanelMahasiswa(perpustakaan);
        panelRiwayatTransaksi = new PanelRiwayatTransaksi(perpustakaan);

        // Display area sederhana untuk log atau output umum
        displayArea = new JTextArea(10, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPaneDisplay = new JScrollPane(displayArea);

        // Tambahkan panel ke tab
        tabbedPane.addTab("Manajemen Buku", panelBuku);
        tabbedPane.addTab("Manajemen Mahasiswa", panelAnggota);
        tabbedPane.addTab("Riwayat Transaksi", panelRiwayatTransaksi);
        tabbedPane.addTab("Log Sistem", scrollPaneDisplay); // Tab untuk displayArea

        // Tambahkan menu sederhana (opsional, bisa dikembangkan)
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Keluar");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        add(tabbedPane, BorderLayout.CENTER);

        // Contoh cara menggunakan displayArea dari panel lain (melalui perpustakaan atau callback)
        // Misalnya, setelah operasi di PanelBuku, panggil metode di MainFrame untuk update displayArea.
        // Untuk kesederhanaan, output ke konsol juga masih digunakan di kelas service.
    }

    // Metode untuk update display area jika diperlukan dari panel lain
    public void appendToDisplayArea(String message) {
        displayArea.append(message + "\n");
    }
}