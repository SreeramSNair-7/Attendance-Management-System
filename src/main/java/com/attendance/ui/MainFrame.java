package com.attendance.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.attendance.db.DatabaseConnection;

/**
 * Main application frame with menu bar and panel switching
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel statusLabel;
    
    // Panels
    private StudentPanel studentPanel;
    private SubjectPanel subjectPanel;
    private AttendancePanel attendancePanel;
    private ReportPanel reportPanel;

    public MainFrame() {
        initializeComponents();
        setupUI();
        checkDatabaseConnection();
    }

    private void initializeComponents() {
        setTitle("Attendance Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize card layout and main panel with custom background
        cardLayout = new CardLayout();
        mainPanel = new CustomBackgroundPanel(cardLayout);
        
        // Initialize panels
        studentPanel = new StudentPanel();
        subjectPanel = new SubjectPanel();
        attendancePanel = new AttendancePanel();
        reportPanel = new ReportPanel();
    }

    private void setupUI() {
        // Create styled menu bar with gradient
        JMenuBar menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(52, 152, 219);
                Color color2 = new Color(155, 89, 182);
                java.awt.GradientPaint gradient = new java.awt.GradientPaint(
                    0, 0, color1, getWidth(), 0, color2
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menuBar.setBorderPainted(false);
        
        Font menuFont = new Font("Arial", Font.BOLD, 14);
        
        // Home menu
        JMenu homeMenu = createStyledMenu("Home", menuFont);
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> showDashboard());
        homeMenu.add(dashboardItem);
        menuBar.add(homeMenu);
        
        // Students menu
        JMenu studentsMenu = createStyledMenu("Students", menuFont);
        JMenuItem manageStudentsItem = new JMenuItem("Manage Students");
        manageStudentsItem.addActionListener(e -> showPanel("students"));
        studentsMenu.add(manageStudentsItem);
        menuBar.add(studentsMenu);
        
        // Subjects menu
        JMenu subjectsMenu = createStyledMenu("Subjects", menuFont);
        JMenuItem manageSubjectsItem = new JMenuItem("Manage Subjects");
        manageSubjectsItem.addActionListener(e -> showPanel("subjects"));
        subjectsMenu.add(manageSubjectsItem);
        menuBar.add(subjectsMenu);
        
        // Attendance menu
        JMenu attendanceMenu = createStyledMenu("Attendance", menuFont);
        JMenuItem markAttendanceItem = new JMenuItem("Mark Attendance");
        markAttendanceItem.addActionListener(e -> showPanel("attendance"));
        attendanceMenu.add(markAttendanceItem);
        menuBar.add(attendanceMenu);
        
        // Reports menu
        JMenu reportsMenu = createStyledMenu("Reports", menuFont);
        JMenuItem viewReportsItem = new JMenuItem("View Reports");
        viewReportsItem.addActionListener(e -> showPanel("reports"));
        reportsMenu.add(viewReportsItem);
        menuBar.add(reportsMenu);
        
        // Help menu
        JMenu helpMenu = createStyledMenu("Help", menuFont);
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
        
        // Add panels to card layout
        mainPanel.add(createDashboardPanel(), "dashboard");
        mainPanel.add(studentPanel, "students");
        mainPanel.add(subjectPanel, "subjects");
        mainPanel.add(attendancePanel, "attendance");
        mainPanel.add(reportPanel, "reports");
        
        // Create status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusLabel = new JLabel("Ready");
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        // Add components to frame
        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        
        // Show dashboard by default
        cardLayout.show(mainPanel, "dashboard");
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); // Transparent to show background
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Attendance Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE); // White text on gradient
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setOpaque(false); // Transparent to show background
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        // Create dashboard buttons with gradient cards
        JButton studentsBtn = createDashboardButton("Manage Students", "students", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton subjectsBtn = createDashboardButton("Manage Subjects", "subjects", new Color(155, 89, 182), new Color(142, 68, 173));
        JButton attendanceBtn = createDashboardButton("Mark Attendance", "attendance", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton reportsBtn = createDashboardButton("View Reports", "reports", new Color(231, 76, 60), new Color(192, 57, 43));
        
        centerPanel.add(studentsBtn);
        centerPanel.add(subjectsBtn);
        centerPanel.add(attendanceBtn);
        centerPanel.add(reportsBtn);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setOpaque(false); // Transparent to show background
        JLabel infoLabel = new JLabel("Welcome to the Attendance Management System");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(Color.WHITE); // White text on gradient
        infoPanel.add(infoLabel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JButton createDashboardButton(String text, String panelName, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color topColor = color1;
                Color bottomColor = color2;
                
                if (getModel().isPressed()) {
                    topColor = color2.darker();
                    bottomColor = color1.darker();
                } else if (getModel().isRollover()) {
                    topColor = color1.brighter();
                    bottomColor = color2.brighter();
                }
                
                java.awt.GradientPaint gradient = new java.awt.GradientPaint(
                    0, 0, topColor,
                    0, getHeight(), bottomColor
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Add shadow effect
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 120));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.addActionListener(e -> showPanel(panelName));
        return button;
    }
    
    private JMenu createStyledMenu(String text, Font font) {
        JMenu menu = new JMenu(text);
        menu.setFont(font);
        menu.setForeground(Color.WHITE);
        menu.setOpaque(false);
        return menu;
    }

    private void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        updateStatusBar(panelName);
        
        // Refresh panel data when shown
        switch (panelName) {
            case "students":
                studentPanel.refreshData();
                break;
            case "subjects":
                subjectPanel.refreshData();
                break;
            case "attendance":
                attendancePanel.refreshData();
                break;
            case "reports":
                reportPanel.refreshData();
                break;
        }
    }

    private void showDashboard() {
        cardLayout.show(mainPanel, "dashboard");
        statusLabel.setText("Dashboard");
    }

    private void updateStatusBar(String panelName) {
        switch (panelName) {
            case "students":
                statusLabel.setText("Managing Students");
                break;
            case "subjects":
                statusLabel.setText("Managing Subjects");
                break;
            case "attendance":
                statusLabel.setText("Marking Attendance");
                break;
            case "reports":
                statusLabel.setText("Viewing Reports");
                break;
            default:
                statusLabel.setText("Ready");
        }
    }

    private void checkDatabaseConnection() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        if (dbConnection.testConnection()) {
            statusLabel.setText("Database connected successfully");
        } else {
            statusLabel.setText("Database connection failed");
            JOptionPane.showMessageDialog(this,
                    "Failed to connect to database. Please check your configuration.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAboutDialog() {
        String message = "Attendance Management System\n" +
                         "Version 1.0.0\n\n" +
                         "A desktop application for managing student attendance.\n" +
                         "Built with Java Swing and MySQL.\n\n" +
                         "© 2025 All Rights Reserved";
        
        JOptionPane.showMessageDialog(this,
                message,
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
