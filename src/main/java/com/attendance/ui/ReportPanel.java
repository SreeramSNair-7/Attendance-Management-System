package com.attendance.ui;

import com.attendance.dao.StudentDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.model.Student;
import com.attendance.model.Subject;
import com.attendance.service.AttendanceService;
import com.attendance.util.CsvExporter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Panel for viewing attendance reports and generating statistics
 */
public class ReportPanel extends JPanel {
    private JComboBox<String> reportTypeComboBox;
    private JComboBox<Student> studentComboBox;
    private JComboBox<Subject> subjectComboBox;
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private SubjectDAO subjectDAO;
    private AttendanceService attendanceService;

    public ReportPanel() {
        studentDAO = new StudentDAO();
        subjectDAO = new SubjectDAO();
        attendanceService = new AttendanceService();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Attendance Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Top panel with filters
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createTitledBorder("Report Options"));

        // Report type panel
        JPanel reportTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reportTypePanel.add(new JLabel("Report Type:"));
        reportTypeComboBox = new JComboBox<>(new String[]{
                "Student Attendance Report",
                "Subject Attendance Report"
        });
        reportTypeComboBox.addActionListener(e -> updateFilterVisibility());
        reportTypePanel.add(reportTypeComboBox);
        topPanel.add(reportTypePanel);

        // Student selection panel
        JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentPanel.add(new JLabel("Select Student:"));
        studentComboBox = new JComboBox<>();
        studentComboBox.setPreferredSize(new Dimension(300, 25));
        studentPanel.add(studentComboBox);
        topPanel.add(studentPanel);

        // Subject selection panel
        JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectPanel.add(new JLabel("Select Subject:"));
        subjectComboBox = new JComboBox<>();
        subjectComboBox.setPreferredSize(new Dimension(300, 25));
        topPanel.add(subjectPanel);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton generateButton = new JButton("Generate Report");
        generateButton.addActionListener(e -> generateReport());
        buttonsPanel.add(generateButton);
        
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportReport());
        buttonsPanel.add(exportButton);
        
        topPanel.add(buttonsPanel);

        add(topPanel, BorderLayout.NORTH);

        // Center panel with table
        String[] columnNames = {"Subject Code", "Subject Name", "Total", "Present", "Absent", "Percentage"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(tableModel);
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.setRowHeight(25);
        reportTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with summary
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        add(bottomPanel, BorderLayout.SOUTH);

        // Load data
        refreshData();
        updateFilterVisibility();
    }

    public void refreshData() {
        loadStudents();
        loadSubjects();
    }

    private void loadStudents() {
        studentComboBox.removeAllItems();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    private void loadSubjects() {
        subjectComboBox.removeAllItems();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        for (Subject subject : subjects) {
            subjectComboBox.addItem(subject);
        }
    }

    private void updateFilterVisibility() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        studentComboBox.setEnabled("Student Attendance Report".equals(reportType));
        subjectComboBox.setEnabled("Subject Attendance Report".equals(reportType));
    }

    private void generateReport() {
        String reportType = (String) reportTypeComboBox.getSelectedItem();
        
        if ("Student Attendance Report".equals(reportType)) {
            generateStudentReport();
        } else if ("Subject Attendance Report".equals(reportType)) {
            generateSubjectReport();
        }
    }

    private void generateStudentReport() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Please select a student.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update table column headers for student report
        String[] columnNames = {"Subject Code", "Subject Name", "Total", "Present", "Absent", "Percentage"};
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.setRowCount(0);

        List<Map<String, Object>> reportData = attendanceService.getStudentAttendanceReport(selectedStudent.getStudentId());
        
        for (Map<String, Object> row : reportData) {
            Object[] tableRow = {
                row.get("subjectCode"),
                row.get("subjectName"),
                row.get("total"),
                row.get("present"),
                row.get("absent"),
                String.format("%.2f%%", row.get("percentage"))
            };
            tableModel.addRow(tableRow);
        }

        if (reportData.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "No attendance records found for this student.", 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void generateSubjectReport() {
        Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
        if (selectedSubject == null) {
            JOptionPane.showMessageDialog(this, "Please select a subject.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update table column headers for subject report
        String[] columnNames = {"Roll No", "Student Name", "Department", "Total", "Present", "Absent", "Percentage"};
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.setRowCount(0);

        List<Map<String, Object>> reportData = attendanceService.getSubjectAttendanceReport(selectedSubject.getSubjectId());
        
        for (Map<String, Object> row : reportData) {
            Object[] tableRow = {
                row.get("rollNo"),
                row.get("studentName"),
                row.get("department"),
                row.get("total"),
                row.get("present"),
                row.get("absent"),
                String.format("%.2f%%", row.get("percentage"))
            };
            tableModel.addRow(tableRow);
        }

        if (reportData.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "No attendance records found for this subject.", 
                    "No Data", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportReport() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                    "No data to export. Please generate a report first.", 
                    "No Data", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setSelectedFile(new File("attendance_report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            
            // Ensure .csv extension
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }

            String reportType = (String) reportTypeComboBox.getSelectedItem();
            boolean success = false;

            if ("Student Attendance Report".equals(reportType)) {
                Student selectedStudent = (Student) studentComboBox.getSelectedItem();
                if (selectedStudent != null) {
                    List<Map<String, Object>> reportData = attendanceService.getStudentAttendanceReport(selectedStudent.getStudentId());
                    success = CsvExporter.exportStudentReport(filePath, 
                            selectedStudent.getName(), 
                            selectedStudent.getRollNo(), 
                            reportData);
                }
            } else if ("Subject Attendance Report".equals(reportType)) {
                Subject selectedSubject = (Subject) subjectComboBox.getSelectedItem();
                if (selectedSubject != null) {
                    List<Map<String, Object>> reportData = attendanceService.getSubjectAttendanceReport(selectedSubject.getSubjectId());
                    success = CsvExporter.exportSubjectReport(filePath, 
                            selectedSubject.getSubjectCode(), 
                            selectedSubject.getSubjectName(), 
                            reportData);
                }
            }

            if (success) {
                JOptionPane.showMessageDialog(this, 
                        "Report exported successfully to:\n" + filePath, 
                        "Export Success", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Failed to export report.", 
                        "Export Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
