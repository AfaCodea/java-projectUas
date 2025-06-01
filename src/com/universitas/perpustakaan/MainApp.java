package com.universitas.perpustakaan;

import javax.swing.*;

import com.universitas.perpustakaan.service.Perpustakaan;
import com.universitas.perpustakaan.util.Logger;

public class MainApp {
    public static void main(String[] args) {
        // Buat instance service perpustakaan (logika bisnis)
        // 1. Object: perpustakaanService adalah objek dari kelas Perpustakaan
        Perpustakaan perpustakaanService = new Perpustakaan();

        // Jalankan GUI di Event Dispatch Thread (EDT) Swing
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Start with LoginGUI
            LoginGUI loginGUI = new LoginGUI();
            loginGUI.setVisible(true);
            
            // Add shutdown hook to close logger
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Logger.close();
            }));
        });
    }
}