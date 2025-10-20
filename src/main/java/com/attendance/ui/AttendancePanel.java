package com.attendance.ui;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.model.Student;
import com.attendance.model.Subject;
import com.attendance.service.AttendanceService;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel for marking and managing attendance
 */
public class AttendancePanel extends JPanel {
    private JDateChooser dateChooser;
    private JComboBox<Subject> subjectComboBox;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private AttendanceDAO attendanceDAO;
    private AttendanceService attendanceService;
    private JLabel statusLabel;

    public AttendancePanel() {
        studentDAO = new StudentDAO();
        subjectDAO = new SubjectDAO();
        attendanceDAO = new AttendanceDAO();
        attendanceService = new AttendanceService();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Mark Attendance");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Top panel with date and subject selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Select Date and Subject"));

        topPanel.add(new JLabel("Date:"));
        dateChooser = new JDateChooser();
        dateChooser.setDate(new java.util.Date());
        dateChooser.setPreferredSize(new Dimension(150, 25));
        topPanel.add(dateChooser);

        topPanel.add(new JLabel("Subject:"));
        subjectComboBox = new JComboBox<>();
        subjectComboBox.setPreferredSize(new Dimension(300, 25));
        topPanel.add(subjectComboBox);

        JButton loadButton = new JButton("Load Students");
        loadButton.addActionListener(e -> loadStudentsForAttendance());
        topPanel.add(loadButton);

        add(topPanel, BorderLayout.NORTH);

        // Center panel with table
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"Student ID", "Roll No", "Name", "Department", "Attendance"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only attendance column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return String.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        attendanceTable.setRowHeight(25);
        attendanceTable.getTableHeader().setReorderingAllowed(false);
        
        // Set up combo box for attendance column
        JComboBox<String> attendanceCombo = new JComboBox<>(new String[]{"Present", "Absent"});
        attendanceTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(attendanceCombo));
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Status label
        statusLabel = new JLabel("Select date and subject, then click 'Load Students'");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton markAllPresentButton = new JButton("Mark All Present");
        markAllPresentButton.addActionListener(e -> markAllAttendance("Present"));
        
        JButton markAllAbsentButton = new JButton("Mark All Absent");
        markAllAbsentButton.addActionListener(e -> markAllAttendance("Absent"));
        
        JButton saveButton = new JButton("Save Attendance");
        saveButton.addActionListener(e -> saveAttendance());
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearTable());
        
        bottomPanel.add(markAllPresentButton);
        bottomPanel.add(markAllAbsentButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(clearButton);
        
        add(bottomPanel, BorderLayout.SOUTH);

        // Load subjects
        refreshData();
    }

    public void refreshData() {
        loadSubjects();
    }

    private void loadSubjects() {
        subjectComboBox.removeAllItems();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }

    private void loadStudentsForAttendance() {
        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.", "No Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject.", "No Subject", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate selectedDate = dateChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Get all students
        List<Student> students = studentDAO.getAllStudents();
        
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students found in the database.", "No Students", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Check if attendance already exists for this date and subject
        var existingRecords = attendanceDAO.getAttendanceBySubjectAndDate(
                selectedSubject.getSubjectId(), selectedDate);
        
        Map<Integer, String> existingAttendance = new HashMap<>();
        for (var record : existingRecords) {
            existingAttendance.put(record.getStudentId(), record.getStatus());
        }

        // Populate table
        tableModel.setRowCount(0);
        for (Student student : students) {
            String status = existingAttendance.getOrDefault(student.getStudentId(), "Present");
            Object[] row = {
                student.getStudentId(),
                student.getRollNo(),
                student.getName(),
                student.getDepartment(),
                status
            };
            tableModel.addRow(row);
        }

        if (!existingAttendance.isEmpty()) {
            statusLabel.setText("Attendance already recorded for this date and subject. You can update it.");
        } else {
            statusLabel.setText("Mark attendance and click 'Save Attendance'");
        }
    }

    private void markAllAttendance(String status) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt(status, i, 4);
        }
    }

    private void saveAttendance() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No students loaded. Please load students first.", 
                    "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select a date.", "No Date", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject.", "No Subject", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate selectedDate = dateChooser.getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        // Collect attendance data
        Map<Integer, String> attendanceData = new HashMap<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int studentId = (Integer) tableModel.getValueAt(i, 0);
            String status = (String) tableModel.getValueAt(i, 4);
            attendanceData.put(studentId, status);
        }

        // Save using service
        boolean success = attendanceService.markAttendance(
                selectedSubject.getSubjectId(), selectedDate, attendanceData);

        if (success) {
            JOptionPane.showMessageDialog(this, 
                    "Attendance saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            statusLabel.setText("Attendance saved for " + selectedDate + " - " + selectedSubject.getSubjectName());
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Failed to save attendance.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
        statusLabel.setText("Select date and subject, then click 'Load Students'");
    }
}
