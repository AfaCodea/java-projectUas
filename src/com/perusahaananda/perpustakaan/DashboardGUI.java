package com.perusahaananda.perpustakaan;

import com.perusahaananda.perpustakaan.gui.PanelBuku;
import com.perusahaananda.perpustakaan.gui.PanelMahasiswa;
import com.perusahaananda.perpustakaan.gui.PanelPeminjaman;
import com.perusahaananda.perpustakaan.gui.PanelPengembalian;
import com.perusahaananda.perpustakaan.gui.PanelRiwayatTransaksi;
import com.perusahaananda.perpustakaan.service.Perpustakaan;

import javax.swing.*;
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

        // Create main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Keluar");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        
        // About Menu
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutMenuItem = new JMenuItem("Tentang Aplikasi");
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
        sidebarPanel.setBackground(new Color(51, 51, 51));
        sidebarPanel.setPreferredSize(new Dimension(200, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Add logo/title
        JLabel titleLabel = new JLabel("Menu");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 15));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebarPanel.add(titleLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Create menu buttons
        String[] menuItems = {
            "Dashboard",
            "Buku",
            "User Manajemen",
            "Peminjaman",
            "Pengembalian",
            "Laporan"
        };

        for (String menuItem : menuItems) {
            JButton button = createMenuButton(menuItem);
            button.addActionListener(e -> {
                String command = menuItem.toLowerCase().replace(" ", "");
                if (command.equals("usermanajemen")) {
                    command = "mahasiswa";
                }
                cardLayout.show(contentPanel, command);
            });
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Add logout button at the bottom
        sidebarPanel.add(Box.createVerticalGlue());
        JButton logoutButton = createMenuButton("Logout");
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.addActionListener(e -> handleLogout());
        sidebarPanel.add(logoutButton);
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

        // Add icon based on menu text
        String iconPath = switch (text.toLowerCase()) {
            case "dashboard" -> "/com/perusahaananda/perpustakaan/resources/icons/home.png";
            case "buku" -> "/com/perusahaananda/perpustakaan/resources/icons/book.png";
            case "user manajemen" -> "/com/perusahaananda/perpustakaan/resources/icons/users.png";
            case "peminjaman" -> "/com/perusahaananda/perpustakaan/resources/icons/borrow.png";
            case "pengembalian" -> "/com/perusahaananda/perpustakaan/resources/icons/return.png";
            case "laporan" -> "/com/perusahaananda/perpustakaan/resources/icons/report.png";
            case "logout" -> "/com/perusahaananda/perpustakaan/resources/icons/logout.png";
            default -> null;
        };

        if (iconPath != null) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(img));
                button.setIconTextGap(10); // Spacing between icon and text
            } catch (Exception e) {
                System.out.println("Icon not found: " + iconPath);
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
            // Add pressed state effect
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(90, 90, 90)); // Darker color when pressed
            }
            public void mouseReleased(MouseEvent e) {
                // Revert to hover color if mouse is still inside, otherwise to default
                if (button.getModel().isRollover()) {
                     button.setBackground(new Color(70, 70, 70));
                } else {
                     button.setBackground(new Color(51, 51, 51));
                }
            }
        });

        // Add click action
        button.addActionListener(e -> {
            // Refresh data sesuai panel
            String panelName = text.toLowerCase();
            switch (panelName) {
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
                    break;
            }
            cardLayout.show(contentPanel, panelName);
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