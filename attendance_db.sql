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
    class_name VARCHAR(50) NOT NULL DEFAULT 'A',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_roll_no (roll_no),
    INDEX idx_department (department),
    INDEX idx_semester (semester),
    INDEX idx_class_name (class_name)
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
-- SAMPLE DATA: Students (75 students - 3 classes of 25 each)
-- ========================================
-- Class A - Computer Science Semester 5
INSERT INTO students (name, roll_no, department, semester, class_name) VALUES
('Aarav Kumar', 'CS001', 'Computer Science', 5, 'A'),
('Aditya Singh', 'CS002', 'Computer Science', 5, 'A'),
('Akash Sharma', 'CS003', 'Computer Science', 5, 'A'),
('Amit Patel', 'CS004', 'Computer Science', 5, 'A'),
('Ananya Reddy', 'CS005', 'Computer Science', 5, 'A'),
('Arjun Verma', 'CS006', 'Computer Science', 5, 'A'),
('Aryan Gupta', 'CS007', 'Computer Science', 5, 'A'),
('Ayush Mehta', 'CS008', 'Computer Science', 5, 'A'),
('Deepak Rao', 'CS009', 'Computer Science', 5, 'A'),
('Dev Joshi', 'CS010', 'Computer Science', 5, 'A'),
('Divya Nair', 'CS011', 'Computer Science', 5, 'A'),
('Harsh Desai', 'CS012', 'Computer Science', 5, 'A'),
('Ishaan Kapoor', 'CS013', 'Computer Science', 5, 'A'),
('Karan Malhotra', 'CS014', 'Computer Science', 5, 'A'),
('Kavya Iyer', 'CS015', 'Computer Science', 5, 'A'),
('Krishna Menon', 'CS016', 'Computer Science', 5, 'A'),
('Meera Bhat', 'CS017', 'Computer Science', 5, 'A'),
('Naina Pillai', 'CS018', 'Computer Science', 5, 'A'),
('Pooja Shah', 'CS019', 'Computer Science', 5, 'A'),
('Priya Krishnan', 'CS020', 'Computer Science', 5, 'A'),
('Rahul Saxena', 'CS021', 'Computer Science', 5, 'A'),
('Rajesh Kumar', 'CS022', 'Computer Science', 5, 'A'),
('Riya Agarwal', 'CS023', 'Computer Science', 5, 'A'),
('Rohan Shetty', 'CS024', 'Computer Science', 5, 'A'),
('Sneha Kulkarni', 'CS025', 'Computer Science', 5, 'A');

-- Class B - Electronics & Communication Semester 5
INSERT INTO students (name, roll_no, department, semester, class_name) VALUES
('Sanjay Pandey', 'EC001', 'Electronics & Communication', 5, 'B'),
('Shweta Mishra', 'EC002', 'Electronics & Communication', 5, 'B'),
('Tanvi Jain', 'EC003', 'Electronics & Communication', 5, 'B'),
('Varun Choudhary', 'EC004', 'Electronics & Communication', 5, 'B'),
('Vikram Singh', 'EC005', 'Electronics & Communication', 5, 'B'),
('Vivek Bansal', 'EC006', 'Electronics & Communication', 5, 'B'),
('Yash Tripathi', 'EC007', 'Electronics & Communication', 5, 'B'),
('Abhishek Tiwari', 'EC008', 'Electronics & Communication', 5, 'B'),
('Anjali Dubey', 'EC009', 'Electronics & Communication', 5, 'B'),
('Bhavya Yadav', 'EC010', 'Electronics & Communication', 5, 'B'),
('Chetan Goel', 'EC011', 'Electronics & Communication', 5, 'B'),
('Dhruv Khanna', 'EC012', 'Electronics & Communication', 5, 'B'),
('Gaurav Sinha', 'EC013', 'Electronics & Communication', 5, 'B'),
('Hemant Bhardwaj', 'EC014', 'Electronics & Communication', 5, 'B'),
('Jatin Kohli', 'EC015', 'Electronics & Communication', 5, 'B'),
('Komal Arora', 'EC016', 'Electronics & Communication', 5, 'B'),
('Lakshmi Devi', 'EC017', 'Electronics & Communication', 5, 'B'),
('Manish Rana', 'EC018', 'Electronics & Communication', 5, 'B'),
('Neha Chauhan', 'EC019', 'Electronics & Communication', 5, 'B'),
('Nikhil Rawat', 'EC020', 'Electronics & Communication', 5, 'B'),
('Pawan Bisht', 'EC021', 'Electronics & Communication', 5, 'B'),
('Preeti Negi', 'EC022', 'Electronics & Communication', 5, 'B'),
('Rahul Thakur', 'EC023', 'Electronics & Communication', 5, 'B'),
('Ritika Saini', 'EC024', 'Electronics & Communication', 5, 'B'),
('Sahil Sharma', 'EC025', 'Electronics & Communication', 5, 'B');

-- Class C - Mechanical Engineering Semester 5
INSERT INTO students (name, roll_no, department, semester, class_name) VALUES
('Sakshi Verma', 'ME001', 'Mechanical Engineering', 5, 'C'),
('Sameer Khan', 'ME002', 'Mechanical Engineering', 5, 'C'),
('Siddharth Rao', 'ME003', 'Mechanical Engineering', 5, 'C'),
('Simran Kaur', 'ME004', 'Mechanical Engineering', 5, 'C'),
('Sunil Reddy', 'ME005', 'Mechanical Engineering', 5, 'C'),
('Swati Joshi', 'ME006', 'Mechanical Engineering', 5, 'C'),
('Tarun Gupta', 'ME007', 'Mechanical Engineering', 5, 'C'),
('Usha Patel', 'ME008', 'Mechanical Engineering', 5, 'C'),
('Vijay Kumar', 'ME009', 'Mechanical Engineering', 5, 'C'),
('Vishal Mehta', 'ME010', 'Mechanical Engineering', 5, 'C'),
('Akshay Desai', 'ME011', 'Mechanical Engineering', 5, 'C'),
('Ankita Singh', 'ME012', 'Mechanical Engineering', 5, 'C'),
('Ashok Mishra', 'ME013', 'Mechanical Engineering', 5, 'C'),
('Deepika Roy', 'ME014', 'Mechanical Engineering', 5, 'C'),
('Ekta Pandey', 'ME015', 'Mechanical Engineering', 5, 'C'),
('Ganesh Iyer', 'ME016', 'Mechanical Engineering', 5, 'C'),
('Geeta Pillai', 'ME017', 'Mechanical Engineering', 5, 'C'),
('Hari Nair', 'ME018', 'Mechanical Engineering', 5, 'C'),
('Isha Bhat', 'ME019', 'Mechanical Engineering', 5, 'C'),
('Kiran Shetty', 'ME020', 'Mechanical Engineering', 5, 'C'),
('Mahesh Kulkarni', 'ME021', 'Mechanical Engineering', 5, 'C'),
('Mohan Agarwal', 'ME022', 'Mechanical Engineering', 5, 'C'),
('Nisha Shah', 'ME023', 'Mechanical Engineering', 5, 'C'),
('Prakash Jain', 'ME024', 'Mechanical Engineering', 5, 'C'),
('Radha Krishna', 'ME025', 'Mechanical Engineering', 5, 'C');

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
