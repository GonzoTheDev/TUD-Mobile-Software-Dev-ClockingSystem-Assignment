package com.C20703429.clockingsystem;

public class Shift {
    private Integer ID;
    private Employee employee;
    private java.sql.Date Date;
    private java.sql.Time startTime;
    private java.sql.Time endTime;

    public Integer getID() {
        return ID;
    }
    public void setID(Integer iD) {
        ID = iD;
    }
    public Employee getEmployee() {
        return employee;
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