package com.attendance.service;

import com.attendance.dao.AttendanceDAO;
import com.attendance.dao.StudentDAO;
import com.attendance.dao.SubjectDAO;
import com.attendance.model.AttendanceRecord;
import com.attendance.model.Student;
import com.attendance.model.Subject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service layer for Attendance business logic
 */
public class AttendanceService {
    private final AttendanceDAO attendanceDAO;
    private final StudentDAO studentDAO;
    private final SubjectDAO subjectDAO;

    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
        this.studentDAO = new StudentDAO();
        this.subjectDAO = new SubjectDAO();
    }

    /**
     * Mark attendance for multiple students
     */
    public boolean markAttendance(int subjectId, LocalDate date, Map<Integer, String> studentAttendance) {
        // Check if attendance already exists for this date and subject
        List<AttendanceRecord> existingRecords = attendanceDAO.getAttendanceBySubjectAndDate(subjectId, date);
        
        if (!existingRecords.isEmpty()) {
            // Update existing records
            for (Map.Entry<Integer, String> entry : studentAttendance.entrySet()) {
                int studentId = entry.getKey();
                String status = entry.getValue();
                attendanceDAO.updateAttendanceByKey(studentId, subjectId, date, status);
            }
            return true;
        } else {
            // Insert new records
            List<AttendanceRecord> records = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : studentAttendance.entrySet()) {
                int studentId = entry.getKey();
                String status = entry.getValue();
                AttendanceRecord record = new AttendanceRecord(studentId, subjectId, date, status);
                records.add(record);
            }
            return attendanceDAO.addAttendanceBatch(records);
        }
    }

    /**
     * Update attendance for a specific student, subject, and date
     */
    public boolean updateAttendance(int studentId, int subjectId, LocalDate date, String status) {
        if (attendanceDAO.attendanceExists(studentId, subjectId, date)) {
            return attendanceDAO.updateAttendanceByKey(studentId, subjectId, date, status);
        } else {
            AttendanceRecord record = new AttendanceRecord(studentId, subjectId, date, status);
            return attendanceDAO.addAttendance(record);
        }
    }

    /**
     * Get attendance percentage for a student in a subject
     */
    public double getAttendancePercentage(int studentId, int subjectId) {
        Map<String, Integer> stats = attendanceDAO.getAttendanceStats(studentId, subjectId);
        int total = stats.getOrDefault("total", 0);
        int present = stats.getOrDefault("present", 0);
        
        if (total == 0) {
            return 0.0;
        }
        
        return (present * 100.0) / total;
    }

    /**
     * Get overall attendance percentage for a student
     */
    public double getOverallAttendancePercentage(int studentId) {
        Map<String, Integer> stats = attendanceDAO.getOverallAttendanceStats(studentId);
        int total = stats.getOrDefault("total", 0);
        int present = stats.getOrDefault("present", 0);
        
        if (total == 0) {
            return 0.0;
        }
        
        return (present * 100.0) / total;
    }

    /**
     * Get attendance report for a student
     */
    public List<Map<String, Object>> getStudentAttendanceReport(int studentId) {
        List<Map<String, Object>> report = new ArrayList<>();
        List<Subject> subjects = subjectDAO.getAllSubjects();
        
        for (Subject subject : subjects) {
            Map<String, Integer> stats = attendanceDAO.getAttendanceStats(studentId, subject.getSubjectId());
            int total = stats.getOrDefault("total", 0);
            int present = stats.getOrDefault("present", 0);
            int absent = stats.getOrDefault("absent", 0);
            double percentage = total > 0 ? (present * 100.0) / total : 0.0;
            
            Map<String, Object> subjectReport = new java.util.HashMap<>();
            subjectReport.put("subjectId", subject.getSubjectId());
            subjectReport.put("subjectCode", subject.getSubjectCode());
            subjectReport.put("subjectName", subject.getSubjectName());
            subjectReport.put("total", total);
            subjectReport.put("present", present);
            subjectReport.put("absent", absent);
            subjectReport.put("percentage", percentage);
            
            report.add(subjectReport);
        }
        
        return report;
    }

    /**
     * Get attendance report for a subject
     */
    public List<Map<String, Object>> getSubjectAttendanceReport(int subjectId) {
        List<Map<String, Object>> report = new ArrayList<>();
        List<Student> students = studentDAO.getAllStudents();
        
        for (Student student : students) {
            Map<String, Integer> stats = attendanceDAO.getAttendanceStats(student.getStudentId(), subjectId);
            int total = stats.getOrDefault("total", 0);
            int present = stats.getOrDefault("present", 0);
            int absent = stats.getOrDefault("absent", 0);
            double percentage = total > 0 ? (present * 100.0) / total : 0.0;
            
            Map<String, Object> studentReport = new java.util.HashMap<>();
            studentReport.put("studentId", student.getStudentId());
            studentReport.put("studentName", student.getName());
            studentReport.put("rollNo", student.getRollNo());
            studentReport.put("department", student.getDepartment());
            studentReport.put("total", total);
            studentReport.put("present", present);
            studentReport.put("absent", absent);
            studentReport.put("percentage", percentage);
            
            report.add(studentReport);
        }
        
        return report;
    }

    /**
     * Get attendance records for a date range
     */
    public List<AttendanceRecord> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceDAO.getAttendanceByDateRange(startDate, endDate);
    }

    /**
     * Delete attendance for a subject and date
     */
    public boolean deleteAttendance(int subjectId, LocalDate date) {
        return attendanceDAO.deleteAttendanceBySubjectAndDate(subjectId, date);
    }

    /**
     * Validate student data
     */
    public String validateStudent(Student student, boolean isUpdate) {
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return "Student name is required";
        }
        
        if (student.getRollNo() == null || student.getRollNo().trim().isEmpty()) {
            return "Roll number is required";
        }
        
        if (student.getDepartment() == null || student.getDepartment().trim().isEmpty()) {
            return "Department is required";
        }
        
        if (student.getSemester() < 1 || student.getSemester() > 8) {
            return "Semester must be between 1 and 8";
        }
        
        // Check for duplicate roll number
        int excludeId = isUpdate ? student.getStudentId() : 0;
        if (studentDAO.isRollNoExists(student.getRollNo(), excludeId)) {
            return "Roll number already exists";
        }
        
        return null; // No validation errors
    }

    /**
     * Validate subject data
     */
    public String validateSubject(Subject subject, boolean isUpdate) {
        if (subject.getSubjectCode() == null || subject.getSubjectCode().trim().isEmpty()) {
            return "Subject code is required";
        }
        
        if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            return "Subject name is required";
        }
        
        if (subject.getSemester() < 1 || subject.getSemester() > 8) {
            return "Semester must be between 1 and 8";
        }
        
        // Check for duplicate subject code
        int excludeId = isUpdate ? subject.getSubjectId() : 0;
        if (subjectDAO.isSubjectCodeExists(subject.getSubjectCode(), excludeId)) {
            return "Subject code already exists";
        }
        
        return null; // No validation errors
    }

    /**
     * Validate attendance status
     */
    public String validateAttendanceStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "Attendance status is required";
        }
        
        if (!status.equals("Present") && !status.equals("Absent")) {
            return "Attendance status must be either 'Present' or 'Absent'";
        }
        
        return null; // No validation errors
    }
}
