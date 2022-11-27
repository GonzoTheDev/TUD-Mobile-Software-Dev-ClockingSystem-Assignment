package com.C20703429.clockingsystem;

public class Shift {
    private int ID;
    private Employee Employee;
    private String Date;
    private String startTime;
    private String endTime;

    public Shift(int id, Employee employee, String date, String start, String end){
        this.ID = id;
        this.Employee = employee;
        this.Date = date;
        this.startTime = start;
        this.endTime = end;
    }

    public Integer getID() {
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
}