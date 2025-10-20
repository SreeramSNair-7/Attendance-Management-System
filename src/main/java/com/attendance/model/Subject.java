package com.attendance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Subject model representing a subject/course entity in the system
 */
public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private int semester;
    private LocalDateTime createdAt;

    // Default constructor
    public Subject() {
    }

    // Constructor without ID (for new subjects)
    public Subject(String subjectCode, String subjectName, int semester) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
    }

    // Full constructor
    public Subject(int subjectId, String subjectCode, String subjectName, int semester, LocalDateTime createdAt) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return subjectCode + " - " + subjectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return subjectId == subject.subjectId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(subjectId);
    }
}
