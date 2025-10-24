package com.attendance.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.attendance.dao.SubjectDAO;
import com.attendance.model.Subject;
import com.attendance.service.AttendanceService;

/**
 * Panel for managing subjects with CRUD operations
 */
public class SubjectPanel extends JPanel {
    private JTable subjectTable;
    private DefaultTableModel tableModel;
    private SubjectDAO subjectDAO;
    private AttendanceService attendanceService;
    private JTextField searchField;
    private JComboBox<String> semesterFilterCombo;

    public SubjectPanel() {
        subjectDAO = new SubjectDAO();
        attendanceService = new AttendanceService();
            // Disabled auto-generation of default subjects
            // subjectDAO.ensureDefaultSubjects();
        initializeComponents();
    }

    private void initializeComponents() {
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    Color darkBlue = new Color(13, 71, 161);
    Color lightBlue = new Color(227, 242, 253);
    setBackground(lightBlue);

        // Title panel
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    titlePanel.setBackground(darkBlue);
        JLabel titleLabel = new JLabel("Manage Subjects");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Create table
        String[] columnNames = {"ID", "Subject Code", "Subject Name", "Semester"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        subjectTable = new JTable(tableModel);
        subjectTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectTable.setRowHeight(25);
        subjectTable.getTableHeader().setReorderingAllowed(false);
        
    JScrollPane scrollPane = new JScrollPane(subjectTable);
    scrollPane.getViewport().setBackground(lightBlue);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with search and buttons
    JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
    bottomPanel.setBackground(lightBlue);
        
        // Search panel
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchPanel.setBackground(lightBlue);
        
        // Semester filter
        searchPanel.add(new JLabel("Semester:"));
        semesterFilterCombo = new JComboBox<>(new String[]{"All", "1", "2", "3", "4", "5", "6", "7", "8"});
        semesterFilterCombo.addActionListener(e -> applyFilters());
        searchPanel.add(semesterFilterCombo);
        
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchSubjects());
        searchPanel.add(searchButton);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            semesterFilterCombo.setSelectedIndex(0); // Reset to "All"
            refreshData();
        });
        searchPanel.add(clearButton);
        
        // Button panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(lightBlue);
        JButton addButton = new JButton("Add Subject");
        addButton.addActionListener(e -> showAddDialog());
        JButton editButton = new JButton("Edit Subject");
        editButton.addActionListener(e -> showEditDialog());
        JButton deleteButton = new JButton("Delete Subject");
        deleteButton.addActionListener(e -> deleteSubject());
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshData());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        bottomPanel.add(searchPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);

        // Load initial data
        refreshData();
    }

    public void refreshData() {
        List<Subject> subjects = subjectDAO.getAllSubjects();
        updateTable(subjects);
    }

    private void applyFilters() {
        String keyword = searchField.getText().trim();
        String semesterFilter = (String) semesterFilterCombo.getSelectedItem();
        
        List<Subject> subjects;
        
        // Apply semester filter
        if ("All".equals(semesterFilter)) {
            if (keyword.isEmpty()) {
                subjects = subjectDAO.getAllSubjects();
            } else {
                subjects = subjectDAO.searchSubjects(keyword);
            }
        } else {
            int semester = Integer.parseInt(semesterFilter);
            subjects = subjectDAO.getSubjectsBySemester(semester);
            
            // Apply keyword search on semester-filtered results
            if (!keyword.isEmpty()) {
                subjects = subjects.stream()
                    .filter(s -> s.getSubjectCode().toLowerCase().contains(keyword.toLowerCase()) ||
                                s.getSubjectName().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
            }
        }
        
        updateTable(subjects);
    }

    private void searchSubjects() {
        applyFilters();
    }

    private void updateTable(List<Subject> subjects) {
        tableModel.setRowCount(0);
        for (Subject subject : subjects) {
            Object[] row = {
                subject.getSubjectId(),
                subject.getSubjectCode(),
                subject.getSubjectName(),
                subject.getSemester()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Subject", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));

        formPanel.add(new JLabel("Subject Code:"));
        formPanel.add(codeField);
        formPanel.add(new JLabel("Subject Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Semester:"));
        formPanel.add(semesterSpinner);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            int semester = (Integer) semesterSpinner.getValue();

            Subject subject = new Subject(code, name, semester);
            String validationError = attendanceService.validateSubject(subject, false);
            
            if (validationError != null) {
                JOptionPane.showMessageDialog(dialog, validationError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (subjectDAO.addSubject(subject)) {
                JOptionPane.showMessageDialog(dialog, "Subject added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add subject.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int subjectId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Subject subject = subjectDAO.getSubjectById(subjectId);
        
        if (subject == null) {
            JOptionPane.showMessageDialog(this, "Subject not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Subject", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField codeField = new JTextField(subject.getSubjectCode());
        JTextField nameField = new JTextField(subject.getSubjectName());
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(subject.getSemester(), 1, 8, 1));

        formPanel.add(new JLabel("Subject Code:"));
        formPanel.add(codeField);
        formPanel.add(new JLabel("Subject Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Semester:"));
        formPanel.add(semesterSpinner);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            subject.setSubjectCode(codeField.getText().trim());
            subject.setSubjectName(nameField.getText().trim());
            subject.setSemester((Integer) semesterSpinner.getValue());

            String validationError = attendanceService.validateSubject(subject, true);
            
            if (validationError != null) {
                JOptionPane.showMessageDialog(dialog, validationError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (subjectDAO.updateSubject(subject)) {
                JOptionPane.showMessageDialog(dialog, "Subject updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update subject.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a subject to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int subjectId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String subjectName = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete subject '" + subjectName + "'?\nThis will also delete all attendance records for this subject.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (subjectDAO.deleteSubject(subjectId)) {
                JOptionPane.showMessageDialog(this, "Subject deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete subject.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
