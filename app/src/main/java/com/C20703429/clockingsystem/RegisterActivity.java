package com.C20703429.clockingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private static RegisterActivity  instance;
    public RegisterActivity()
    {
        instance = this;
    }
    public static Context getContext()
    {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Create our button objects from corresponding views
        Button submitButton = findViewById(R.id.submitButton);

        // Create our editText objects from corresponding views
        EditText name = (EditText)findViewById(R.id.name);
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);
        EditText password2 = (EditText)findViewById(R.id.password2);
        EditText email = (EditText)findViewById(R.id.email);


        // Onclick listener for the register button
        submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if( TextUtils.isEmpty(name.getText())){
                    name.setError( "Name is required!" );
                }
                else if ( TextUtils.isEmpty(email.getText())){
                    email.setError( "Email is required!" );
                }
                else if ( TextUtils.isEmpty(username.getText())){
                    username.setError( "Username is required!" );
                }
                else if ( TextUtils.isEmpty(password.getText())){
                    password.setError( "Password is required!" );
                } else {
                    if (password.getText().toString().equals(password2.getText().toString())) {
                        MyDatabaseHelper db = MyDatabaseHelper.getContext(RegisterActivity.getContext());
                        Employee dbUsername = db.getUser(username.getText().toString());
                        if (dbUsername != null) {
                            username.setError( "Username is taken!" );
                        } else {
                            Employee newEmployee = new Employee(0, name.getText().toString(), username.getText().toString(), password.getText().toString(), email.getText().toString());
                            db.addOrUpdateUser(newEmployee);
                            Toast.makeText(getApplicationContext(), "Registration Success! Please log in.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }

                    } else {
                        password.setError( "Passwords do not match!" );
                    }
                }


            }

        });


    }
}
