package com.attendance.ui;

import com.attendance.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;

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
        
        // Initialize card layout and main panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize panels
        studentPanel = new StudentPanel();
        subjectPanel = new SubjectPanel();
        attendancePanel = new AttendancePanel();
        reportPanel = new ReportPanel();
    }

    private void setupUI() {
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // Home menu
        JMenu homeMenu = new JMenu("Home");
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> showDashboard());
        homeMenu.add(dashboardItem);
        menuBar.add(homeMenu);
        
        // Students menu
        JMenu studentsMenu = new JMenu("Students");
        JMenuItem manageStudentsItem = new JMenuItem("Manage Students");
        manageStudentsItem.addActionListener(e -> showPanel("students"));
        studentsMenu.add(manageStudentsItem);
        menuBar.add(studentsMenu);
        
        // Subjects menu
        JMenu subjectsMenu = new JMenu("Subjects");
        JMenuItem manageSubjectsItem = new JMenuItem("Manage Subjects");
        manageSubjectsItem.addActionListener(e -> showPanel("subjects"));
        subjectsMenu.add(manageSubjectsItem);
        menuBar.add(subjectsMenu);
        
        // Attendance menu
        JMenu attendanceMenu = new JMenu("Attendance");
        JMenuItem markAttendanceItem = new JMenuItem("Mark Attendance");
        markAttendanceItem.addActionListener(e -> showPanel("attendance"));
        attendanceMenu.add(markAttendanceItem);
        menuBar.add(attendanceMenu);
        
        // Reports menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem viewReportsItem = new JMenuItem("View Reports");
        viewReportsItem.addActionListener(e -> showPanel("reports"));
        reportsMenu.add(viewReportsItem);
        menuBar.add(reportsMenu);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
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
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Attendance Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        // Create dashboard buttons
        JButton studentsBtn = createDashboardButton("Manage Students", "students");
        JButton subjectsBtn = createDashboardButton("Manage Subjects", "subjects");
        JButton attendanceBtn = createDashboardButton("Mark Attendance", "attendance");
        JButton reportsBtn = createDashboardButton("View Reports", "reports");
        
        centerPanel.add(studentsBtn);
        centerPanel.add(subjectsBtn);
        centerPanel.add(attendanceBtn);
        centerPanel.add(reportsBtn);
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel infoLabel = new JLabel("Welcome to the Attendance Management System");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(infoLabel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JButton createDashboardButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(200, 100));
        button.setFocusPainted(false);
        button.addActionListener(e -> showPanel(panelName));
        return button;
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
