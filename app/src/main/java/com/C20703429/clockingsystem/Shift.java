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

    public String toString() {
        return "ID: " + ID + ", startTime: " + startTime + ", endTime: " + endTime;
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

    public int[] calculateTime() {

        int[] calc = findDifference(this.getStartTime(), this.getEndTime());

        return calc;

    }

    public static String calcToString(int[] dataset) {

        String result = String.valueOf(dataset[0]) + "h, " + String.valueOf(dataset[1]) + "m, " + String.valueOf(dataset[2]) + "s";

        return result;
    }
    // Function to print difference in
    // time start_date and end_date
    public int[] findDifference(String start_date, String end_date)
    {

        int[] result = {0, 0, 0};

        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Try Block
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);


            // Calculate time difference
            // in milliseconds
            long difference_In_Time = d2.getTime() - d1.getTime();

            // Calculate time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;

            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;



            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds
            result = new int[] {(int)difference_In_Hours, (int)difference_In_Minutes, (int)difference_In_Seconds};

        }

        // Catch the Exception
        catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}