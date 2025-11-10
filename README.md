# Attendance Management System

A comprehensive desktop application for managing student attendance records using Java Swing for the GUI and MySQL for the backend database.

## ✨ Features

### Student Management
- Add, edit, and delete student records
- Search students by name or roll number
- View all students with department and semester information

### Subject Management
- Add, edit, and delete subject records
- Search subjects by code or name
- Organize subjects by semester

### Attendance Management
- Mark attendance for multiple students at once
- Date-wise attendance recording
- Subject-wise attendance tracking
- Update existing attendance records
- Mark all students as present/absent with one click

### Reports & Analytics
- Generate student-wise attendance reports
- Generate subject-wise attendance reports
- View attendance percentage for each student/subject
- Export reports to CSV format
- Statistical analysis of attendance data

### Additional Features
- User authentication with login system
- Intuitive dashboard for easy navigation
- Database connectivity status indicator
- Data validation for all inputs
- Prevention of duplicate entries

## 🛠 Technologies Used

- **Java 24** - Core programming language
- **Java Swing** - GUI framework
- **MySQL 8.0+** - Database management system
- **JDBC** - Database connectivity
- **Maven** - Build automation and dependency management
- **JCalendar** - Date picker component

## 💻 System Requirements

### Software Requirements
- Java Development Kit (JDK) 24 or higher
- MySQL Server 8.0 or higher
- Maven 3.6 or higher
- Any Java IDE (VS Code, IntelliJ IDEA, Eclipse) - Optional

### Hardware Requirements
- Minimum 4GB RAM
- 500MB free disk space
- Windows/Linux/macOS

## 📥 Installation & Setup

