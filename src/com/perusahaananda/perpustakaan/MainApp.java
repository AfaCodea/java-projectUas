package com.perusahaananda.perpustakaan;

import com.perusahaananda.perpustakaan.gui.MainFrame;
import com.perusahaananda.perpustakaan.service.Perpustakaan;
import com.perusahaananda.perpustakaan.util.Logger;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        // Buat instance service perpustakaan (logika bisnis)
        // 1. Object: perpustakaanService adalah objek dari kelas Perpustakaan
        Perpustakaan perpustakaanService = new Perpustakaan();

        // Jalankan GUI di Event Dispatch Thread (EDT) Swing
        SwingUtilities.invokeLater(() -> {
            // 1. Object: mainFrame adalah objek dari kelas MainFrame
            MainFrame mainFrame = new MainFrame(perpustakaanService);
            mainFrame.setVisible(true);
            
            // Add shutdown hook to close logger
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Logger.close();
            }));
        });
    }
}