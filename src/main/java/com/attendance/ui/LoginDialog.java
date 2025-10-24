package com.attendance.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
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
        setLayout(new BorderLayout(10, 10));
        setSize(450, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(51, 122, 183));
        JLabel titleLabel = new JLabel("Attendance Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 20, 30));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setPreferredSize(new Dimension(250, 35));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(250, 35));

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        loginButton.setPreferredSize(new Dimension(100, 30));
        cancelButton.setPreferredSize(new Dimension(100, 30));

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

        add(buttonPanel, BorderLayout.SOUTH);

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
