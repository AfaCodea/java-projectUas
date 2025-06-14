package com.universitas.perpustakaan;

import javax.swing.*;

import com.universitas.perpustakaan.gui.PanelBuku;
import com.universitas.perpustakaan.gui.PanelMahasiswa;
import com.universitas.perpustakaan.gui.PanelPeminjaman;
import com.universitas.perpustakaan.gui.PanelPengembalian;
import com.universitas.perpustakaan.gui.PanelRiwayatTransaksi;
import com.universitas.perpustakaan.service.Perpustakaan;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.stream.Collectors;

public class DashboardGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Perpustakaan perpustakaan;
    private PanelBuku panelBuku;
    private PanelMahasiswa panelMahasiswa;
    private PanelPeminjaman panelPeminjaman;
    private PanelPengembalian panelPengembalian;
    private PanelRiwayatTransaksi panelRiwayatTransaksi;
    private JPanel dashboardPanel;
    private JPanel laporanPanel;
    private boolean isSidebarExpanded = true;
    private Timer sidebarTimer;
    private static final int SIDEBAR_WIDTH = 200;
    private static final int COLLAPSED_WIDTH = 60;
    private static final Color SIDEBAR_COLOR = new Color(80, 80, 80);
    private static final Color SIDEBAR_HOVER_COLOR = new Color(100, 100, 100);
    private static final Color SIDEBAR_PRESSED_COLOR = new Color(60, 60, 60);
    private static final Color SIDEBAR_ACTIVE_COLOR = new Color(0, 120, 215);
    private static final Font SIDEBAR_FONT = new Font("Arial", Font.BOLD, 15);

    public DashboardGUI() {
        // Initialize Perpustakaan service
        perpustakaan = new Perpustakaan();
        
        // Initialize panels
        panelBuku = new PanelBuku(perpustakaan);
        panelRiwayatTransaksi = new PanelRiwayatTransaksi(perpustakaan);
        panelPeminjaman = new PanelPeminjaman(perpustakaan, panelRiwayatTransaksi, panelBuku);
        panelPengembalian = new PanelPengembalian(perpustakaan, panelRiwayatTransaksi, panelBuku);
        panelMahasiswa = new PanelMahasiswa(perpustakaan);

        dashboardPanel = createDashboardPanel();
        laporanPanel = createLaporanPanel();

        // Set up the frame
        setTitle("Dashboard Perpustakaan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        // Create main panel with BorderLayout and smooth border
        mainPanel = new JPanel(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menuBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        fileMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JMenuItem exitMenuItem = new JMenuItem("Keluar");
        exitMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        
        // About Menu
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aboutMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JMenuItem aboutMenuItem = new JMenuItem("Tentang Aplikasi");
        aboutMenuItem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aboutMenuItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Sistem Perpustakaan v1.0\n\n" +
                "Aplikasi ini dibuat untuk mengelola perpustakaan,\n" +
                "termasuk manajemen buku, mahasiswa, peminjaman,\n" +
                "dan pengembalian buku.\n\n" +
                "Pengembang:\n" +
                "Agil Prasunza\n" +
                "Muhammad Raihan Firdaus\n" +
                "Jordy Marchelino Lumban Gaol",
                "Tentang Aplikasi",
                JOptionPane.INFORMATION_MESSAGE);
        });
        aboutMenu.add(aboutMenuItem);
        menuBar.add(aboutMenu);
        
        setJMenuBar(menuBar);
        
        // Create sidebar
        createSidebar();
        
        // Create content panel with CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        
        // Add panels to content panel
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(panelBuku, "buku");
        contentPanel.add(panelMahasiswa, "mahasiswa");
        contentPanel.add(panelPeminjaman, "peminjaman");
        contentPanel.add(panelPengembalian, "pengembalian");
        contentPanel.add(panelRiwayatTransaksi, "laporan");
        
        // Add panels to main panel
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Show default panel
        cardLayout.show(contentPanel, "dashboard");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create welcome message
        JLabel welcomeLabel = new JLabel("Selamat Datang di Sistem Perpustakaan", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);

        // Create statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add statistics cards
        statsPanel.add(createStatCard("Total Buku", String.valueOf(perpustakaan.getSemuaBuku().size())));
        statsPanel.add(createStatCard("Total Mahasiswa", String.valueOf(perpustakaan.getSemuaAnggota().size())));
        statsPanel.add(createStatCard("Buku Dipinjam", String.valueOf(perpustakaan.getDaftarPeminjamanAktif().size())));
        statsPanel.add(createStatCard("Buku Tersedia", String.valueOf(
            perpustakaan.getSemuaBuku().stream().filter(buku -> buku.isTersedia()).count()
        )));

        panel.add(statsPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);

        return card;
    }

    private JPanel createLaporanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Laporan Perpustakaan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // The actual report content will be in PanelRiwayatTransaksi, 
        // this panel just serves as a container/title if needed.
        // For simplicity, we will just use PanelRiwayatTransaksi directly for "laporan" card.
        // If more complex report layout is needed, this method can be expanded.
        
        // For now, returning a placeholder or could potentially embed PanelRiwayatTransaksi here
        // However, adding PanelRiwayatTransaksi directly to CardLayout is simpler.
        
        // Returning a simple panel with a message indicating where the actual report is.
         JPanel simpleReportPanel = new JPanel();
         simpleReportPanel.add(new JLabel("Silakan klik menu 'Laporan' di sidebar untuk melihat riwayat transaksi."));
         return simpleReportPanel;
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(80, 80, 80));
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Add toggle button
        JButton toggleButton = new JButton();
        try {
            URL menuUrl = getClass().getClassLoader().getResource("icons_toggle/menu.png");
            URL closeUrl = getClass().getClassLoader().getResource("icons_toggle/close.png");
            
            if (menuUrl != null && closeUrl != null) {
                ImageIcon menuIcon = new ImageIcon(menuUrl);
                ImageIcon closeIcon = new ImageIcon(closeUrl);
                Image menuImg = menuIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                Image closeImg = closeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                toggleButton.setIcon(new ImageIcon(menuImg));
                toggleButton.setPressedIcon(new ImageIcon(closeImg));
            } else {
                System.out.println("Menu icons not found");
                toggleButton.setText("☰"); // Fallback ke text jika icon tidak ditemukan
            }
        } catch (Exception e) {
            System.out.println("Error loading menu icons: " + e.getMessage());
            toggleButton.setText("☰"); // Fallback ke text jika icon tidak ditemukan
        }
        toggleButton.setFont(new Font("Arial", Font.BOLD, 15));
        toggleButton.setForeground(Color.WHITE);
        toggleButton.setBackground(new Color(51, 51, 51));
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleButton.setMaximumSize(new Dimension(40, 40));
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect for toggle button
        toggleButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                toggleButton.setBackground(new Color(70, 70, 70));
            }
            public void mouseExited(MouseEvent e) {
                toggleButton.setBackground(new Color(51, 51, 51));
            }
            public void mousePressed(MouseEvent e) {
                toggleButton.setBackground(new Color(90, 90, 90));
            }
            public void mouseReleased(MouseEvent e) {
                if (toggleButton.getModel().isRollover()) {
                    toggleButton.setBackground(new Color(70, 70, 70));
                } else {
                    toggleButton.setBackground(new Color(51, 51, 51));
                }
            }
        });

        toggleButton.addActionListener(e -> {
            toggleSidebar();
            // Mengubah icon saat sidebar dibuka/ditutup
            try {
                URL menuUrl = getClass().getClassLoader().getResource("icons_toggle/menu.png");
                URL closeUrl = getClass().getClassLoader().getResource("icons_toggle/close.png");
                
                if (menuUrl != null && closeUrl != null) {
                    ImageIcon menuIcon = new ImageIcon(menuUrl);
                    ImageIcon closeIcon = new ImageIcon(closeUrl);
                    Image menuImg = menuIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    Image closeImg = closeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    toggleButton.setIcon(isSidebarExpanded ? new ImageIcon(menuImg) : new ImageIcon(closeImg));
                } else {
                    System.out.println("Menu icons not found");
                    toggleButton.setText(isSidebarExpanded ? "☰" : "×"); // Fallback ke text jika icon tidak ditemukan
                }
            } catch (Exception ex) {
                System.out.println("Error loading menu icons: " + ex.getMessage());
                toggleButton.setText(isSidebarExpanded ? "☰" : "×"); // Fallback ke text jika icon tidak ditemukan
            }
        });
        sidebarPanel.add(toggleButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add logo/title
        JLabel titleLabel = new JLabel("Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Create menu items with their respective icons
        String[][] menuItems = {
            {"Dashboard", "icons/dashboard.png"},
            {"Buku", "icons/book.png"},
            {"User Manajemen", "icons/users.png"},
            {"Peminjaman", "icons/borrow.png"},
            {"Pengembalian", "icons/return.png"},
            {"Laporan", "icons/report.png"}
        };

        for (String[] menuItem : menuItems) {
            JPanel panel = createSidebarPanel(menuItem[0], menuItem[1]);
            panel.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.setMaximumSize(new Dimension(SIDEBAR_WIDTH - 20, 35));
            
            // Add click listener
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String command = menuItem[0].toLowerCase().replace(" ", "");
                    if (command.equals("usermanajemen")) {
                        command = "mahasiswa";
                    }
                    cardLayout.show(contentPanel, command);
                    
                    // Refresh data sesuai panel
                    switch (command) {
                        case "buku":
                            panelBuku.refreshTabelBuku();
                            break;
                        case "mahasiswa":
                            panelMahasiswa.refreshTabelMahasiswa();
                            break;
                        case "peminjaman":
                            panelPeminjaman.refreshData();
                            break;
                        case "pengembalian":
                            panelPengembalian.refreshData();
                            break;
                        case "laporan":
                            panelRiwayatTransaksi.refreshTabelTransaksi();
                            break;
                        case "dashboard":
                            contentPanel.remove(dashboardPanel);
                            dashboardPanel = createDashboardPanel();
                            contentPanel.add(dashboardPanel, "dashboard");
                            cardLayout.show(contentPanel, "dashboard");
                            break;
                    }
                }
            });
            
            sidebarPanel.add(panel);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        // Add logout button at the bottom
        sidebarPanel.add(Box.createVerticalGlue());
        JPanel logoutPanel = createSidebarPanel("Logout", "icons/logout.png");
        logoutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutPanel.setMaximumSize(new Dimension(SIDEBAR_WIDTH - 20, 35));
        logoutPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleLogout();
            }
        });
        sidebarPanel.add(logoutPanel);

        // Initialize sidebar timer
        sidebarTimer = new Timer(10, null);
        sidebarTimer.setRepeats(true);
    }

    private void toggleSidebar() {
        if (sidebarTimer.isRunning()) {
            return;
        }

        // Target width for the animation
        int targetWidth = isSidebarExpanded ? COLLAPSED_WIDTH : SIDEBAR_WIDTH;
        // Step size and direction for the animation
        int step = isSidebarExpanded ? -5 : 5;

        // Remove any existing action listeners to ensure a clean state for the timer
        for (ActionListener listener : sidebarTimer.getActionListeners()) {
            sidebarTimer.removeActionListener(listener);
        }

        sidebarTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the actual current width of the sidebar panel in each animation step
                int actualCurrentWidth = sidebarPanel.getWidth();
                int newWidth = actualCurrentWidth + step;

                // Check if the animation should stop
                // If expanding (!isSidebarExpanded), stop when newWidth >= targetWidth
                // If collapsing (isSidebarExpanded), stop when newWidth <= targetWidth
                if ((!isSidebarExpanded && newWidth >= targetWidth) || (isSidebarExpanded && newWidth <= targetWidth)) {
                    newWidth = targetWidth; // Snap to the exact target width
                    sidebarTimer.stop();
                    isSidebarExpanded = !isSidebarExpanded; // Toggle the expanded state
                    updateSidebarComponents(); // Update components based on the new state
                }

                // Set the new preferred size for the sidebar
                sidebarPanel.setPreferredSize(new Dimension(newWidth, sidebarPanel.getHeight()));
                
                // Revalidate and repaint the main panel to reflect size changes
                // Assuming mainPanel is the container of sidebarPanel and needs to adjust its layout
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        sidebarTimer.start(); // Start the animation
    }
    
    private void updateSidebarComponents() {
        Component[] components = sidebarPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals("☰") || button.getText().equals("×")) {
                    continue; // Skip toggle button
                }
                if (isSidebarExpanded) {
                    button.setText(button.getActionCommand());
                    button.setMaximumSize(new Dimension(180, 25));
                    button.setPreferredSize(new Dimension(180, 25));
                } else {
                    button.setText("");
                    button.setMaximumSize(new Dimension(30, 25));
                    button.setPreferredSize(new Dimension(30, 25));
                }
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setVisible(isSidebarExpanded);
            }
        }
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(51, 51, 51));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 25));
        button.setPreferredSize(new Dimension(180, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setActionCommand(text); // Store the original text

        // Add icon based on menu text
        String iconPath = switch (text.toLowerCase()) {
            case "dashboard" -> "icons/dashboard.png";
            case "buku" -> "icons/book.png";
            case "user manajemen" -> "icons/users.png";
            case "peminjaman" -> "icons/borrow.png";
            case "pengembalian" -> "icons/return.png";
            case "laporan" -> "icons/report.png";
            case "logout" -> "icons/logout.png";
            default -> null;
        };

        if (iconPath != null) {
            try {
                URL imageUrl = getClass().getClassLoader().getResource(iconPath);
                if (imageUrl != null) {
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                    button.setIcon(new ImageIcon(img));
                    button.setIconTextGap(10); // Spacing between icon and text
                } else {
                    System.out.println("Icon not found: " + iconPath);
                }
            } catch (Exception e) {
                System.out.println("Error loading icon: " + e.getMessage());
            }
        }

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 70));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(51, 51, 51));
            }
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(90, 90, 90));
            }
            public void mouseReleased(MouseEvent e) {
                if (button.getModel().isRollover()) {
                    button.setBackground(new Color(70, 70, 70));
                } else {
                    button.setBackground(new Color(51, 51, 51));
                }
            }
        });

        return button;
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin keluar?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginGUI().setVisible(true);
        }
    }

    private JPanel createSidebarPanel(String title, String iconPath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(SIDEBAR_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setOpaque(true);

        // Add hover effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(SIDEBAR_HOVER_COLOR);
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, SIDEBAR_ACTIVE_COLOR),
                    BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(SIDEBAR_COLOR);
                panel.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                panel.setBackground(SIDEBAR_PRESSED_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (panel.getBounds().contains(e.getPoint())) {
                    panel.setBackground(SIDEBAR_HOVER_COLOR);
                } else {
                    panel.setBackground(SIDEBAR_COLOR);
                }
            }
        });

        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));
            panel.add(iconLabel);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new DashboardGUI().setVisible(true);
        });
    }
} 