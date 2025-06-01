package com.universitas.perpustakaan;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // Warna modern
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    // private static final Color ACCENT_COLOR = new Color(231, 76, 60); // Tidak terpakai saat ini, bisa untuk error atau highlight lain
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color INPUT_BORDER_COLOR = new Color(189, 195, 199);
    private static final Color BUTTON_PRESSED_COLOR = PRIMARY_COLOR.darker(); // Warna tombol saat ditekan

    // Fonts
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 18);

    public LoginGUI() {
        setTitle("Aplikasi Perpustakaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Padding keseluruhan

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Logo/Title Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(BACKGROUND_COLOR);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Penyesuaian padding bawah

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // JLabel subtitleLabel = new JLabel("Universitas");
        // subtitleLabel.setFont(SUBTITLE_FONT);
        // subtitleLabel.setForeground(TEXT_COLOR);
        // subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(titleLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Penyesuaian spasi
        // logoPanel.add(subtitleLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // formPanel itu sendiri tetap di tengah parent-nya
        // Padding atas untuk formPanel, padding horizontal 0 karena komponen di dalamnya akan mengatur lebar efektif
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT); // Pastikan LABEL_FONT sudah didefinisikan
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT); // <<< PERUBAHAN KUNCI: Label rata kiri
        usernameLabel.setPreferredSize(new Dimension(150, 15));
        usernameLabel.setMaximumSize(new Dimension(150, 15));

        usernameField = createStyledTextField(); // Field dibuat dengan style
        // Field input dan tombol memiliki setMaximumSize(new Dimension(300, ...))
        // dan setAlignmentX(Component.CENTER_ALIGNMENT).
        // Ini membuat blok konten (formPanel) memiliki lebar efektif sekitar 300px.
        // Label yang rata kiri akan berada di kiri blok 300px ini.
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT); // Field tetap di tengah (efektif mengisi lebar 300px)
        usernameLabel.setLabelFor(usernameField); // Untuk aksesibilitas

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT); // Pastikan LABEL_FONT sudah didefinisikan
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.RIGHT_ALIGNMENT); // <<< PERUBAHAN KUNCI: Label rata kiri
        passwordLabel.setPreferredSize(new Dimension(150, 15));
        passwordLabel.setMaximumSize(new Dimension(150, 15));


        passwordField = createStyledPasswordField(); // Field dibuat dengan style
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT); // Field tetap di tengah
        passwordLabel.setLabelFor(passwordField); // Untuk aksesibilitas

        // Login Button
        loginButton = createStyledButton("Login"); // Tombol dibuat dengan style
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Tombol tetap di tengah

        // Menambahkan komponen ke formPanel dengan spasi yang sesuai
        formPanel.add(usernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8))); // Spasi antara label dan field
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spasi setelah field username
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8))); // Spasi antara label dan field
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spasi setelah field password
        formPanel.add(loginButton);

        // Add panels to center panel
        centerPanel.add(logoPanel);
        centerPanel.add(formPanel);

        // Add center panel to main panel (untuk centering vertikal)
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(centerPanel);
        mainPanel.add(Box.createVerticalGlue());

        add(mainPanel);

        loginButton.addActionListener(e -> handleLogin());

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // FOKUS AWAL: Set fokus ke usernameField setelah frame visible
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20); // Kolom ini mempengaruhi preferred size awal
        field.setFont(INPUT_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBackground(Color.WHITE);
        // Padding internal dan border
        Border outerBorder = BorderFactory.createLineBorder(INPUT_BORDER_COLOR, 1);
        Border innerPadding = BorderFactory.createEmptyBorder(10, 12, 10, 12); // Padding atas-bawah ditambah
        field.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerPadding));
        // Ukuran maksimum agar konsisten dan bisa di-center
        field.setMaximumSize(new Dimension(300, field.getPreferredSize().height));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(INPUT_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBackground(Color.WHITE);
        Border outerBorder = BorderFactory.createLineBorder(INPUT_BORDER_COLOR, 1);
        Border innerPadding = BorderFactory.createEmptyBorder(10, 12, 10, 12);
        field.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerPadding));
        field.setMaximumSize(new Dimension(300, field.getPreferredSize().height));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false); // Untuk tampilan flat
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true); // Pastikan background terlihat di semua LookAndFeel

        // Mengatur ukuran tombol secara eksplisit, termasuk tinggi
        Dimension buttonSize = new Dimension(300, 45);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);


        // PENYEMPURNAAN EFEK TOMBOL: Menggunakan ChangeListener
        button.getModel().addChangeListener(e -> {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isPressed()) {
                button.setBackground(BUTTON_PRESSED_COLOR);
            } else if (model.isRollover()) {
                button.setBackground(SECONDARY_COLOR);
            } else {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Simulasi login
        if (username.equals("admin") && password.equals("admin")) {
            JOptionPane.showMessageDialog(this,
                "Login berhasil!",
                "Login",
                JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Tutup jendela login
            // Buka DashboardGUI
            SwingUtilities.invokeLater(() -> {
                DashboardGUI dashboard = new DashboardGUI();
                dashboard.setVisible(true);
            });
        } else if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username dan password tidak boleh kosong!",
                "Input Kosong",
                JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Username atau password salah!",
                "Login Gagal",
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocusInWindow(); // Kembalikan fokus ke username
            usernameField.selectAll(); // Pilih semua teks agar mudah diganti
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Untuk tampilan yang lebih konsisten dengan tema modern (opsional, FlatLaf adalah library eksternal)
                // UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Failed to initialize LaF: " + e.getMessage());
            }
            new LoginGUI().setVisible(true);
        });
    }
}