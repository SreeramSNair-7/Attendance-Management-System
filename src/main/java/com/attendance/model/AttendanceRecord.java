package com.attendance.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * AttendanceRecord model representing an attendance entry in the system
 */
public class AttendanceRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int attendanceId;
    private int studentId;
    private int subjectId;
    private LocalDate date;
    private String status; // "Present" or "Absent"
    
    // Additional fields for display purposes
    private String studentName;
    private String rollNo;
    private String subjectName;

    // Default constructor
    public AttendanceRecord() {
    }

    // Constructor without ID (for new records)
    public AttendanceRecord(int studentId, int subjectId, LocalDate date, String status) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.date = date;
        this.status = status;
    }

    // Full constructor
    public AttendanceRecord(int attendanceId, int studentId, int subjectId, LocalDate date, String status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.date = date;
        this.status = status;
    }

    // Getters and Setters
    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "attendanceId=" + attendanceId +
                ", studentId=" + studentId +
                ", subjectId=" + subjectId +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceRecord that = (AttendanceRecord) o;
        return attendanceId == that.attendanceId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(attendanceId);
    }
}
