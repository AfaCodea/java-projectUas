package com.universitas.perpustakaan;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.DimensionUIResource;

import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    // === PERUBAHAN WARNA BUTTON ===
    // Warna abu-abu untuk tombol
    private static final Color PRIMARY_BUTTON_GREY = new Color(128, 128, 128); // Abu-abu sedang (normal)
    private static final Color SECONDARY_BUTTON_GREY = new Color(160, 160, 160); // Abu-abu lebih terang (hover)
    private static final Color PRESSED_BUTTON_GREY = new Color(105, 105, 105);   // Abu-abu lebih gelap (pressed)
    // ==============================

    // Warna UI lainnya (tetap sama)
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color INPUT_BORDER_COLOR = new Color(189, 195, 199);


    // Fonts (tetap sama)
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 18);

    public LoginGUI() {
        setTitle("Aplikasi Perpustakaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(BACKGROUND_COLOR);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(titleLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        usernameLabel.setPreferredSize(new Dimension(150, 15));
        usernameLabel.setMaximumSize(new Dimension(150, 15));

        usernameField = createStyledTextField();
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setLabelFor(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        passwordLabel.setPreferredSize(new Dimension(150, 15));
        passwordLabel.setMaximumSize(new Dimension(150, 15));

        passwordField = createStyledPasswordField();
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setLabelFor(passwordField);

        loginButton = createStyledButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(usernameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        formPanel.add(loginButton);

        centerPanel.add(logoPanel);
        centerPanel.add(formPanel);

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

        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(INPUT_FONT);
        field.setForeground(TEXT_COLOR);
        field.setBackground(Color.WHITE);
        Border outerBorder = BorderFactory.createLineBorder(INPUT_BORDER_COLOR, 1);
        Border innerPadding = BorderFactory.createEmptyBorder(10, 12, 10, 12);
        field.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerPadding));
        
        // Set preferred, minimum and maximum size
        Dimension fieldSize = new Dimension(300, 30);
        field.setPreferredSize(fieldSize);
        field.setMinimumSize(fieldSize);
        field.setMaximumSize(fieldSize);
        
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
        
        // Set preferred, minimum and maximum size
        Dimension fieldSize = new Dimension(300, 30);
        field.setPreferredSize(fieldSize);
        field.setMinimumSize(fieldSize);
        field.setMaximumSize(fieldSize);
        
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_BUTTON_GREY);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        // Mengatur layout tombol untuk menampung teks dan ikon
        button.setLayout(new FlowLayout(FlowLayout.LEADING, 13, 3));

        // Menambahkan ikon panah dari file PNG
        try {
            ImageIcon arrowIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/arrow.png"));
            // Resize ikon jika diperlukan
            Image img = arrowIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            arrowIcon = new ImageIcon(img);
            JLabel arrowLabel = new JLabel(arrowIcon);
            button.add(arrowLabel);
        } catch (Exception e) {
            System.err.println("Error loading arrow icon: " + e.getMessage());
            // Fallback ke teks panah jika gambar tidak ditemukan
            JLabel arrowLabel = new JLabel("→");
            arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            arrowLabel.setForeground(Color.WHITE);
            button.add(arrowLabel);
        }

        Dimension buttonSize = new Dimension(160, 35);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    button.setBackground(PRESSED_BUTTON_GREY);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (button.getBounds().contains(e.getPoint())) {
                        button.setBackground(SECONDARY_BUTTON_GREY);
                    } else {
                        button.setBackground(PRIMARY_BUTTON_GREY);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getModel().isPressed()) {
                    button.setBackground(SECONDARY_BUTTON_GREY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getModel().isPressed()) {
                    button.setBackground(PRIMARY_BUTTON_GREY);
                }
            }
        });

        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createRaisedBevelBorder(),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setBorderPainted(true);

        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(this,
                "Login berhasil!",
                "Message",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
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
            usernameField.requestFocusInWindow();
            usernameField.selectAll();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Failed to initialize LaF: " + e.getMessage());
            }
            new LoginGUI().setVisible(true);
        });
    }
}