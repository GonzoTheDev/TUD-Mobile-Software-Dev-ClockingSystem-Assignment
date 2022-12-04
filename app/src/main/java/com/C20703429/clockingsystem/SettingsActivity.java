package com.C20703429.clockingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // instantiate a database helper object
        MyDatabaseHelper db = MyDatabaseHelper.getContext(MyShiftsActivity.getContext());

        Bundle p = getIntent().getExtras();
        String employeeName = p.getString("employeeName");
        Employee employee = db.getUser(employeeName);


        Button deleteShiftsButton = findViewById(R.id.deleteShiftsButton);
        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);

        // Onclick listener for the myShifts button
        deleteShiftsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (db.deleteAllShifts(employee)) {
                    // Toast success message
                    Toast.makeText(getApplicationContext(), "All shifts deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {

                    // Toast fail message
                    Toast.makeText(getApplicationContext(), "Error: Shift deletion unsuccessful.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Onclick listener for the delete account button
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (db.deleteAllShifts(employee)) {
                    if (db.deleteAccount(employee)) {
                        Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        // Toast success message
                        Toast.makeText(getApplicationContext(), "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Toast fail message
                        Toast.makeText(getApplicationContext(), "Error: Account not deleted.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Toast fail message
                    Toast.makeText(getApplicationContext(), "Error: Account not deleted. Problem with shifts deletion.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
