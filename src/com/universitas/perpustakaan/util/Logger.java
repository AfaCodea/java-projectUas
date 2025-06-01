package com.universitas.perpustakaan.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "perpustakaan.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static PrintWriter writer;
    private static boolean isInitialized = false;

    static {
        initializeLogger();
    }

    private static void initializeLogger() {
        try {
            writer = new PrintWriter(new FileWriter(LOG_FILE, true));
            isInitialized = true;
            log("=== Sistem Log Perpustakaan Dimulai ===");
        } catch (IOException e) {
            System.err.println("Error initializing logger: " + e.getMessage());
            isInitialized = false;
        }
    }

    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logMessage = String.format("[%s] %s", timestamp, message);
        
        // Print to console with color
        System.out.println("\u001B[32m" + logMessage + "\u001B[0m"); // Green color
        
        // Write to file
        if (writer != null) {
            writer.println(logMessage);
            writer.flush();
        } else if (!isInitialized) {
            // Try to reinitialize if not initialized
            initializeLogger();
            if (writer != null) {
                writer.println(logMessage);
                writer.flush();
            }
        }
    }

    public static void close() {
        if (writer != null) {
            log("=== Sistem Log Perpustakaan Diakhiri ===");
            writer.close();
            writer = null;
            isInitialized = false;
        }
    }
} 