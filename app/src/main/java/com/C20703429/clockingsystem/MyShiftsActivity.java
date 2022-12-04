package com.C20703429.clockingsystem;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyShiftsActivity extends ListActivity {

    // Error Tag
    public static final String TAG = "MyShiftsActivity";

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

        // instantiate a database helper object
        MyDatabaseHelper db = MyDatabaseHelper.getContext(MyShiftsActivity.getContext());

        Bundle p = getIntent().getExtras();
        String employeeName = p.getString("employeeName");
        Employee employee = db.getUser(employeeName);


        // create new array list of shift objects
        ArrayList<Shift> myShifts = new ArrayList<Shift>();


        // Pass employee object to getAllShifts method and store in array list
        myShifts = db.getAllShifts(employee, MyShiftsActivity.getContext());


        // instantiate our ShiftAdapter class, giving it the row layout, and data source (Shifts)
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
         * list Shift will look. The getView method runs once per row, moving the data from the
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
             * It refers to the position of the current Shift object in the list.                  *
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


                shiftIdText.setText(String.valueOf(currentShift.getID()));
                startText.setText(String.valueOf(currentShift.getStartTime()));
                endText.setText(String.valueOf(currentShift.getEndTime()));
                totalTimeText.setText(String.valueOf(currentShift.calcToString(currentShift.calculateTime())));

            }

            return v;

        } // end of getView

    } // end of adapter class
}