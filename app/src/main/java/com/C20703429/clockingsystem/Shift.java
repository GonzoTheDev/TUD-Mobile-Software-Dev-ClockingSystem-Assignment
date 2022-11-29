package com.C20703429.clockingsystem;

import android.content.Context;

public class Shift {

    private Context context;

    private int ID;
    private Employee Employee;
    private String Date;
    private String startTime;
    private String endTime;

    public Shift(int id, Employee employee, String date, String start, String end, Context context){
        this.ID = id;
        this.Employee = employee;
        this.Date = date;
        this.startTime = start;
        this.endTime = end;
        this.context=context;
    }

    public int getID() {
        return ID;
    }
    public void setID(int id) {
        ID = id;
    }
    public Employee getEmployee() {
        return Employee;
    }
    public void setEmployee(Employee employee) {
        this.Employee = employee;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String addShift () {
        String result;

        if(this.Employee.isClockedIn()){
            result = "fail";
            return result;
        }else {
            // Create an instance of the database helper class
            MyDatabaseHelper db = MyDatabaseHelper.getInstance(this.context);

            // Pass the new shift object to the database helper class addShift method
            if (db.addShift(this)) {
                this.Employee.ClockIn(true);
                result = "success";
                return result;
            } else {
                return null;
            }
        }
    }
}