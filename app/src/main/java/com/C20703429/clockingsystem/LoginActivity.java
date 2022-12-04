/**

 * This class handles the login activity.
 */
package com.C20703429.clockingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    // Instantiate an instance of this class, create a constructor and a getContext method
    private static LoginActivity instance;
    public LoginActivity()
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
        setContentView(R.layout.activity_login);

        // Initialize a database helper object
        MyDatabaseHelper db = MyDatabaseHelper.getContext(LoginActivity.getContext());

        // Create our button objects from corresponding views
        Button registerButton = findViewById(R.id.registerButton);
        Button loginButton = findViewById(R.id.loginButton);

        // Create our editText objects from corresponding views
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);

        // Onclick listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Send the user to the register activity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

        });

        // Onclick listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Check that the password matches the username
                String pwd = db.loginUser(username.getText().toString());

                if(pwd != null && password.getText().toString().equals(pwd)) {

                    // If password matches then send the user to the main activity and add their username to the intent
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("employeeName", username.getText().toString());
                    startActivity(i);
                    finish();

                }else{

                    // Else display error toast
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                }
            }

        });
    }

}
