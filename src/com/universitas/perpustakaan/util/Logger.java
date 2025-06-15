package com.universitas.perpustakaan.util;

// Import library yang diperlukan untuk penulisan file, waktu, dan format tanggal
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Kelas Logger digunakan untuk mencatat aktivitas sistem ke file log dan menampilkan ke konsol
public class Logger {
    // Nama file log
    private static final String LOG_FILE = "perpustakaan.log";
    // Format tanggal dan waktu untuk setiap log
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // Writer untuk menulis ke file log
    private static PrintWriter writer;
    // Penanda apakah logger sudah diinisialisasi
    private static boolean isInitialized = false;

    // Blok statis untuk inisialisasi logger saat kelas pertama kali dipanggil
    static {
        initializeLogger();
    }

    // Method untuk inisialisasi logger dan membuka file log
    private static void initializeLogger() {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true)); // Mode append
            isInitialized = true;
            log("=== Sistem Log Perpustakaan Dimulai ===");
        } catch (IOException e) {
            System.err.println("Error initializing logger: " + e.getMessage());
            isInitialized = false;
        }
    }

    // Method untuk mencatat pesan ke log dan menampilkan ke konsol
    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] %s", timestamp, message);
        
        // Tampilkan ke konsol dengan warna hijau
        System.out.println("\u001B[32m" + logMessage + "\u001B[0m"); // Warna hijau
        
        // Tulis ke file log
        if (writer != null) {
            writer.println(logMessage);
            writer.flush();
        } else if (!isInitialized) {
            // Jika belum terinisialisasi, coba inisialisasi ulang
            initializeLogger();
            if (writer != null) {
                writer.println(logMessage);
                writer.flush();
            }
        }
    }

    // Method untuk menutup logger dan menulis pesan akhir ke log
    public static void close() {
        if (writer != null) {
            log("=== Sistem Log Perpustakaan Diakhiri ===");
            writer.close();
            writer = null;
            isInitialized = false;
        }
    }
} 