package com.attendance;

import com.attendance.ui.LoginDialog;
import com.attendance.ui.MainFrame;

import javax.swing.*;

/**
 * Main entry point for the Attendance Management System
 */
public class Main {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Show login dialog
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);

            // If login successful, show main frame
            if (loginDialog.isAuthenticated()) {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } else {
                // Exit if login cancelled or failed
                System.exit(0);
            }
        });
    }
}
