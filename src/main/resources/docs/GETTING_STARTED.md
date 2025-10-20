# 🚀 Getting Started with Attendance Management System

Welcome! This guide will help you get the Attendance Management System up and running in just a few minutes.

---

## 📋 Before You Start

Make sure you have these installed:
- [ ] **Java 24** (or higher)
- [ ] **Maven 3.6+**
- [ ] **MySQL 8.0+**

### ✅ Verify Installation

Open your terminal/command prompt and run:

```bash
# Check Java
java -version
# Should show: java version "24" or higher

# Check Maven
mvn -version
# Should show: Apache Maven 3.x.x

# Check MySQL
mysql --version
# Should show: mysql  Ver 8.x.x
```

If any command fails, install the missing software first.

---

## 🎯 Step-by-Step Setup (5 Minutes)

### Step 1: Update Database Configuration (30 seconds)

Open `config.properties` in the project root directory and update your MySQL password:

```properties
db.url=jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_MYSQL_PASSWORD_HERE  👈 Change this!
```

**Also update** `src/main/resources/config.properties` with the same password.

---

### Step 2: Create Database (1 minute)

Open your terminal in the project directory and run:

**Windows (Command Prompt):**
```cmd
mysql -u root -p < attendance_db.sql
```

**Linux/Mac:**
```bash
mysql -u root -p < attendance_db.sql
```

When prompted, enter your MySQL root password.

✅ This will:
- Create the `attendance_db` database
- Create 4 tables (students, subjects, attendance, users)
- Insert sample data (15 students, 14 subjects)
- Create user accounts

---

### Step 3: Build the Project (2 minutes)

In the project directory, run:

```bash
mvn clean package
```

This will:
- Download all dependencies (MySQL connector, JCalendar)
- Compile all Java files
- Create a runnable JAR file
- Take 1-2 minutes on first run (downloads dependencies)

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

### Step 4: Run the Application (30 seconds)

**Method 1: Using Maven (Easiest)**
```bash
mvn exec:java -Dexec.mainClass="com.attendance.Main"
```

**Method 2: Using the JAR file**
```bash
java -jar target/attendance-management-system-1.0.0-jar-with-dependencies.jar
```

**Method 3: Using build scripts**

Windows:
```cmd
build-and-run.bat
```

Linux/Mac:
```bash
chmod +x build-and-run.sh
./build-and-run.sh
```

---

### Step 5: Login & Explore (30 seconds)

When the application starts, you'll see a login screen.

**Use these credentials:**
```
Username: admin
Password: admin123
```

Click **Login** or press **Enter**.

---

## 🎮 Your First Tasks

### 1. Explore the Dashboard
After login, you'll see the main dashboard with 4 buttons:
- 👥 Manage Students
- 📚 Manage Subjects
- ✅ Mark Attendance
- 📊 View Reports

### 2. View Sample Data
Click **"Manage Students"** to see the 15 pre-loaded students.
Click **"Manage Subjects"** to see the 14 pre-loaded subjects.

### 3. Mark Attendance
1. Click **"Mark Attendance"**
2. Select today's date
3. Choose a subject (e.g., "CS501 - Data Structures")
4. Click **"Load Students"**
5. Mark attendance as Present/Absent for each student
6. Click **"Save Attendance"**

### 4. Generate a Report
1. Click **"View Reports"**
2. Select **"Student Attendance Report"**
3. Choose a student from dropdown
4. Click **"Generate Report"**
5. See attendance percentage for all subjects

### 5. Export to CSV
After generating any report:
1. Click **"Export to CSV"**
2. Choose where to save the file
3. Open the CSV in Excel/LibreOffice

---

## 🆘 Troubleshooting

### Problem: "Cannot connect to database"

**Solution:**
1. Check MySQL is running:
   ```bash
   # Windows
   net start MySQL80
   
   # Linux
   sudo systemctl start mysql
   ```
