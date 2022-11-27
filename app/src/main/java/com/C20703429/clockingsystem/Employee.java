package com.C20703429.clockingsystem;

public class Employee {
    private int ID;
    private String Name;
    private String Username;
    private String Password;
    private String Email;

    public Employee(int id, String name, String username, String password, String email) {
        this.ID = id;
        this.Name = name;
        this.Username = username;
        this.Password = password;
        this.Email = email;
    }
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
    
}
