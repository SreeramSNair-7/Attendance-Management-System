@echo off
REM ========================================
REM Attendance Management System
REM Build and Run Script (Windows)
REM ========================================

echo.
echo ======================================
echo Attendance Management System
echo Build and Run Script
echo ======================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven and add it to PATH
    echo Download from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 24 and add it to PATH
    pause
    exit /b 1
)

echo [INFO] Maven found: 
mvn -version | findstr "Apache Maven"
echo.
echo [INFO] Java found:
java -version 2>&1 | findstr "version"
echo.

REM Menu
echo Choose an option:
echo.
echo 1. Clean and Build Project
echo 2. Run Application
echo 3. Clean, Build and Run
echo 4. Create Fat JAR
echo 5. Run Fat JAR
echo 6. Setup Database (requires MySQL)
echo 7. Exit
echo.

set /p choice="Enter your choice (1-7): "

if "%choice%"=="1" goto build
if "%choice%"=="2" goto run
if "%choice%"=="3" goto buildrun
if "%choice%"=="4" goto package
if "%choice%"=="5" goto runjar
if "%choice%"=="6" goto setupdb
if "%choice%"=="7" goto end

echo Invalid choice!
pause
goto end

:build
echo.
echo [INFO] Cleaning and building project...
call mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Build completed successfully!
) else (
    echo [ERROR] Build failed!
)
pause
goto end

:run
echo.
echo [INFO] Running application...
call mvn exec:java -Dexec.mainClass="com.attendance.Main"
pause
goto end

:buildrun
echo.
echo [INFO] Cleaning, building and running...
call mvn clean compile
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Build completed!
    echo [INFO] Starting application...
    call mvn exec:java -Dexec.mainClass="com.attendance.Main"
) else (
    echo [ERROR] Build failed!
)
pause
goto end

:package
echo.
echo [INFO] Creating fat JAR with dependencies...
call mvn clean package
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Fat JAR created successfully!
    echo [INFO] Location: target\attendance-management-system-1.0.0-jar-with-dependencies.jar
) else (
    echo [ERROR] Packaging failed!
)
pause
goto end

:runjar
echo.
echo [INFO] Running fat JAR...
if exist "target\attendance-management-system-1.0.0-jar-with-dependencies.jar" (
    java -jar target\attendance-management-system-1.0.0-jar-with-dependencies.jar
) else (
    echo [ERROR] Fat JAR not found!
    echo Please build the project first (Option 4)
)
pause
goto end

:setupdb
echo.
echo [INFO] Setting up database...
echo.
set /p dbuser="Enter MySQL username (default: root): "
if "%dbuser%"=="" set dbuser=root

echo [INFO] Running SQL script...
echo [INFO] You will be prompted for MySQL password
mysql -u %dbuser% -p < attendance_db.sql
if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Database setup completed!
) else (
    echo [ERROR] Database setup failed!
    echo Make sure MySQL is running and credentials are correct
)
pause
goto end

:end
echo.
echo Thank you for using Attendance Management System!
echo.
