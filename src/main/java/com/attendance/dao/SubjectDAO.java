package com.attendance.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.attendance.db.DatabaseConnection;
import com.attendance.model.Subject;

/**
 * Data Access Object for Subject operations
 */
public class SubjectDAO {
    private final DatabaseConnection dbConnection;

    public SubjectDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Add a new subject to the database
     */
    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects (subject_code, subject_name, semester) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, subject.getSubjectCode());
            pstmt.setString(2, subject.getSubjectName());
            pstmt.setInt(3, subject.getSemester());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        subject.setSubjectId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error adding subject: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing subject
     */
    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE subjects SET subject_code = ?, subject_name = ?, semester = ? WHERE subject_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subject.getSubjectCode());
            pstmt.setString(2, subject.getSubjectName());
            pstmt.setInt(3, subject.getSemester());
            pstmt.setInt(4, subject.getSubjectId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating subject: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a subject by ID
     */
    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subject: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get a subject by ID
     */
    public Subject getSubjectById(int subjectId) {
        String sql = "SELECT * FROM subjects WHERE subject_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractSubjectFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting subject by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a subject by code
     */
    public Subject getSubjectByCode(String subjectCode) {
        String sql = "SELECT * FROM subjects WHERE subject_code = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subjectCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractSubjectFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting subject by code: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all subjects
     */
    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects ORDER BY semester ASC, subject_id ASC";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                subjects.add(extractSubjectFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all subjects: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Search subjects by code or name
     */
    public List<Subject> searchSubjects(String keyword) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE subject_code LIKE ? OR subject_name LIKE ? ORDER BY semester ASC, subject_id ASC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    subjects.add(extractSubjectFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching subjects: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Get subjects by semester
     */
    public List<Subject> getSubjectsBySemester(int semester) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE semester = ? ORDER BY subject_id ASC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, semester);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    subjects.add(extractSubjectFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting subjects by semester: " + e.getMessage());
            e.printStackTrace();
        }
        return subjects;
    }

    /**
     * Check if subject code already exists
     */
    public boolean isSubjectCodeExists(String subjectCode, int excludeSubjectId) {
        String sql = "SELECT COUNT(*) FROM subjects WHERE subject_code = ? AND subject_id != ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, subjectCode);
            pstmt.setInt(2, excludeSubjectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking subject code: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Extract Subject object from ResultSet
     */
    private Subject extractSubjectFromResultSet(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setSubjectId(rs.getInt("subject_id"));
        subject.setSubjectCode(rs.getString("subject_code"));
        subject.setSubjectName(rs.getString("subject_name"));
        subject.setSemester(rs.getInt("semester"));
        
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            subject.setCreatedAt(timestamp.toLocalDateTime());
        }
        
        return subject;
    }

    /**
     * Ensure there are at least 6 subjects for each semester S1..S7.
     * It will insert default subjects with codes like S{sem}-01..S{sem}-06
     * and names like "Semester {sem} Subject {i}" if they don't already exist.
     */
    public void ensureDefaultSubjects() {
        try (Connection conn = dbConnection.getConnection()) {
            // For semesters 1 through 7
            for (int sem = 1; sem <= 7; sem++) {
                int existingCount = 0;
                try (PreparedStatement countStmt = conn.prepareStatement(
                        "SELECT COUNT(*) FROM subjects WHERE semester = ?")) {
                    countStmt.setInt(1, sem);
                    try (ResultSet rs = countStmt.executeQuery()) {
                        if (rs.next()) existingCount = rs.getInt(1);
                    }
                }

                // Try to insert up to 6 defaults; skip any that already exist by code
                for (int i = 1; i <= 6; i++) {
                    String code = String.format("S%d-%02d", sem, i);
                    if (getSubjectByCode(code) != null) continue;
                    // If there are already 6 or more for this semester, stop adding
                    if (existingCount >= 6) break;
                    String name = String.format("Semester %d Subject %d", sem, i);
                    addSubject(new Subject(code, name, sem));
                    existingCount++;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ensuring default subjects: " + e.getMessage());
        }
    }
}
