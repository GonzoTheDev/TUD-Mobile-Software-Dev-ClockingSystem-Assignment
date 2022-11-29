package com.C20703429.clockingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements Parcelable {

    /*
        REFERENCE: The following code is from: http://www.java2s.com/Open-Source/Android_Free_Code/Development/studio/modelMyParcelable_java.htm
    */

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
    public static final Parcelable.Creator<MainActivity> CREATOR = new Parcelable.Creator<MainActivity>() {
        public MainActivity createFromParcel(Parcel in) {
            return new MainActivity(in);
        }

        public MainActivity[] newArray(int size) {
            return new MainActivity[size];
        }
    };

    // constructor that takes a Parcel and gives you an object populated with it's values
    private MainActivity(Parcel in) {
        mData = in.readInt();
    }

    /* REFERENCE COMPLETE */

    private static MainActivity  instance;
    public MainActivity()
    {
        instance = this;
    }
    public static Context getContext()
    {
        return instance;
    }

    // Prevent the back button from closing app
    @Override
    public void onBackPressed() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Employee employee = null;

        // Get our parcelable extras from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            employee = (Employee) getIntent().getParcelableExtra("EMPLOYEE");
        }else{
            Toast.makeText(getApplicationContext(), "Error: User not logged in.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            finish();
        }

        // Create our button objects from corresponding views
        Button logoutButton = findViewById(R.id.logoutButton);
        Button timeInButton = findViewById(R.id.timeInButton);
        Button timeOutButton = findViewById(R.id.timeOutButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        // Onclick listener for the timeIn button
        Employee finalEmployee = employee;
        timeInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get Date and Time
                Date c = Calendar.getInstance().getTime();

                // Format Date
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                // Format Time
                SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String formattedTime = dt.format(c);

                // Create new shift object
                Shift newShift = new Shift(0, finalEmployee, formattedDate, formattedTime, null, MainActivity.getContext());
                String result = newShift.addShift();
                if(result != null){
                    if(result == "fail"){
                        // Toast success message
                        Toast.makeText(getApplicationContext(), "You are already clocked in, please clock out first.", Toast.LENGTH_SHORT).show();
                    }else{
                        // Toast success message
                        Toast.makeText(getApplicationContext(), "Clock in success: " + formattedDate + " " + formattedTime, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    // Toast success message
                    Toast.makeText(getApplicationContext(), "Clock in failure.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Onclick listener for the Logout button
        timeOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                
            }

        });

        // Onclick listener for the Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

        });
    }
}