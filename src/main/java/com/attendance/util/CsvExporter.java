package com.attendance.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Utility class for exporting data to CSV format
 */
public class CsvExporter {
    
    /**
     * Export student attendance report to CSV
     */
    public static boolean exportStudentReport(String filePath, String studentName, String rollNo, 
                                               List<Map<String, Object>> reportData) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Student Attendance Report\n");
            writer.append("Student Name: ").append(studentName).append("\n");
            writer.append("Roll Number: ").append(rollNo).append("\n");
            writer.append("Generated On: ").append(LocalDate.now().toString()).append("\n\n");
            
            // Write column headers
            writer.append("Subject Code,Subject Name,Total Classes,Present,Absent,Percentage\n");
            
            // Write data rows
            for (Map<String, Object> row : reportData) {
                writer.append(escapeSpecialCharacters(row.get("subjectCode").toString())).append(",");
                writer.append(escapeSpecialCharacters(row.get("subjectName").toString())).append(",");
                writer.append(row.get("total").toString()).append(",");
                writer.append(row.get("present").toString()).append(",");
                writer.append(row.get("absent").toString()).append(",");
                writer.append(String.format("%.2f", row.get("percentage"))).append("%\n");
            }
            
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting student report to CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export subject attendance report to CSV
     */
    public static boolean exportSubjectReport(String filePath, String subjectCode, String subjectName, 
                                               List<Map<String, Object>> reportData) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Subject Attendance Report\n");
            writer.append("Subject: ").append(subjectCode).append(" - ").append(subjectName).append("\n");
            writer.append("Generated On: ").append(LocalDate.now().toString()).append("\n\n");
            
            // Write column headers
            writer.append("Roll No,Student Name,Department,Total Classes,Present,Absent,Percentage\n");
            
            // Write data rows
            for (Map<String, Object> row : reportData) {
                writer.append(escapeSpecialCharacters(row.get("rollNo").toString())).append(",");
                writer.append(escapeSpecialCharacters(row.get("studentName").toString())).append(",");
                writer.append(escapeSpecialCharacters(row.get("department").toString())).append(",");
                writer.append(row.get("total").toString()).append(",");
                writer.append(row.get("present").toString()).append(",");
                writer.append(row.get("absent").toString()).append(",");
                writer.append(String.format("%.2f", row.get("percentage"))).append("%\n");
            }
            
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting subject report to CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export attendance records by date range to CSV
     */
    public static boolean exportAttendanceByDateRange(String filePath, LocalDate startDate, LocalDate endDate,
                                                       List<Object[]> reportData) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Attendance Records Report\n");
            writer.append("Date Range: ").append(startDate.toString()).append(" to ").append(endDate.toString()).append("\n");
            writer.append("Generated On: ").append(LocalDate.now().toString()).append("\n\n");
            
            // Write column headers
            writer.append("Date,Roll No,Student Name,Subject Code,Subject Name,Status\n");
            
            // Write data rows
            for (Object[] row : reportData) {
                writer.append(row[0].toString()).append(","); // date
                writer.append(escapeSpecialCharacters(row[1].toString())).append(","); // rollNo
                writer.append(escapeSpecialCharacters(row[2].toString())).append(","); // studentName
                writer.append(escapeSpecialCharacters(row[3].toString())).append(","); // subjectCode
                writer.append(escapeSpecialCharacters(row[4].toString())).append(","); // subjectName
                writer.append(row[5].toString()).append("\n"); // status
            }
            
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting attendance records to CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Export overall attendance summary to CSV
     */
    public static boolean exportOverallSummary(String filePath, List<Object[]> summaryData) {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Overall Attendance Summary\n");
            writer.append("Generated On: ").append(LocalDate.now().toString()).append("\n\n");
            
            // Write column headers
            writer.append("Roll No,Student Name,Department,Semester,Total Classes,Present,Absent,Percentage\n");
            
            // Write data rows
            for (Object[] row : summaryData) {
                writer.append(escapeSpecialCharacters(row[0].toString())).append(","); // rollNo
                writer.append(escapeSpecialCharacters(row[1].toString())).append(","); // studentName
                writer.append(escapeSpecialCharacters(row[2].toString())).append(","); // department
                writer.append(row[3].toString()).append(","); // semester
                writer.append(row[4].toString()).append(","); // total
                writer.append(row[5].toString()).append(","); // present
                writer.append(row[6].toString()).append(","); // absent
                writer.append(String.format("%.2f", row[7])).append("%\n"); // percentage
            }
            
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting overall summary to CSV: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Escape special characters in CSV values
     */
    private static String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
