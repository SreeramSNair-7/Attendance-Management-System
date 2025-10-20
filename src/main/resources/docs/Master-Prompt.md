# MASTER PROMPT: Attendance Management System (Java 24, Swing, MySQL, JDBC)

## GOAL
Develop a complete *Attendance Management System* desktop application using *Java 24, **Swing, **MySQL, and **JDBC*.  
The system must be *fully runnable, with a functional **GUI, **database connectivity, **data validation, and **CRUD operations* for students, subjects, and attendance records.

*Tech Stack*
- Java 24
- Java Swing for GUI (no JavaFX)
- MySQL (local database)
- JDBC (mysql-connector-j.jar)
- Development IDE: Visual Studio Code
- No Maven or Gradle — local classpath build
- Project folder must include /lib folder containing JDBC connector .jar

---

## SYSTEM OBJECTIVE
A desktop app that allows faculty to manage student attendance records easily.  
The application must allow:
1. Adding, updating, and deleting students and subjects.
2. Marking daily attendance.
3. Viewing attendance reports by student or subject.
4. Searching attendance by date or student ID.
5. Generating basic statistics (e.g., attendance percentage).
6. Exporting reports to .csv.

---

## REQUIREMENTS

### 1. PROJECT STRUCTURE
AttendanceManagement/
├── lib/
│ └── mysql-connector-j.jar
├── src/
│ ├── Main.java
│ ├── db/
│ │ └── DatabaseConnection.java
│ ├── model/
│ │ ├── Student.java
│ │ ├── Subject.java
│ │ └── AttendanceRecord.java
│ ├── dao/
│ │ ├── StudentDAO.java
│ │ ├── SubjectDAO.java
│ │ └── AttendanceDAO.java
│ ├── service/
│ │ └── AttendanceService.java
│ ├── ui/
│ │ ├── MainFrame.java
│ │ ├── StudentPanel.java
│ │ ├── SubjectPanel.java
│ │ ├── AttendancePanel.java
│ │ ├── ReportPanel.java
│ │ └── LoginDialog.java
│ └── util/
│ └── CsvExporter.java
├── config.properties
└── README.md

pgsql
Copy code

---

### 2. DATABASE DESIGN (MySQL)

```sql
CREATE DATABASE attendance_db;

USE attendance_db;

CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(50) UNIQUE NOT NULL,
    department VARCHAR(100),
    semester INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subjects (
    subject_id INT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(50) UNIQUE NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    semester INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    subject_id INT,
    date DATE,
    status ENUM('Present', 'Absent'),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);
3. FUNCTIONAL MODULES
🧍 STUDENT MANAGEMENT
Add new student (name, roll_no, department, semester)

Edit student details

Delete student

Search student by name or roll number

Display all students in a JTable

📘 SUBJECT MANAGEMENT
Add, edit, delete subjects

Search subjects by code or name

Display subjects in a JTable

🗓 ATTENDANCE MANAGEMENT
Select date and subject

List all students for that subject

Mark each student as "Present" or "Absent"

Save records to DB

Prevent duplicate attendance for same date + subject

Option to update attendance later

📊 ATTENDANCE REPORTS
View attendance percentage for each student

Filter by subject or date range

Show summary table: total classes, presents, absents, percentage

Export attendance report to CSV

🔒 LOGIN SYSTEM (Optional but recommended)
Basic username-password login (store in a users table)

Roles: “Admin” (full access), “Teacher” (limited to marking attendance)

4. CLASS DESIGN (OOP PRINCIPLES)
Student.java
java
Copy code
public class Student {
    private int studentId;
    private String name;
    private String rollNo;
    private String department;
    private int semester;
    // Getters, setters, constructors, toString()
}
Subject.java
java
Copy code
public class Subject {
    private int subjectId;
    private String subjectCode;
    private String subjectName;
    private int semester;
    // Getters, setters, constructors, toString()
}
AttendanceRecord.java
java
Copy code
public class AttendanceRecord {
    private int attendanceId;
    private int studentId;
    private int subjectId;
    private LocalDate date;
    private String status; // "Present" or "Absent"
    // Getters, setters, constructors
}
DatabaseConnection.java
Singleton class

Reads credentials from config.properties

Creates and provides Connection object

Example:

java
Copy code
Class.forName("com.mysql.cj.jdbc.Driver");
connection = DriverManager.getConnection(url, user, password);
5. SWING UI DESIGN
MainFrame.java
JFrame with menu bar: Students, Subjects, Attendance, Reports, Exit

CardLayout to switch between panels

Status bar at bottom (DB connection status)

StudentPanel.java
JTable for displaying students

Buttons: Add, Edit, Delete, Refresh

Form dialog for Add/Edit

SubjectPanel.java
JTable for subjects

Buttons: Add, Edit, Delete, Refresh

AttendancePanel.java
Date Picker (JDateChooser or combo boxes)

Subject dropdown

JTable: Student list with checkbox column for “Present”

Buttons: Save Attendance, View Report

ReportPanel.java
Combo box: Student or Subject filter

Table showing attendance percentage

Button: Export to CSV

UI Guidelines
Use proper layout managers (BorderLayout, GridBagLayout)

Always update UI in Event Dispatch Thread (SwingUtilities.invokeLater)

Handle database calls in background thread (SwingWorker)

6. CONFIGURATION FILE
config.properties

ini
Copy code
db.url=jdbc:mysql://localhost:3306/attendance_db
db.username=root
db.password=yourpassword
7. ERROR HANDLING & VALIDATION
Validate all input fields (no empty names or duplicate roll numbers)

Catch and display SQLExceptions in JOptionPane

Confirm before deletion

Handle DB connection errors gracefully (show retry dialog)

8. BUILD & RUN INSTRUCTIONS
Compile
bash
Copy code
javac -cp ".;lib/mysql-connector-j.jar" src//*.java -d bin
Run
bash
Copy code
java -cp "bin;lib/mysql-connector-j.jar" Main
(Use : instead of ; on Linux/Mac.)

MySQL Setup
Create DB with provided SQL.

Edit credentials in config.properties.

Place mysql-connector-j.jar inside /lib.

9. DOCUMENTATION DELIVERABLES
Copilot must produce:

All .java source files (as per structure)

config.properties

attendance_db.sql (schema + sample data)

README.md (setup, build, run)

Screenshots placeholders in README

PDF-ready text for User Manual (usage guide)

Test cases for DAO layer (optional)

10. CODING STANDARDS
Follow clean OOP design (Encapsulation, Abstraction, Inheritance, Polymorphism)

No TODOs left in code

Use PreparedStatement for all SQL queries

Code should compile and run without external dependencies other than MySQL connector

11. FINAL DELIVERABLES CHECKLIST
✅ Fully working GUI (Swing)

✅ MySQL backend (attendance_db)

✅ JDBC connectivity (singleton)

✅ CRUD for Student & Subject

✅ Attendance marking & report

✅ CSV export feature

✅ README.md + config.properties

✅ Compiles and runs cleanly in VSCode with Java 24

FINAL NOTE TO COPILOT
Do not skip any step.
Generate the full runnable Java project source code, SQL file, and README.
All JDBC connections must use config.properties.
All database operations must use prepared statements and close resources.
Output must include every .java file fully implemented with no TODOs or empty methods.

Begin generating the source code files one by one in proper order.