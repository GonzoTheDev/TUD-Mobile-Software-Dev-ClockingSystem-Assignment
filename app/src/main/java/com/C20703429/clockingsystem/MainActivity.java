/**

 * This class handles the main (account homepage) activity.
 */
package com.C20703429.clockingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Instantiate an instance of this class, create a constructor and a getContext method
    private static MainActivity instance;
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

    // Initialize a shift instance to null
    Shift currentShift = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize a database helper object
        MyDatabaseHelper db = MyDatabaseHelper.getContext(MainActivity.getContext());

        // Get the employee name passed by the intent and create an employee instance
        Bundle p = getIntent().getExtras();
        String employeeName = p.getString("employeeName");
        Employee employee = db.getUser(employeeName);

        // Create our button objects from corresponding views
        Button logoutButton = findViewById(R.id.logoutButton);
        Button timeInButton = findViewById(R.id.timeInButton);
        Button timeOutButton = findViewById(R.id.timeOutButton);
        Button myShiftsButton = findViewById(R.id.myShiftsButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        // Onclick listener for the clocking in button
        timeInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get Date and Time
                Date c = Calendar.getInstance().getTime();

                // Format Date
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                String formattedTimeStamp = df.format(c);

                // Create new shift object and run it's addShift method
                Shift newShift = new Shift(0, employee, formattedTimeStamp, null, MainActivity.getContext());
                String result = newShift.addShift();

                // If result returns a fail string, display error toast.
                if(result != null){
                    if(result == "fail"){

                        // Toast error message
                        Toast.makeText(getApplicationContext(), "You are already clocked in, please clock out first.", Toast.LENGTH_SHORT).show();

                    }else{

                        // Set currentShift instance to the newly created shift.
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

        // Onclick listener for the clocking out button
        timeOutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Get Date and Time
                Date c2 = Calendar.getInstance().getTime();

                // Format Date
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                String formattedTimeStamp2 = df2.format(c2);

                // Check if employee is logged in and that currentShift is not null
                if(employee.isClockedIn()){
                    if(currentShift != null){

                        // Set the currentShift instances end time to current timestamp
                        currentShift.setEndTime(formattedTimeStamp2);

                        // Update the database using update shift method
                        if(currentShift.updateShift()) {

                            // Get the calculated time and convert the resulting int array to a string
                            int[] totaltimecalc = currentShift.calculateTime();
                            String totaltime = currentShift.calcToString(totaltimecalc);

                            // Toast success message
                            Toast.makeText(getApplicationContext(), "Clock out success: " + totaltime, Toast.LENGTH_SHORT).show();

                        } else {

                            // Toast error message
                            Toast.makeText(getApplicationContext(), "Unknown error clocking out.", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {

                    // Toast error message
                    Toast.makeText(getApplicationContext(), "You have not clocked in.", Toast.LENGTH_SHORT).show();

                }
            }

        });

        // Onclick listener for the myShifts button
        myShiftsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Send the user to the MyShifts activity and add their username to the intent
                Intent i = new Intent(MainActivity.this, MyShiftsActivity.class);
                i.putExtra("employeeName", employee.getUsername());
                startActivity(i);

            }

        });


        // Onclick listener for the settings button
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Send the user to the MyShifts activity and add their username to the intent
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                i.putExtra("employeeName", employee.getUsername());
                startActivity(i);

            }

        });

        // Onclick listener for the Logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Send the user to the login activity and kill this activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

        });
    }
}