2. Verify password in `config.properties`
3. Verify database exists:
   ```sql
   mysql -u root -p
   SHOW DATABASES;
   # Should see 'attendance_db' in the list
   ```

---

### Problem: "mvn command not found"

**Solution:**
1. Install Maven from https://maven.apache.org/download.cgi
2. Add Maven to your system PATH
3. Restart your terminal/command prompt
4. Try again: `mvn -version`

---

### Problem: "JDateChooser not found" error

**Solution:**
```bash
# Run Maven install to download all dependencies
mvn clean install
```

---

### Problem: Build fails with compilation errors

**Solution:**
1. Make sure you have Java 24:
   ```bash
   java -version
   ```
2. Clean and rebuild:
   ```bash
   mvn clean compile
   ```
3. If still fails, delete `target` folder and try again

---

### Problem: "Access denied for user 'root'"

**Solution:**
Your MySQL password in `config.properties` is incorrect.
1. Find your correct MySQL password
2. Update both config.properties files
3. Restart the application

---

## 📚 What to Do Next

### Learn the Features
- ✅ Add your own students
- ✅ Add your own subjects
- ✅ Mark attendance for different dates
- ✅ Generate various reports
- ✅ Export data to CSV

### Explore the Code
Check out these important files:
- `src/main/java/com/attendance/Main.java` - Entry point
- `src/main/java/com/attendance/ui/MainFrame.java` - Main window
- `src/main/java/com/attendance/dao/*.java` - Database operations
- `attendance_db.sql` - Database schema

### Customize
You can modify:
- Login credentials (in database users table)
- UI colors and styles (in UI classes)
- Validation rules (in AttendanceService.java)
- Report formats (in CsvExporter.java)

---

## 🎓 Sample Data Overview

### Pre-loaded Students (15)
- Computer Science: 8 students (CS001-CS008)
- Electronics: 4 students (EC001-EC004)
- Mechanical: 3 students (ME001-ME003)

### Pre-loaded Subjects (14)
- Computer Science: 9 subjects (CS501-CS702)
- Electronics: 3 subjects (EC301-EC501)
- Mechanical: 3 subjects (ME401-ME501)

### User Accounts (3)
| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Administrator |
| teacher1 | teacher123 | Teacher |
| teacher2 | teacher123 | Teacher |

---

## 💡 Pro Tips

### 1. Quick Actions
- Use **Search** to find students/subjects quickly
- Use **Mark All Present** to save time
- Use **Refresh** button if data looks outdated

### 2. Keyboard Shortcuts
- Press **Enter** on login screen to login
- Press **Tab** to navigate between form fields

### 3. Data Management
- Always use **Search** before adding to avoid duplicates
- Use **Export to CSV** to backup your data
- Delete with care - attendance records will also be deleted

### 4. Reports
- Generate student reports to see individual performance
- Generate subject reports to see class attendance
- Export reports regularly for record keeping

---

## 📖 Additional Resources

- **Full Documentation**: See `README.md`
- **Quick Setup**: See `SETUP.md`
- **Project Details**: See `PROJECT_SUMMARY.md`
- **Completion Status**: See `COMPLETION_CHECKLIST.md`

---

## ✅ Quick Checklist

- [ ] Java 24 installed
- [ ] Maven installed
- [ ] MySQL running
- [ ] config.properties updated with password
- [ ] Database created (attendance_db.sql executed)
- [ ] Project built (mvn clean package)
- [ ] Application running
- [ ] Logged in successfully
- [ ] Explored the dashboard
- [ ] Tried adding a student
- [ ] Tried marking attendance
- [ ] Tried generating a report

---

## 🎉 Congratulations!

You're now ready to use the Attendance Management System!

If you have any issues, check the **Troubleshooting** section above or refer to the comprehensive `README.md`.

**Enjoy managing attendance! 🎓**

---

*Need help? Check README.md for detailed documentation*
*Having issues? See the Troubleshooting section above*
