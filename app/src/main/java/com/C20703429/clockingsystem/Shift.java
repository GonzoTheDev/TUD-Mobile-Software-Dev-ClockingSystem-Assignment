package com.C20703429.clockingsystem;

public class Shift {
    private int ID;
    private Employee Employee;
    private java.sql.Date Date;
    private java.sql.Time startTime;
    private java.sql.Time endTime;

    public Shift(int id, Employee employee, java.sql.Date date, java.sql.Time start, java.sql.Time end){
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
        this.employee = employee;
    }
    public java.sql.Date getDate() {
        return Date;
    }
    public void setDate(java.sql.Date date) {
        Date = date;
    }
    public java.sql.Time getStartTime() {
        return startTime;
    }
    public void setStartTime(java.sql.Time startTime) {
        this.startTime = startTime;
    }
    public java.sql.Time getEndTime() {
        return endTime;
    }
    public void setEndTime(java.sql.Time endTime) {
        this.endTime = endTime;
    }
}