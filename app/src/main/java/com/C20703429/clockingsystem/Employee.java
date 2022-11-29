package com.C20703429.clockingsystem;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {
    private int mData;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mData);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Employee(Parcel in) {
        mData = in.readInt();
    }

    private int ID;
    private String Name;
    private String Username;
    private String Password;
    private String Email;
    private boolean isClockedIn;

    public Employee(int id, String name, String username, String password, String email) {
        this.ID = id;
        this.Name = name;
        this.Username = username;
        this.Password = password;
        this.Email = email;
        this.isClockedIn = false;
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

    public void ClockIn (Boolean clockedIn) {
        this.isClockedIn = clockedIn;
    }
    public boolean isClockedIn(){
        return isClockedIn;
    }

}
