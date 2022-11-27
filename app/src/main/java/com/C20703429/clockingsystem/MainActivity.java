package com.C20703429.clockingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Employee employee = (Employee) getIntent().getParcelableExtra("EMPLOYEE");
        }else{
            Toast.makeText(getApplicationContext(), "Error: User not logged in.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
        }
    }
}