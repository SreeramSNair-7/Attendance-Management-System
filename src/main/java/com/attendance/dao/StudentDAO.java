package com.attendance.dao;

import com.attendance.db.DatabaseConnection;
import com.attendance.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations
 */
public class StudentDAO {
    private final DatabaseConnection dbConnection;

    public StudentDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Add a new student to the database
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (name, roll_no, department, semester) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNo());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getSemester());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setStudentId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing student
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, roll_no = ?, department = ?, semester = ? WHERE student_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNo());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getSemester());
            pstmt.setInt(5, student.getStudentId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a student by ID
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get a student by ID
     */
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStudentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting student by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a student by roll number
     */
    public Student getStudentByRollNo(String rollNo) {
        String sql = "SELECT * FROM students WHERE roll_no = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rollNo);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractStudentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting student by roll number: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Search students by name or roll number
     */
    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name LIKE ? OR roll_no LIKE ? ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Get students by department
     */
    public List<Student> getStudentsByDepartment(String department) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE department = ? ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, department);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting students by department: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Get students by semester
     */
    public List<Student> getStudentsBySemester(int semester) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE semester = ? ORDER BY name";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, semester);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(extractStudentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting students by semester: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Check if roll number already exists
     */
    public boolean isRollNoExists(String rollNo, int excludeStudentId) {
        String sql = "SELECT COUNT(*) FROM students WHERE roll_no = ? AND student_id != ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rollNo);
            pstmt.setInt(2, excludeStudentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking roll number: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Extract Student object from ResultSet
     */
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setName(rs.getString("name"));
        student.setRollNo(rs.getString("roll_no"));
        student.setDepartment(rs.getString("department"));
        student.setSemester(rs.getInt("semester"));
        
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            student.setCreatedAt(timestamp.toLocalDateTime());
        }
        
        return student;
    }
}
