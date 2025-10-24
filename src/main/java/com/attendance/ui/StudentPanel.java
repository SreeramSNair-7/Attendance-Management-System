package com.attendance.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
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

import com.attendance.dao.StudentDAO;
import com.attendance.model.Student;
import com.attendance.service.AttendanceService;

/**
 * Panel for managing students with CRUD operations
 */
public class StudentPanel extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private AttendanceService attendanceService;
    private JTextField searchField;
    private JComboBox<String> classFilterCombo;
    private JComboBox<String> deptFilterCombo;

    // Predefined departments
    private static final String[] DEPARTMENTS = new String[]{
            "All",
            "Computer Science",
            "Electronics & Communication",
            "Mechanical Engineering"
    };

    public StudentPanel() {
        studentDAO = new StudentDAO();
        attendanceService = new AttendanceService();
        initializeComponents();
    }

    private void initializeComponents() {
    setLayout(new BorderLayout(10, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    Color primaryBlue = new Color(51, 122, 183);
    Color lightBlue = new Color(230, 242, 255);
    setBackground(lightBlue);

        // Title panel
    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    titlePanel.setBackground(primaryBlue);
        JLabel titleLabel = new JLabel("Manage Students");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Create table
        String[] columnNames = {"ID", "Name", "Roll No", "Department", "Semester", "Class"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.getViewport().setBackground(lightBlue);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with search and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(lightBlue);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(lightBlue);
        
        // Class filter
        searchPanel.add(new JLabel("Class:"));
        classFilterCombo = new JComboBox<>(new String[]{"All", "A", "B", "C"});
        classFilterCombo.addActionListener(e -> applyFilters());
        searchPanel.add(classFilterCombo);
        
        searchPanel.add(Box.createHorizontalStrut(15));
        searchPanel.add(new JLabel("Department:"));
        deptFilterCombo = new JComboBox<>(DEPARTMENTS);
        deptFilterCombo.addActionListener(e -> applyFilters());
        searchPanel.add(deptFilterCombo);

        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchStudents());
        searchPanel.add(searchButton);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            classFilterCombo.setSelectedIndex(0);
            deptFilterCombo.setSelectedIndex(0);
            refreshData();
        });
        searchPanel.add(clearButton);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(lightBlue);
        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> showAddDialog());
        JButton editButton = new JButton("Edit Student");
        editButton.addActionListener(e -> showEditDialog());
        JButton deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(e -> deleteStudent());
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
        List<Student> students = studentDAO.getAllStudents();
        updateTable(students);
    }

    private void applyFilters() {
        String selectedClass = (String) classFilterCombo.getSelectedItem();
        String selectedDept = (String) deptFilterCombo.getSelectedItem();

        List<Student> students;
        boolean allClass = selectedClass == null || selectedClass.equals("All");
        boolean allDept = selectedDept == null || selectedDept.equals("All");

        if (allClass && allDept) {
            students = studentDAO.getAllStudents();
        } else if (!allClass && allDept) {
            students = studentDAO.getStudentsByClass(selectedClass);
        } else if (allClass && !allDept) {
            students = studentDAO.getStudentsByDepartmentOrdered(selectedDept);
        } else {
            students = studentDAO.getStudentsByClassAndDepartment(selectedClass, selectedDept);
        }
        updateTable(students);
    }

    private void searchStudents() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            applyFilters();
            return;
        }

        // Apply search then filter in-memory for current selections
        List<Student> searched = studentDAO.searchStudents(keyword);
        String selectedClass = (String) classFilterCombo.getSelectedItem();
        String selectedDept = (String) deptFilterCombo.getSelectedItem();
        boolean allClass = selectedClass == null || selectedClass.equals("All");
        boolean allDept = selectedDept == null || selectedDept.equals("All");

        java.util.ArrayList<Student> filtered = new java.util.ArrayList<>();
        for (Student s : searched) {
            boolean classOk = allClass || selectedClass.equals(s.getClassName());
            boolean deptOk = allDept || selectedDept.equals(s.getDepartment());
            if (classOk && deptOk) filtered.add(s);
        }
        updateTable(filtered);
    }

    private void updateTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getRollNo(),
                student.getDepartment(),
                student.getSemester(),
                student.getClassName()
            };
            tableModel.addRow(row);
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Student", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField();
        JTextField rollNoField = new JTextField();
    JComboBox<String> departmentCombo = new JComboBox<>(new String[]{
        "Computer Science",
        "Electronics & Communication",
        "Mechanical Engineering"
    });
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        JComboBox<String> classCombo = new JComboBox<>(new String[]{"A", "B", "C"});

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Roll No:"));
        formPanel.add(rollNoField);
    formPanel.add(new JLabel("Department:"));
    formPanel.add(departmentCombo);
        formPanel.add(new JLabel("Semester:"));
        formPanel.add(semesterSpinner);
        formPanel.add(new JLabel("Class:"));
        formPanel.add(classCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String rollNo = rollNoField.getText().trim();
            String department = (String) departmentCombo.getSelectedItem();
            int semester = (Integer) semesterSpinner.getValue();
            String className = (String) classCombo.getSelectedItem();

            Student student = new Student(name, rollNo, department, semester, className);
            String validationError = attendanceService.validateStudent(student, false);
            
            if (validationError != null) {
                JOptionPane.showMessageDialog(dialog, validationError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (studentDAO.addStudent(student)) {
                JOptionPane.showMessageDialog(dialog, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add student.", "Error", JOptionPane.ERROR_MESSAGE);
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
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Student student = studentDAO.getStudentById(studentId);
        
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Student", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(student.getName());
        JTextField rollNoField = new JTextField(student.getRollNo());
    JComboBox<String> departmentCombo = new JComboBox<>(new String[]{
        "Computer Science",
        "Electronics & Communication",
        "Mechanical Engineering"
    });
    departmentCombo.setSelectedItem(student.getDepartment());
        JSpinner semesterSpinner = new JSpinner(new SpinnerNumberModel(student.getSemester(), 1, 8, 1));
        JComboBox<String> classCombo = new JComboBox<>(new String[]{"A", "B", "C"});
        classCombo.setSelectedItem(student.getClassName());

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Roll No:"));
        formPanel.add(rollNoField);
    formPanel.add(new JLabel("Department:"));
    formPanel.add(departmentCombo);
        formPanel.add(new JLabel("Semester:"));
        formPanel.add(semesterSpinner);
        formPanel.add(new JLabel("Class:"));
        formPanel.add(classCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            student.setName(nameField.getText().trim());
            student.setRollNo(rollNoField.getText().trim());
            student.setDepartment((String) departmentCombo.getSelectedItem());
            student.setSemester((Integer) semesterSpinner.getValue());
            student.setClassName((String) classCombo.getSelectedItem());

            String validationError = attendanceService.validateStudent(student, true);
            
            if (validationError != null) {
                JOptionPane.showMessageDialog(dialog, validationError, "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (studentDAO.updateStudent(student)) {
                JOptionPane.showMessageDialog(dialog, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete student '" + studentName + "'?\nThis will also delete all attendance records for this student.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentDAO.deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
