-- ========================================
-- Attendance Management System Database
-- MySQL Schema with Sample Data
-- ========================================

-- Drop database if exists and create fresh
DROP DATABASE IF EXISTS attendance_db;
CREATE DATABASE attendance_db;
USE attendance_db;

-- ========================================
-- TABLE: students
-- ========================================
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(50) UNIQUE NOT NULL,
    department VARCHAR(100) NOT NULL,
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_roll_no (roll_no),
    INDEX idx_department (department),
    INDEX idx_semester (semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- TABLE: subjects
-- ========================================
CREATE TABLE subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(50) UNIQUE NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_subject_code (subject_code),
    INDEX idx_semester (semester)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- TABLE: attendance
-- ========================================
CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    date DATE NOT NULL,
    status ENUM('Present', 'Absent') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (student_id, subject_id, date),
    INDEX idx_date (date),
    INDEX idx_student_subject (student_id, subject_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- TABLE: users (for login system)
-- ========================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('Admin', 'Teacher') NOT NULL DEFAULT 'Teacher',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- SAMPLE DATA: Students
-- ========================================
INSERT INTO students (name, roll_no, department, semester) VALUES
('Rajesh Kumar', 'CS001', 'Computer Science', 5),
('Priya Sharma', 'CS002', 'Computer Science', 5),
('Amit Patel', 'CS003', 'Computer Science', 5),
('Sneha Reddy', 'CS004', 'Computer Science', 5),
('Vikram Singh', 'CS005', 'Computer Science', 5),
('Ananya Iyer', 'EC001', 'Electronics', 3),
('Rohit Verma', 'EC002', 'Electronics', 3),
('Divya Nair', 'EC003', 'Electronics', 3),
('Karthik Rao', 'ME001', 'Mechanical', 4),
('Pooja Desai', 'ME002', 'Mechanical', 4),
('Arjun Menon', 'CS006', 'Computer Science', 6),
('Kavya Krishnan', 'CS007', 'Computer Science', 6),
('Sanjay Gupta', 'EC004', 'Electronics', 5),
('Meera Joshi', 'ME003', 'Mechanical', 5),
('Aditya Bhatt', 'CS008', 'Computer Science', 7);

-- ========================================
-- SAMPLE DATA: Subjects
-- ========================================
INSERT INTO subjects (subject_code, subject_name, semester) VALUES
('CS501', 'Data Structures and Algorithms', 5),
('CS502', 'Database Management Systems', 5),
('CS503', 'Operating Systems', 5),
('CS504', 'Computer Networks', 5),
('CS601', 'Software Engineering', 6),
('CS602', 'Web Technologies', 6),
('EC301', 'Digital Electronics', 3),
('EC302', 'Signals and Systems', 3),
('EC501', 'Microprocessors', 5),
('ME401', 'Thermodynamics', 4),
('ME402', 'Fluid Mechanics', 4),
('ME501', 'Machine Design', 5),
('CS701', 'Artificial Intelligence', 7),
('CS702', 'Machine Learning', 7);

-- ========================================
-- SAMPLE DATA: Attendance Records
-- ========================================
-- Attendance for CS501 (Data Structures)
INSERT INTO attendance (student_id, subject_id, date, status) VALUES
(1, 1, '2025-10-01', 'Present'),
(2, 1, '2025-10-01', 'Present'),
(3, 1, '2025-10-01', 'Absent'),
(4, 1, '2025-10-01', 'Present'),
(5, 1, '2025-10-01', 'Present'),

(1, 1, '2025-10-03', 'Present'),
(2, 1, '2025-10-03', 'Absent'),
(3, 1, '2025-10-03', 'Present'),
(4, 1, '2025-10-03', 'Present'),
(5, 1, '2025-10-03', 'Present'),

(1, 1, '2025-10-05', 'Present'),
(2, 1, '2025-10-05', 'Present'),
(3, 1, '2025-10-05', 'Present'),
(4, 1, '2025-10-05', 'Absent'),
(5, 1, '2025-10-05', 'Present');

-- Attendance for CS502 (DBMS)
INSERT INTO attendance (student_id, subject_id, date, status) VALUES
(1, 2, '2025-10-02', 'Present'),
(2, 2, '2025-10-02', 'Present'),
(3, 2, '2025-10-02', 'Present'),
(4, 2, '2025-10-02', 'Present'),
(5, 2, '2025-10-02', 'Absent'),

(1, 2, '2025-10-04', 'Present'),
(2, 2, '2025-10-04', 'Present'),
(3, 2, '2025-10-04', 'Absent'),
(4, 2, '2025-10-04', 'Present'),
(5, 2, '2025-10-04', 'Present');

-- Attendance for EC301 (Digital Electronics)
INSERT INTO attendance (student_id, subject_id, date, status) VALUES
(6, 7, '2025-10-01', 'Present'),
(7, 7, '2025-10-01', 'Present'),
(8, 7, '2025-10-01', 'Present'),

(6, 7, '2025-10-03', 'Absent'),
(7, 7, '2025-10-03', 'Present'),
(8, 7, '2025-10-03', 'Present');

-- ========================================
-- SAMPLE DATA: Users
-- ========================================
-- Password: admin123 (plain text for demo - should be hashed in production)
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'System Administrator', 'Admin'),
('teacher1', 'teacher123', 'Dr. Ramesh Kumar', 'Teacher'),
('teacher2', 'teacher123', 'Prof. Lakshmi Rao', 'Teacher');

-- ========================================
-- USEFUL QUERIES
-- ========================================

-- Get attendance percentage for a specific student
/*
SELECT 
    s.student_id,
    s.name,
    s.roll_no,
    COUNT(*) as total_classes,
    SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) as present_count,
    SUM(CASE WHEN a.status = 'Absent' THEN 1 ELSE 0 END) as absent_count,
    ROUND((SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) as percentage
FROM students s
LEFT JOIN attendance a ON s.student_id = a.student_id
WHERE s.student_id = 1
GROUP BY s.student_id, s.name, s.roll_no;
*/

-- Get attendance report for a specific subject
/*
SELECT 
    s.name AS student_name,
    s.roll_no,
    sub.subject_name,
    COUNT(*) as total_classes,
    SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) as present_count,
    ROUND((SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)), 2) as percentage
FROM attendance a
JOIN students s ON a.student_id = s.student_id
JOIN subjects sub ON a.subject_id = sub.subject_id
WHERE a.subject_id = 1
GROUP BY s.name, s.roll_no, sub.subject_name
ORDER BY s.name;
*/

-- Get attendance for a specific date
/*
SELECT 
    s.roll_no,
    s.name,
    sub.subject_code,
    sub.subject_name,
    a.status
FROM attendance a
JOIN students s ON a.student_id = s.student_id
JOIN subjects sub ON a.subject_id = sub.subject_id
WHERE a.date = '2025-10-01'
ORDER BY sub.subject_name, s.name;
*/

-- ========================================
-- DATABASE SETUP COMPLETE
-- ========================================
