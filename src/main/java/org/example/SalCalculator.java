package org.example;

import java.util.*;

class SalCalculator{
    SalCalculator(){

    }

    public void CalculateSalary(HashMap<Employee, Integer> empAttendance){
        if(empAttendance.keySet().size()!=0){
            System.out.println("\t\t\t\t\tEmployees payslip\n---------------------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%5s %12s %18s %15s %13s %13s %13s %10s %13s %13s", "ID", "Name", "Designation", "Department", "Salary", "Attendance", "Allowance", "PF", "Gross", "Net");
            System.out.println();
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
            for(Employee emp: empAttendance.keySet()){
                double pf, sal;
                double allowance = 0, grossSalary = 0, netSalary = 0;
                if(emp.getDesg().compareTo("Manager") == 0){
                    sal = (emp.getSal()*10)/12;
                    allowance = sal*20/100;
                    pf = allowance/2;
                }
                else{
                    sal = (emp.getSal()*10)/11;
                    allowance = sal*10/100;
                    pf = allowance;
                }
                grossSalary = sal + allowance;
                netSalary = grossSalary - pf;
                System.out.format("%5s %15s %13s %15s %15s %9s %15s %13s %13s %13s", emp.getEmpID(),emp.getName(), emp.getDesg(), emp.getDept(), sal, empAttendance.get(emp), allowance,pf, grossSalary, netSalary);
                System.out.println();
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
        }
        else{
            System.out.println("No eligible employees\n");
        }
    }
}


