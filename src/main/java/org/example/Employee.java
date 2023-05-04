package org.example;

import java.util.Scanner;

class Employee{
    private static int counter = 1001;
    private int empID;
    private String name;
    private String designation;
    private String department;
    private double salary;

    Employee(){
    }

    public int getEmpID(){
        return this.empID;
    }
    public String getName(){
        return this.name;
    }
    public String getDesg(){
        return this.designation;
    }
    public String getDept(){
        return this.department;
    }
    public double getSal(){
        return this.salary;
    }

    public void setEmpID(int id){
        this.empID = id;

    }
    public void setName(String name){
        boolean starting = true;
        Scanner sc = new Scanner(System.in);
        do {
            if (starting) {
                System.out.print("Enter name: ");
                name = sc.nextLine();
                starting = false;
            }
            System.out.println("Enter valid name (Start with caps)\n");
            System.out.print("Enter name: ");
            name = sc.nextLine();
        } while (!name.matches("^[A-Z][a-zA-Z ]+"));
        this.name = name;
        this.empID = SqlConn.LastId()+1;
    }
    public void setDesg(String desg){
        String[] designation_array = {"Manager", "Tester", "Software Dev", "Intern"};
        Scanner scin = new Scanner(System.in);
        boolean starting = true;
        boolean invalidInput = false;
        int desg_choice = 0;
        do{
            if(starting == true){
                System.out.print("\nDesignation choice\n1.Manager\n2.Tester\n3.Software Developer\n4.Intern\nEnter designation choice: ");
                desg = scin.nextLine();
                starting = false;
            }
            invalidInput = false;
            try{
                desg_choice = Integer.parseInt(desg);
            }
            catch(NumberFormatException e){
                System.out.println("Enter only one integer option\n");
                invalidInput = true;
                System.out.print("Designation choice\n1.Manager\n2.Tester\n3.Software Developer\n4.Intern\nEnter designation choice: ");
                desg = scin.nextLine();
            }
            if((desg_choice>4 || desg_choice<0) && !invalidInput){
                System.out.println("Enter an available option (1-4)\n");
                System.out.print("Designation choice\n1.Manager\n2.Tester\n3.Software Developer\n4.Intern\nEnter designation choice: ");
                invalidInput = true;
                desg = scin.nextLine();
            }
        }while(invalidInput);
        this.designation = designation_array[desg_choice-1];
    }
    public void setDept(String dept){
        String[] department_array = {"R & D", "IT", "Admin", "HR", "Support"};
        Scanner scin = new Scanner(System.in);
        boolean invalidInput;
        boolean starting = true;
        int dept_choice = 0;
        do{
            if(starting){
                System.out.print("\nDepartment choices \n1.R & D\n2.IT\n3.Admin\n4.HR\n5.Support\nEnter department choice: ");
                dept = scin.nextLine();
                starting = false;
            }
            invalidInput = false;
            try{
                dept_choice = Integer.parseInt(dept);
            }
            catch(NumberFormatException e){
                System.out.println("Enter only one integer option\n");
                invalidInput = true;
                System.out.print("\nDepartment choices \n1.R & D\n2.IT\n3.Admin\n4.HR\n5.Support\nEnter department choice:");
                dept = scin.nextLine();
            }
            if((dept_choice>5 || dept_choice<0) && !invalidInput){
                System.out.println("Enter an available option (1-5)\n");
                System.out.print("\nDepartment choices \n1.R & D\n2.IT\n3.Admin\n4.HR\n5.Support\nEnter department choice:");
                invalidInput = true;
                dept = scin.nextLine();
            }
        }while(invalidInput);
        this.department = department_array[dept_choice-1];
    }
    public void setSal(double sal){
        Scanner scin = new Scanner(System.in);
        boolean invalidInput;
        boolean starting = true;

        do{
            invalidInput = false;
            if(starting == true){
                System.out.print("Enter Salary: ");
                starting = false;
            }
            try{
                sal = Double.parseDouble(scin.nextLine());
            }
            catch(NumberFormatException e){
                System.out.println("Enter valid salary");
                invalidInput = true;
                System.out.print("Enter Salary: ");
            }
            if(sal < 10000 && invalidInput!=true){
                System.out.println("Enter more than 4 figure salary");
                invalidInput = true;
                System.out.print("Enter Salary: ");
            }
        }while(invalidInput);

        this.salary = sal;
        this.SetAllowance();
    }

    public void SetAllowance(){
        if(this.designation.compareTo("Manager") == 0){
            this.salary+=(20*this.salary/100);
        }
        else{
            this.salary+=(10*this.salary/100);
        }
    }
    @Override
    public String toString(){
        return String.format("%10s %20s %20s %20s %20s", this.getEmpID(),this.getName(), this.getDesg(), this.getDept(), this.getSal());
    }
}