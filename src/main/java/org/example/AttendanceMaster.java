package org.example;

import java.util.*;

public class AttendanceMaster {
    private HashMap<Employee, Integer> empAtten = new HashMap<Employee,Integer>();

    AttendanceMaster( HashMap<Employee, Integer> emphash){
        this.empAtten = emphash;
    }
    public HashMap<Employee,Integer> getEmpAtten(){
        return this.empAtten;
    }

    public void showEligibleList(){
        System.out.println("Eligible employees\n");
        ArrayList<Employee> emList = new ArrayList<Employee>(this.empAtten.keySet());
        if(emList.size()==0){
            System.out.println("No Eligible Employees\n");
        }
        else{
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.format("%10s %20s %20s %20s %20s", "Employee ID", "Name", "Designation", "Department", "Salary");
            System.out.println();
            System.out.println("-------------------------------------------------------------------------------------------------");
            for (Employee emp : emList) {
                if(this.empAtten.get(emp) > 9){
                    System.out.println(emp.toString());
                }
            }
            System.out.println("-------------------------------------------------------------------------------------------------");
        }
    }

    public void FilterEmployeeList(){
        ArrayList<Employee> emList = new ArrayList<Employee>(empAtten.keySet());
        for (Employee emp : emList) {
            if(empAtten.get(emp) < 10){
                empAtten.remove(emp);
            }
        }
    }
}

