/**

 * This class handles the employee class.
 */
package com.C20703429.clockingsystem;


public class Employee{

    // Initialize attributes
    private int ID;
    private String Name;
    private String Username;
    private String Password;
    private String Email;
    private boolean isClockedIn;

    // Define the constructor
    public Employee(int id, String name, String username, String password, String email) {
        this.ID = id;
        this.Name = name;
        this.Username = username;
        this.Password = password;
        this.Email = email;
        this.isClockedIn = false;
    }

    // Getters and setters
    public Integer getID() {
        return ID;
    }
    public void setID(int id) {
        ID = id;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getUsername() {
        return Username;
    }
    public void setUsername(String username) {
        Username = username;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }

    // Method to set the users clocked in/out status
    public void ClockInOut (Boolean clockedInOut) {
        this.isClockedIn = clockedInOut;
    }

    // Method to return the users clocked in/out status
    public boolean isClockedIn(){
        return isClockedIn;
    }

}
