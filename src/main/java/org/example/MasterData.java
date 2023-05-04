package org.example;

import java.util.*;

public class MasterData {
    private ArrayList<Employee> empList = new ArrayList<Employee>();

    MasterData(ArrayList<Employee> emp){
        this.empList = emp;
    }

    public void displayEmployees(){
        if(this.empList.size()==0){
            System.out.println("No employees to display\n");
        }
        else{
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.format("%10s %20s %20s %20s %20s", "Employee ID", "Name", "Designation", "Department", "Salary");
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------------------------");
            for (Employee emp : this.empList) {
                System.out.println(emp.toString());
            }
            System.out.println("-------------------------------------------------------------------------------------------------");
        }
    }

    public ArrayList<Employee> getEmpList(){
        return this.empList;
    }

    public void setEmpList(ArrayList<Employee> employeeList){
        this.empList = employeeList;
    }
}
