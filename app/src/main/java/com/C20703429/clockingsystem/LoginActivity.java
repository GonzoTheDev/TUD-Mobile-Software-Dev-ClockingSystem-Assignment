package com.C20703429.clockingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements Parcelable {

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
    public static final Parcelable.Creator<LoginActivity> CREATOR = new Parcelable.Creator<LoginActivity>() {
        public LoginActivity createFromParcel(Parcel in) {
            return new LoginActivity(in);
        }

        public LoginActivity[] newArray(int size) {
            return new LoginActivity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private LoginActivity(Parcel in) {
        mData = in.readInt();
    }

    private static LoginActivity  instance;
    public LoginActivity()
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
        setContentView(R.layout.activity_login);

        MyDatabaseHelper db = MyDatabaseHelper.getInstance(LoginActivity.getContext());
        Employee test = new Employee(0, "Shane", "admin", "admin", "sbw92@outlook.com");
        db.addOrUpdateUser(test);

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
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MyDatabaseHelper db = MyDatabaseHelper.getInstance(LoginActivity.getContext());
                String pwd = db.loginUser(username.getText().toString());
                if(pwd != null && password.getText().toString().equals(pwd)) {
                    Employee employee = db.getUser(username.getText().toString());
                    Toast.makeText(getApplicationContext(),"Hi " + employee.getName() + ". Logging in...",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("EMPLOYEE", employee);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

}
