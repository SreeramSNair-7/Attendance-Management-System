#!/bin/bash
# ========================================
# Attendance Management System
# Build and Run Script (Linux/Mac)
# ========================================

echo ""
echo "======================================"
echo "Attendance Management System"
echo "Build and Run Script"
echo "======================================"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven is not installed or not in PATH"
    echo "Please install Maven: https://maven.apache.org/download.cgi"
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed or not in PATH"
    echo "Please install Java 24"
    exit 1
fi

echo "[INFO] Maven found:"
mvn -version | grep "Apache Maven"
echo ""
echo "[INFO] Java found:"
java -version 2>&1 | grep "version"
echo ""

# Menu
echo "Choose an option:"
echo ""
echo "1. Clean and Build Project"
echo "2. Run Application"
echo "3. Clean, Build and Run"
echo "4. Create Fat JAR"
echo "5. Run Fat JAR"
echo "6. Setup Database (requires MySQL)"
echo "7. Exit"
echo ""

read -p "Enter your choice (1-7): " choice

case $choice in
    1)
        echo ""
        echo "[INFO] Cleaning and building project..."
        mvn clean compile
        if [ $? -eq 0 ]; then
            echo "[SUCCESS] Build completed successfully!"
        else
            echo "[ERROR] Build failed!"
        fi
        ;;
    2)
        echo ""
        echo "[INFO] Running application..."
        mvn exec:java -Dexec.mainClass="com.attendance.Main"
        ;;
    3)
        echo ""
        echo "[INFO] Cleaning, building and running..."
        mvn clean compile
        if [ $? -eq 0 ]; then
            echo "[SUCCESS] Build completed!"
            echo "[INFO] Starting application..."
            mvn exec:java -Dexec.mainClass="com.attendance.Main"
        else
            echo "[ERROR] Build failed!"
        fi
        ;;
    4)
        echo ""
        echo "[INFO] Creating fat JAR with dependencies..."
        mvn clean package
        if [ $? -eq 0 ]; then
            echo "[SUCCESS] Fat JAR created successfully!"
            echo "[INFO] Location: target/attendance-management-system-1.0.0-jar-with-dependencies.jar"
        else
            echo "[ERROR] Packaging failed!"
        fi
        ;;
    5)
        echo ""
        echo "[INFO] Running fat JAR..."
        if [ -f "target/attendance-management-system-1.0.0-jar-with-dependencies.jar" ]; then
            java -jar target/attendance-management-system-1.0.0-jar-with-dependencies.jar
        else
            echo "[ERROR] Fat JAR not found!"
            echo "Please build the project first (Option 4)"
        fi
        ;;
    6)
        echo ""
        echo "[INFO] Setting up database..."
        echo ""
        read -p "Enter MySQL username (default: root): " dbuser
        dbuser=${dbuser:-root}
        
        echo "[INFO] Running SQL script..."
        echo "[INFO] You will be prompted for MySQL password"
        mysql -u "$dbuser" -p < attendance_db.sql
        if [ $? -eq 0 ]; then
            echo "[SUCCESS] Database setup completed!"
        else
            echo "[ERROR] Database setup failed!"
            echo "Make sure MySQL is running and credentials are correct"
        fi
        ;;
    7)
        echo ""
        echo "Thank you for using Attendance Management System!"
        exit 0
        ;;
    *)
        echo "Invalid choice!"
        exit 1
        ;;
esac

echo ""
echo "Thank you for using Attendance Management System!"
echo ""
