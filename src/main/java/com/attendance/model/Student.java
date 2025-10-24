package com.attendance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Student model representing a student entity in the system
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int studentId;
    private String name;
    private String rollNo;
    private String department;
    private int semester;
    private String className;
    private LocalDateTime createdAt;

    // Default constructor
    public Student() {
    }

    // Constructor without ID (for new students)
    public Student(String name, String rollNo, String department, int semester, String className) {
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.semester = semester;
        this.className = className;
    }

    // Full constructor
    public Student(int studentId, String name, String rollNo, String department, int semester, String className, LocalDateTime createdAt) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.semester = semester;
        this.className = className;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                ", className='" + className + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(studentId);
    }
}