### 1. Install Java 24
Download and install Java 24 from [Oracle's website](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK.

Verify installation:
```bash
java -version
```

### 2. Install MySQL
Download and install MySQL Server from [MySQL's website](https://dev.mysql.com/downloads/mysql/).

During installation, remember your root password.

### 3. Install Maven
Download Maven from [Apache Maven website](https://maven.apache.org/download.cgi).

Add Maven to your system PATH and verify:
```bash
mvn -version
# OR if you have Maven Daemon installed:
mvnd --version
```

**Note:** You have Maven Daemon (mvnd) installed, which is a faster version of Maven. You can use `mvnd` instead of `mvn` in all commands.

### 4. Clone the Repository
```bash
git clone https://github.com/SrEE-RaM-7/Attendance-Management-System.git
cd Attendance-Management-System
```

## 🗄 Database Setup

### 1. Start MySQL Server
Make sure your MySQL server is running.

### 2. Create Database and Tables
Open MySQL command line or MySQL Workbench and run:

```bash
mysql -u root -p < attendance_db.sql
```

Or manually execute the SQL script:
```bash
mysql -u root -p
```

Then:
```sql
source attendance_db.sql
```

This will:
- Create the `attendance_db` database
- Create tables: `students`, `subjects`, `attendance`, `users`
- Insert sample data for testing

### 3. Configure Database Connection
Edit the `config.properties` file in the root directory or in `src/main/resources/`:

```properties
db.url=jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=yourpassword
```

Replace `yourpassword` with your MySQL root password.

## 🔨 Building the Project

### Using Maven

1. **Clean and compile:**
```bash
mvn clean compile
# OR with Maven Daemon (faster):
mvnd clean compile
```

2. **Package the application:**
```bash
mvn clean package
# OR with Maven Daemon (faster):
mvnd clean package
```

This will create two JAR files in the `target/` directory:
- `attendance-management-system-1.0.0.jar` - Regular JAR
- `attendance-management-system-1.0.0-jar-with-dependencies.jar` - Fat JAR with all dependencies

## 🚀 Running the Application

### Method 1: Using Maven
```bash
mvn exec:java -Dexec.mainClass="com.attendance.Main"
```

**OR with Maven Daemon (faster):**
```bash
mvnd exec:java -Dexec.mainClass="com.attendance.Main"
```

### Method 2: Using the Fat JAR
After building with Maven:
```bash
java -jar target/attendance-management-system-1.0.0-jar-with-dependencies.jar
```

### Method 3: Using IDE
1. Open the project in your IDE (VS Code, IntelliJ, Eclipse)
2. Wait for Maven to download dependencies
3. Run the `Main.java` class located in `src/main/java/com/attendance/Main.java`

### Method 4: Direct Java Execution (with Maven dependencies)
```bash
java -cp "target/classes;target/dependency/*" com.attendance.Main
```

**Note for Linux/Mac users:** Replace `;` with `:` in classpath:
```bash
java -cp "target/classes:target/dependency/*" com.attendance.Main
```

## 📖 Usage Guide

### 1. Login
- Launch the application
- Enter username and password
- Default credentials available in [Default Login Credentials](#default-login-credentials)

### 2. Managing Students
- Navigate to **Students → Manage Students**
- Click **Add Student** to create new student record
- Select a student and click **Edit Student** to modify
- Select a student and click **Delete Student** to remove
- Use the search bar to find specific students

### 3. Managing Subjects
- Navigate to **Subjects → Manage Subjects**
- Follow similar CRUD operations as students

### 4. Marking Attendance
- Navigate to **Attendance → Mark Attendance**
- Select the date using the date picker
- Choose the subject from the dropdown
- Click **Load Students** to display all students
- Mark each student as Present or Absent
- Or use **Mark All Present/Absent** buttons
- Click **Save Attendance** to store the records

### 5. Viewing Reports
- Navigate to **Reports → View Reports**
- Select report type:
  - **Student Attendance Report**: Shows attendance for all subjects of a specific student
  - **Subject Attendance Report**: Shows attendance for all students in a specific subject
- Click **Generate Report** to view
- Click **Export to CSV** to save the report

## 📁 Project Structure

```
Attendance-Management-System/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── attendance/
│       │           ├── Main.java                    # Application entry point
│       │           ├── db/
│       │           │   └── DatabaseConnection.java  # Database singleton
│       │           ├── model/
│       │           │   ├── Student.java             # Student entity
│       │           │   ├── Subject.java             # Subject entity
│       │           │   └── AttendanceRecord.java    # Attendance entity
│       │           ├── dao/
│       │           │   ├── StudentDAO.java          # Student CRUD operations
│       │           │   ├── SubjectDAO.java          # Subject CRUD operations
│       │           │   └── AttendanceDAO.java       # Attendance CRUD operations
│       │           ├── service/
│       │           │   └── AttendanceService.java   # Business logic layer
│       │           ├── ui/
│       │           │   ├── MainFrame.java           # Main application frame
│       │           │   ├── StudentPanel.java        # Student management UI
│       │           │   ├── SubjectPanel.java        # Subject management UI
│       │           │   ├── AttendancePanel.java     # Attendance marking UI
│       │           │   ├── ReportPanel.java         # Reports UI
│       │           │   └── LoginDialog.java         # Login screen
│       │           └── util/
│       │               └── CsvExporter.java         # CSV export utility
│       └── resources/
│           └── config.properties                    # Database configuration
├── attendance_db.sql                                # Database schema & sample data
├── config.properties                                # Configuration file
├── pom.xml                                          # Maven configuration
├── LICENSE                                          # MIT License
└── README.md                                        # This file
```

## 🔐 Default Login Credentials

The application comes with three pre-configured users:

### Administrator
- **Username:** `admin`
- **Password:** `admin123`
- **Role:** Admin (Full access)

### Teacher 1
- **Username:** `teacher1`
- **Password:** `teacher123`
- **Role:** Teacher

### Teacher 2
- **Username:** `teacher2`
- **Password:** `teacher123`
- **Role:** Teacher

**Note:** In a production environment, implement proper password hashing and role-based access control.

## 📸 Screenshots

### Dashboard
![Dashboard](screenshots/dashboard.png)

### Student Management
![Students](screenshots/students.png)

### Attendance Marking
![Attendance](screenshots/attendance.png)

### Reports
![Reports](screenshots/reports.png)

## 🐛 Troubleshooting

### Database Connection Error
- Verify MySQL is running
- Check credentials in `config.properties`
- Ensure database `attendance_db` exists
- Check firewall settings

### JDateChooser Not Found
- Run `mvn clean install` to download all dependencies
- Verify Maven is configured correctly

### ClassNotFoundException
- Ensure MySQL Connector JAR is in the classpath
- Run `mvn clean package` to rebuild

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Sreeram S Nair**

---

**Note:** This is an academic project created for learning purposes. For production use, additional security measures, error handling, and testing should be implemented.
