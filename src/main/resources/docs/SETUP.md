# Quick Setup Guide

## Prerequisites
1. ✅ Java 24 installed
2. ✅ Maven installed
3. ✅ MySQL Server running

## Setup Steps

### 1. Configure MySQL Password
Edit `config.properties` and update your MySQL password:
```properties
db.password=yourpassword
```

### 2. Setup Database
```bash
mysql -u root -p < attendance_db.sql
```

### 3. Build Project
```bash
mvn clean package
```

### 4. Run Application
```bash
java -jar target/attendance-management-system-1.0.0-jar-with-dependencies.jar
```

Or simply:
```bash
mvn exec:java -Dexec.mainClass="com.attendance.Main"
```

## Default Login Credentials
- **Username:** admin
- **Password:** admin123

## Common Issues

### Issue: Cannot connect to database
**Solution:** 
- Check MySQL is running
- Verify password in config.properties
- Ensure attendance_db database exists

### Issue: JDateChooser not found
**Solution:** 
```bash
mvn clean install
```

### Issue: Compilation errors
**Solution:** 
```bash
mvn clean compile
```

## Directory Structure Created
```
src/
├── main/
│   ├── java/
│   │   └── com/attendance/
│   │       ├── Main.java
│   │       ├── db/DatabaseConnection.java
│   │       ├── model/ (Student, Subject, AttendanceRecord)
│   │       ├── dao/ (StudentDAO, SubjectDAO, AttendanceDAO)
│   │       ├── service/AttendanceService.java
│   │       ├── ui/ (All UI panels and dialogs)
│   │       └── util/CsvExporter.java
│   └── resources/
│       └── config.properties
```

## Features Implemented
✅ Student Management (CRUD)
✅ Subject Management (CRUD)
✅ Attendance Marking with Date Picker
✅ Reports Generation
✅ CSV Export
✅ Login System
✅ Database Connectivity
✅ Data Validation
✅ Search Functionality

Enjoy using the Attendance Management System! 🎓
