package com.attendance.dao;

import com.attendance.db.DatabaseConnection;
import com.attendance.model.AttendanceRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Attendance operations
 */
public class AttendanceDAO {
    private final DatabaseConnection dbConnection;

    public AttendanceDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Add a new attendance record
     */
    public boolean addAttendance(AttendanceRecord record) {
        String sql = "INSERT INTO attendance (student_id, subject_id, date, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, record.getStudentId());
            pstmt.setInt(2, record.getSubjectId());
            pstmt.setDate(3, Date.valueOf(record.getDate()));
            pstmt.setString(4, record.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        record.setAttendanceId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error adding attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add multiple attendance records (batch insert)
     */
    public boolean addAttendanceBatch(List<AttendanceRecord> records) {
        String sql = "INSERT INTO attendance (student_id, subject_id, date, status) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (AttendanceRecord record : records) {
                pstmt.setInt(1, record.getStudentId());
                pstmt.setInt(2, record.getSubjectId());
                pstmt.setDate(3, Date.valueOf(record.getDate()));
                pstmt.setString(4, record.getStatus());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding attendance batch: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing attendance record
     */
    public boolean updateAttendance(AttendanceRecord record) {
        String sql = "UPDATE attendance SET status = ? WHERE attendance_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getStatus());
            pstmt.setInt(2, record.getAttendanceId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update attendance by student, subject, and date
     */
    public boolean updateAttendanceByKey(int studentId, int subjectId, LocalDate date, String status) {
        String sql = "UPDATE attendance SET status = ? WHERE student_id = ? AND subject_id = ? AND date = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, subjectId);
            pstmt.setDate(4, Date.valueOf(date));
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating attendance by key: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete an attendance record
     */
    public boolean deleteAttendance(int attendanceId) {
        String sql = "DELETE FROM attendance WHERE attendance_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, attendanceId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if attendance exists for student, subject, and date
     */
    public boolean attendanceExists(int studentId, int subjectId, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM attendance WHERE student_id = ? AND subject_id = ? AND date = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setDate(3, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking attendance existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get attendance record by ID
     */
    public AttendanceRecord getAttendanceById(int attendanceId) {
        String sql = "SELECT a.*, s.name AS student_name, s.roll_no, sub.subject_name " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN subjects sub ON a.subject_id = sub.subject_id " +
                     "WHERE a.attendance_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, attendanceId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractAttendanceFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get attendance records by student ID
     */
    public List<AttendanceRecord> getAttendanceByStudent(int studentId) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, s.roll_no, sub.subject_name " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN subjects sub ON a.subject_id = sub.subject_id " +
                     "WHERE a.student_id = ? ORDER BY a.date DESC";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractAttendanceFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by student: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get attendance records by subject ID
     */
    public List<AttendanceRecord> getAttendanceBySubject(int subjectId) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, s.roll_no, sub.subject_name " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN subjects sub ON a.subject_id = sub.subject_id " +
                     "WHERE a.subject_id = ? ORDER BY a.date DESC, s.name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractAttendanceFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by subject: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get attendance records by date
     */
    public List<AttendanceRecord> getAttendanceByDate(LocalDate date) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, s.roll_no, sub.subject_name " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN subjects sub ON a.subject_id = sub.subject_id " +
                     "WHERE a.date = ? ORDER BY sub.subject_name, s.name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractAttendanceFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by date: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get attendance records by subject and date
     */
    public List<AttendanceRecord> getAttendanceBySubjectAndDate(int subjectId, LocalDate date) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, s.roll_no, sub.subject_name " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN subjects sub ON a.subject_id = sub.subject_id " +
                     "WHERE a.subject_id = ? AND a.date = ? ORDER BY s.name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            pstmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractAttendanceFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by subject and date: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get attendance records by date range
     */
    public List<AttendanceRecord> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT a.*, s.name AS student_name, s.roll_no, sub.subject_name " +
                     "FROM attendance a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN subjects sub ON a.subject_id = sub.subject_id " +
                     "WHERE a.date BETWEEN ? AND ? ORDER BY a.date DESC, sub.subject_name, s.name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(extractAttendanceFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get attendance statistics for a student in a subject
     */
    public Map<String, Integer> getAttendanceStats(int studentId, int subjectId) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) AS total, " +
                     "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS present, " +
                     "SUM(CASE WHEN status = 'Absent' THEN 1 ELSE 0 END) AS absent " +
                     "FROM attendance " +
                     "WHERE student_id = ? AND subject_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, subjectId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total", rs.getInt("total"));
                    stats.put("present", rs.getInt("present"));
                    stats.put("absent", rs.getInt("absent"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance stats: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Get overall attendance statistics for a student
     */
    public Map<String, Integer> getOverallAttendanceStats(int studentId) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) AS total, " +
                     "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS present, " +
                     "SUM(CASE WHEN status = 'Absent' THEN 1 ELSE 0 END) AS absent " +
                     "FROM attendance " +
                     "WHERE student_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("total", rs.getInt("total"));
                    stats.put("present", rs.getInt("present"));
                    stats.put("absent", rs.getInt("absent"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting overall attendance stats: " + e.getMessage());
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Delete all attendance records for a subject and date
     */
    public boolean deleteAttendanceBySubjectAndDate(int subjectId, LocalDate date) {
        String sql = "DELETE FROM attendance WHERE subject_id = ? AND date = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, subjectId);
            pstmt.setDate(2, Date.valueOf(date));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting attendance by subject and date: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extract AttendanceRecord object from ResultSet
     */
    private AttendanceRecord extractAttendanceFromResultSet(ResultSet rs) throws SQLException {
        AttendanceRecord record = new AttendanceRecord();
        record.setAttendanceId(rs.getInt("attendance_id"));
        record.setStudentId(rs.getInt("student_id"));
        record.setSubjectId(rs.getInt("subject_id"));
        
        Date date = rs.getDate("date");
        if (date != null) {
            record.setDate(date.toLocalDate());
        }
        
        record.setStatus(rs.getString("status"));
        
        // Optional fields from JOIN
        try {
            record.setStudentName(rs.getString("student_name"));
            record.setRollNo(rs.getString("roll_no"));
            record.setSubjectName(rs.getString("subject_name"));
        } catch (SQLException e) {
            // These fields might not be present in all queries
        }
        
        return record;
    }
}
