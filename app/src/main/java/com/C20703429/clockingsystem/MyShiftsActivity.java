package com.C20703429.clockingsystem;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;


public class MyShiftsActivity extends ListActivity implements Parcelable {

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
    public static final Parcelable.Creator<MyShiftsActivity> CREATOR = new Parcelable.Creator<MyShiftsActivity>() {
        public MyShiftsActivity createFromParcel(Parcel in) {
            return new MyShiftsActivity(in);
        }

        public MyShiftsActivity[] newArray(int size) {
            return new MyShiftsActivity[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private MyShiftsActivity(Parcel in) {
        mData = in.readInt();
    }

    /* REFERENCE COMPLETE */

    private static MyShiftsActivity  instance;
    public MyShiftsActivity()
    {
        instance = this;
    }
    public static Context getContext()
    {
        return instance;
    }

    // declare class variables
    ArrayList<Shift> myShifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshifts);

        Employee employee = null;

        // Get our parcelable extras from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            employee = (Employee) getIntent().getParcelableExtra("EMPLOYEE");
        }else{
            Toast.makeText(getApplicationContext(), "Error: User not logged in.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MyShiftsActivity.this, LoginActivity.class);
            finish();
        }

        // create new array list of shift objects
        ArrayList<Shift> myShifts = new ArrayList<Shift>();

        // instantiate a database helper object
        MyDatabaseHelper db = MyDatabaseHelper.getInstance(MyShiftsActivity.getContext());

        // Pass employee object to getAllShifts method and store in array list
        myShifts = db.getAllShifts(employee, MyShiftsActivity.getContext());

        // instantiate our DogAdapter class,     giving it the row layout, and data source (Dogs)
        ShiftsAdapter myAdapter = new ShiftsAdapter(this, R.layout.rows, myShifts);

        // assign the adapter to the list .
        setListAdapter(myAdapter);

    }




    // This custom adapter extnends ArrayAdapter because the data is coming from an array.
    // The class is included here as an inner class, but we could have put it is its own
    // separate file if any other class apart from this list activity was using it.
    class ShiftsAdapter extends ArrayAdapter<Shift>
    {

        ArrayList<Shift> Shifts;

        // Constructor: takes in the data needed by the ArrayAdapter.

        public ShiftsAdapter(Context context, int textViewResourceId, ArrayList<Shift> objects) {
            super(context, textViewResourceId, objects);
            this.Shifts = objects;

        }

        /*
         * we are overriding the getView method here - this is what defines how each
         * list Dog will look. The getView method runs once per row, moving the data from the
         * datasource (in this case, arraylist) into the row textviews.
         */
        public View getView(int position, View convertView, ViewGroup parent)
        {

            // assign the view we are converting to a local variable
            View v = convertView;

            // first check to see if the view is inflated before. If not, we need to inflate it.

            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.rows, null);
            }

            /*
             * "position" is sent in as an argument to this method.
             * It refers to the position of the current Dog object in the list.                  *
             */
            Shift currentShift = Shifts.get(position);

            if (currentShift != null)
            {

                // This is how you obtain a reference to the TextViews.
                // These TextViews are created in the XML files we defined.
                TextView shiftIdText = (TextView) v.findViewById(R.id.shiftid);
                TextView startText = (TextView) v.findViewById(R.id.starttime);
                TextView endText = (TextView) v.findViewById(R.id.endtime);
                TextView totalTimeText = (TextView) v.findViewById(R.id.totaltime);


                shiftIdText.setText(currentShift.getID());
                startText.setText(String.valueOf(currentShift.getStartTime()));
                endText.setText(currentShift.getEndTime());
                try {
                    totalTimeText.setText(currentShift.calculateTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            return v;

        } // end of getView

    } // end of adapter class
}