package com.attendance.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Simple login dialog for the application
 */
public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean authenticated = false;

    public LoginDialog(Frame parent) {
        super(parent, "Login - Attendance Management System", true);
        initializeComponents();
    }

    private void initializeComponents() {
        // Create main panel with animated background
        CustomBackgroundPanel mainContentPanel = new CustomBackgroundPanel(new BorderLayout(10, 10));
        setContentPane(mainContentPanel);
        
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Title panel with transparency
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        JLabel titleLabel = new JLabel("Attendance Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainContentPanel.add(titlePanel, BorderLayout.NORTH);

        // Form panel with semi-transparent card
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Create semi-transparent card for form
        JPanel cardPanel = new JPanel(new GridLayout(2, 2, 15, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameLabel.setForeground(new Color(40, 40, 40));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(250, 40));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordLabel.setForeground(new Color(40, 40, 40));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(250, 40));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        cardPanel.add(usernameLabel);
        cardPanel.add(usernameField);
        cardPanel.add(passwordLabel);
        cardPanel.add(passwordField);
        
        formPanel.add(cardPanel);
        mainContentPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setOpaque(false);
        
        JButton loginButton = createStyledButton("Login", new Color(46, 204, 113));
        JButton cancelButton = createStyledButton("Cancel", new Color(231, 76, 60));

        loginButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setPreferredSize(new Dimension(120, 40));

        loginButton.addActionListener(e -> performLogin());
        cancelButton.addActionListener(e -> {
            authenticated = false;
            dispose();
        });

        // Add Enter key listener to password field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        mainContentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set default button
        getRootPane().setDefaultButton(loginButton);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Simple authentication (In production, this should query the database)
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Default credentials for demo (should be replaced with database authentication)
        if ((username.equals("admin") && password.equals("admin123")) ||
            (username.equals("teacher1") && password.equals("teacher123")) ||
            (username.equals("teacher2") && password.equals("teacher123"))) {
            authenticated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        return button;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog dialog = new LoginDialog(null);
            dialog.setVisible(true);
            
            if (dialog.isAuthenticated()) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login cancelled or failed.");
            }
        });
    }
}
