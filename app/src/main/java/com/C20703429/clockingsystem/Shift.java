/**

 * This class handles the shift class.
 */
package com.C20703429.clockingsystem;

import android.content.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Shift {

    // Initialize attributes
    private Context context;
    private int ID;
    private Employee Employee;
    private String startTime;
    private String endTime;

    // Define the constructor
    public Shift(int id, Employee employee, String start, String end, Context context){
        this.ID = id;
        this.Employee = employee;
        this.startTime = start;
        this.endTime = end;
        this.context=context;
    }

    // Getters and setters
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

    // Define toString method
    public String toString() {
        return "ID: " + ID + ", startTime: " + startTime + ", endTime: " + endTime;
    }

    // Method to add new shift and return string result
    public String addShift () {

        // Initialize result string
        String result;

        // If employee is already clocked in, return fail message
        if(this.Employee.isClockedIn()){

            result = "fail";
            return result;

        }else{

            // Create an instance of the database helper class
            MyDatabaseHelper db = MyDatabaseHelper.getContext(this.context);

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
    // Method to update a shift and return boolean result
    public boolean updateShift() {

        // Create an instance of the database helper class
        MyDatabaseHelper db = MyDatabaseHelper.getContext(this.context);

        // Pass the new shift object to the database helper class updateShift method
        if (db.updateShift(this)) {

            this.Employee.ClockInOut(false);
            return true;

        } else {

            return false;

        }
    }

    // Method to calculate time difference and return int array result
    public int[] calculateTime() {

        int[] calc = timeDifference(this.getStartTime(), this.getEndTime());

        return calc;

    }

    // Method to parse int array from time difference calculation to a string
    public static String calcToString(int[] dataset) {

        String result = String.valueOf(dataset[0]) + "h, " + String.valueOf(dataset[1]) + "m, " + String.valueOf(dataset[2]) + "s";

        return result;
    }

    // Method to return int array to determine difference in time start_time and end_time
    public int[] timeDifference(String start_time, String end_time)
    {

        // Initialize int array with zero values
        int[] result = {0, 0, 0};

        // SimpleDateFormat converts the string format to date object
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Try except block
        try {

            // parse string to date object for both start and end time
            Date d1 = sdf.parse(start_time);
            Date d2 = sdf.parse(end_time);


            // Calculate time difference in milliseconds
            long difference_In_Time = d2.getTime() - d1.getTime();

            // Calculate time difference in hours, minutes and seconds
            long diffHours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long diffMins = (difference_In_Time / (1000 * 60)) % 60;
            long diffSecs = (difference_In_Time / 1000) % 60;



            // Update int array with hours, minutes and seconds values
            result = new int[] {(int)diffHours, (int)diffMins, (int)diffSecs};

        }

        // Catch exception
        catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the result
        return result;
    }
}