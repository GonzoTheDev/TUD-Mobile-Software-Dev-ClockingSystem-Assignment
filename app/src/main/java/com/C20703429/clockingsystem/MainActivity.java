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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {




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


    Shift currentShift = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDatabaseHelper db = MyDatabaseHelper.getInstance(MainActivity.getContext());
        Bundle p = getIntent().getExtras();
        String employeeName = p.getString("employeeName");
        Employee employee = db.getUser(employeeName);



        // Create our button objects from corresponding views
        Button logoutButton = findViewById(R.id.logoutButton);
        Button timeInButton = findViewById(R.id.timeInButton);
        Button timeOutButton = findViewById(R.id.timeOutButton);
        Button myShiftsButton = findViewById(R.id.myShiftsButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        // Onclick listener for the timeIn button
        timeInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get Date and Time
                Date c = Calendar.getInstance().getTime();

                // Format Date
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                String formattedTimeStamp = df.format(c);

                // Create new shift object
                Shift newShift = new Shift(0, employee, formattedTimeStamp, null, MainActivity.getContext());
                String result = newShift.addShift();
                if(result != null){
                    if(result == "fail"){
                        // Toast success message
                        Toast.makeText(getApplicationContext(), "You are already clocked in, please clock out first.", Toast.LENGTH_SHORT).show();
                    }else{
                        currentShift = newShift;
                        // Toast success message
                        Toast.makeText(getApplicationContext(), "Clock in success: " + formattedTimeStamp, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    // Toast success message
                    Toast.makeText(getApplicationContext(), "Clock in failure." + employee.getID(), Toast.LENGTH_SHORT).show();
                }
            }

        });


        // Onclick listener for the time out button
        timeOutButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                // Get Date and Time
                Date c2 = Calendar.getInstance().getTime();

                // Format Date
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                String formattedTimeStamp2 = df2.format(c2);

                if(employee.isClockedIn()){
                    if(currentShift != null){
                        currentShift.setEndTime(formattedTimeStamp2);
                        if(currentShift.updateShift()) {

                            String totaltime = currentShift.calculateTime();

                            // Toast success message
                            Toast.makeText(getApplicationContext(), "Clock out success: " + totaltime, Toast.LENGTH_SHORT).show();
                        } else {
                            // Toast success message
                            Toast.makeText(getApplicationContext(), "Unknown error clocking out.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Toast success message
                    Toast.makeText(getApplicationContext(), "You have not clocked in.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Onclick listener for the myShifts button
        myShiftsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyShiftsActivity.class);
                i.putExtra("employeeName", employee.getUsername());
                startActivity(i);
            }

        });

        // Onclick listener for the Logout button
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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