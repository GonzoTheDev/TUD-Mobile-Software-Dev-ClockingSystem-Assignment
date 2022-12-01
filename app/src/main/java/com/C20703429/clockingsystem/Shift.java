package com.C20703429.clockingsystem;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Shift {

    private Context context;

    private int ID;
    private Employee Employee;
    private String startTime;
    private String endTime;

    public Shift(int id, Employee employee, String start, String end, Context context){
        this.ID = id;
        this.Employee = employee;
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
                this.Employee.ClockInOut(true);
                result = "success";
                return result;
            } else {
                return null;
            }
        }
    }

    public boolean updateShift() {
        // Create an instance of the database helper class
        MyDatabaseHelper db = MyDatabaseHelper.getInstance(this.context);

        // Pass the new shift object to the database helper class updateShift method
        if (db.updateShift(this)) {
            this.Employee.ClockInOut(false);
            return true;
        } else {
            return false;
        }
    }

    public String calculateTime() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startTime = format.parse(this.getStartTime());
        Date endTime = format.parse(this.getEndTime());
        long difference = endTime.getTime() - startTime.getTime();
        long msPerHour = 1000*60*60;
        long hours = difference/msPerHour;
        long minutes = difference % msPerHour;

        String result = Long.toString(hours) + "hrs " + Long.toString(minutes) + "mins";

        return result;

    }
}