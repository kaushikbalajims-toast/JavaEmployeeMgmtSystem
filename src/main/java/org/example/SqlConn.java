package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class SqlConn {

    public static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String URL = "jdbc:sqlserver://PF3ZWGE4\\SQLEXPRESS:1433;DatabaseName=Employees_new;encrypt=true;trustServerCertificate=true;integratedsecurity=true;";
    public static final String SELECTALL_EMPLOYEES_QUERY = "SELECT EmpId, EmpName, Designation, Department, Salary FROM EmployeeData WITH(NOLOCK)";
    public static final String FilteredEmployees_display = "SELECT EmpId, EmpName, Designation, Department, Salary FROM FilteredEmployees WITH(NOLOCK)";
    public static final String INSERT_EMPLOYEE_QUERY = "INSERT INTO EmployeeData(EmpName, Designation, Department, Salary) VALUES (?, ?, ?, ?)";
    public static final String ADD_ATTENDANCE_QUERY = "begin tran if exists (select * from AttendanceMaster with (nolock) where EmpId = ?) begin update AttendanceMaster set WorkDays = ? where EmpId = ? end else begin insert into AttendanceMaster (EmpId, WorkDays) values (?, ?) end commit tran";



    public static void main(String[] args) {
        try {
            getConnection();
            System.out.println("Connection success");
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            Class.forName(SqlConn.DRIVER_CLASS);
            return DriverManager.getConnection(SqlConn.URL);
        } catch (Exception e) {
            System.out.println("Error in connection");
            e.printStackTrace();
        }
        return null;
    }

    public static void InsertEmployee(String name, String desg, String dept, String sal) {
        String id;
        try (Connection con = getConnection(); PreparedStatement pstmt = con.prepareStatement(SqlConn.INSERT_EMPLOYEE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, desg);
            pstmt.setString(3, dept);
            pstmt.setString(4, sal);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        System.out.println("Employee added\n");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            PreparedStatement pstmt1 = con.prepareStatement("SELECT MAX(EmpId) FROM EmployeeData");

            ResultSet rs = pstmt1.executeQuery();
            rs.next();
            id = rs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        AddAttendance(id, ""+0);
    }

    public static void DisplayAllEmployees(String qry) {
        long id = 0;
        try (Connection con = getConnection();) {
            PreparedStatement pstmt = con.prepareStatement(qry);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.format("%10s %20s %20s %20s %20s", "Employee ID", "Name", "Designation", "Department", "Salary");
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%10s %20s %20s %20s %20s%n", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean FindEmployee(String id) {
        String sql = "SELECT EmpId, EmpName, Designation, Department, Salary FROM EmployeeData WITH(NOLOCK) WHERE EmpId = " + id;
        try (Connection con = getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
//            rs.next();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int GetEmployeesCount(){
        String sql = "SELECT COUNT(EmpId) FROM EmployeeData WITH(NOLOCK)";
        try(Connection con = getConnection()){
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void AddAttendance(String id, String days){
        try(Connection con = getConnection()){
            PreparedStatement pstmt = con.prepareStatement(SqlConn.ADD_ATTENDANCE_QUERY);
            pstmt.setString(1, id);
            pstmt.setString(3, id);
            pstmt.setString(4, id);
            pstmt.setString(2, days);
            pstmt.setString(5, days);
            pstmt.executeUpdate();
            if(!days.equals("0"))
                System.out.println("Attendance given\n");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> IdsToAddAttendance(){
        ArrayList<String> ids = new ArrayList<String>();
        try(Connection con = getConnection()){
            String sql = "SELECT e.EmpId FROM EmployeeData AS e WITH(NOLOCK) WHERE e.EmpId NOT IN (SELECT a.EmpId FROM AttendanceMaster a WHERE e.EmpId = a.EmpId AND a.WorkDays!=0)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                ids.add(""+rs.getInt(1));
                while (rs.next()) {
                    ids.add("" + rs.getInt(1));
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ids;
    }

    public static int LastId(){
        try(Connection con = getConnection()){
            String sql = "SELECT MAX(EmpId) FROM EmployeeData";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static int TableSize(){
        try(Connection con = getConnection()){
            PreparedStatement pstmt = con.prepareStatement("SELECT COUNT(EmpId) FROM AttendanceMaster where Workdays!=0");
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void FilterEmployees(){
        try(Connection con = getConnection()){
            String sql = "delete from FilteredEmployees where EmpId in (select EmpId from AttendanceMaster where WorkDays<10)";
            String sql1 = "if exists(SELECT 1 FROM sys.tables WHERE type = 'U' AND object_id = object_id('FilteredEmployees')) BEGIN DROP TABLE FilteredEmployees END select * into FilteredEmployees from EmployeeData";

            PreparedStatement pstmt = con.prepareStatement(sql1);
            pstmt.executeUpdate();
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();

            System.out.println("Employees filtered\n");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void MakePayslip(){
        try(Connection con = SqlConn.getConnection()){
            PreparedStatement pstmt = con.prepareStatement("SELECT f.EmpId, f.EmpName, f.Designation, f.Department, f.Salary, a.WorkDays FROM FilteredEmployees f, AttendanceMaster a where a.EmpId = f.EmpId");
            ResultSet rs = pstmt.executeQuery();
            double pf, sal;
            double allowance = 0, grossSalary = 0, netSalary = 0;
            System.out.println("\t\t\t\t\tEmployees payslip\n---------------------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%5s %12s %18s %15s %13s %13s %13s %10s %13s %13s", "ID", "Name", "Designation", "Department", "Salary", "Attendance", "Allowance", "PF", "Gross", "Net");
            System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------");

            while(rs.next()){
                if(Objects.equals(rs.getString("Designation"), "Manager")){
                    sal = (rs.getDouble(5)*10)/12;
                    allowance = sal*20/100;
                    pf = allowance/2;
                }
                else{
                    sal = (rs.getDouble(5)*10)/11;
                    allowance = sal*10/100;
                    pf = allowance;
                }

                pf = (Math.round((pf)*100.0)/100.0);
                sal = (Math.round((sal)*100.0)/100.0);
                allowance = (Math.round((allowance)*100.0)/100.0);
                grossSalary = (Math.round((sal + allowance)*100.0)/100.0);
                netSalary = (Math.round((grossSalary - pf)*100.0)/100.0);
                System.out.format("%5s %15s %13s %15s %15s %9s %15s %13s %13s %13s", rs.getString(1),rs.getString(2), rs.getString(3), rs.getString(4), sal, rs.getInt(6), allowance,pf, grossSalary, netSalary);
                System.out.println();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
