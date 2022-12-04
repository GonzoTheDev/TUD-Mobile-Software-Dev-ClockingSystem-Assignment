/**

 * This class handles the MyDatabaseHelper class which extends the SQLiteOpenHelper class.
 */
package com.C20703429.clockingsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {

        // Error Tag
        public static final String TAG = "MyDatabaseHelper";

        // Database Info
        private static final String DATABASE_NAME = "clockingSystemDatabase";
        private static final int DATABASE_VERSION = 3;

        // Table Names
        private static final String TABLE_SHIFTS = "ShiftRecords";
        private static final String TABLE_EMPLOYEES = "Employees";

        // Shifts Table Columns
        private static final String KEY_SHIFT_ID = "ID";
        private static final String KEY_SHIFT_USER_ID_FK = "EmployeeID";
        private static final String KEY_SHIFT_START = "startTime";
        private static final String KEY_SHIFT_END = "endTime";

        // Employees Table Columns
        private static final String KEY_EMPLOYEE_ID = "ID";
        private static final String KEY_EMPLOYEE_USERNAME = "Username";
        private static final String KEY_EMPLOYEE_NAME = "Name";
        private static final String KEY_EMPLOYEE_PASSWORD = "Password";
        private static final String KEY_EMPLOYEE_EMAIL = "Email";

        // Instantiate an instance of this class, create a constructor and a getContext method
        private static MyDatabaseHelper instance;

        public static synchronized MyDatabaseHelper getContext(Context context) {
            if (instance == null) {
                instance = new MyDatabaseHelper(context.getApplicationContext());
            }
            return instance;
        }

        public MyDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Database configuration called when database is being configured
        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        // Method to create database tables, only called when the database is created for the first time.
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_SHIFTS_TABLE = "CREATE TABLE " + TABLE_SHIFTS +
                    "(" +
                    KEY_SHIFT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                    KEY_SHIFT_USER_ID_FK + " INTEGER REFERENCES " + TABLE_EMPLOYEES + "," + // Define a foreign key
                    KEY_SHIFT_START + " TIME," +
                    KEY_SHIFT_END + " TIME" +
                    ")";

            String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES +
                    "(" +
                    KEY_EMPLOYEE_ID + " INTEGER PRIMARY KEY," +
                    KEY_EMPLOYEE_USERNAME + " TEXT UNIQUE," +
                    KEY_EMPLOYEE_NAME + " TEXT," +
                    KEY_EMPLOYEE_PASSWORD + " TEXT," +
                    KEY_EMPLOYEE_EMAIL + " TEXT" +
                    ")";

            db.execSQL(CREATE_SHIFTS_TABLE);
            db.execSQL(CREATE_EMPLOYEES_TABLE);
        }

        // Method to upgrade database is only be called if a database already exists on disk with the same DATABASE_NAME but the DATABASE_VERSION is different.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                // Drop old tables and recreate them
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIFTS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
                onCreate(db);
            }
        }

        // Method to insert a shift into the database
        public boolean addShift(Shift shift) {

            // Initialize variables
            boolean success = false;
            Employee employee = shift.getEmployee();
            int employeeID = employee.getID();

            // Create and/or open the database for writing
            SQLiteDatabase db = getWritableDatabase();

            // Try except block and begin db transaction
            db.beginTransaction();
            try {

                // Set values to be stored in database
                ContentValues values = new ContentValues();
                values.put(KEY_SHIFT_USER_ID_FK, employeeID);
                values.put(KEY_SHIFT_START, String.valueOf(shift.getStartTime()));
                values.put(KEY_SHIFT_END, String.valueOf(shift.getEndTime()));

                // Insert values into shifts table
                db.insertOrThrow(TABLE_SHIFTS, null, values);
                db.setTransactionSuccessful();
                success = true;
            } catch (Exception e) {
                Log.d(TAG, "Error while trying to add shift to database");
            } finally {
                db.endTransaction();
            }

            return success;
        }

        // Method to update the endTime of a shift
        public boolean updateShift(Shift shift) {

            // Create and/or open the database for writing
            SQLiteDatabase db = getWritableDatabase();

            // Initialize variables
            boolean success = false;
            int empID = shift.getEmployee().getID();

            // Try except block and begin db transaction
            db.beginTransaction();
            try {

                // SELECT ID FROM SHIFTS WHERE EmployeeID = shift.getEmployee.getID() && SHIFT END is NULL
                String SHIFTS_SELECT_QUERY =
                        String.format("SELECT * FROM %s WHERE %s = %s ORDER BY ID DESC LIMIT 1",
                                TABLE_SHIFTS,
                                KEY_SHIFT_USER_ID_FK, empID);

                // Create and/or open the database for reading
                SQLiteDatabase db2 = getReadableDatabase();

                // Instantiate a cursor object with our formatted select query
                Cursor cursor = db2.rawQuery(SHIFTS_SELECT_QUERY, null);

                // Try except block
                try {

                    // If cursor has results then read the first result
                    if (cursor.moveToFirst()) {

                        // Do while loop to iterate the cursor objects results
                        do {
                            // Create a content values object and store the shift end time
                            ContentValues values = new ContentValues();
                            values.put(KEY_SHIFT_END, String.valueOf(shift.getEndTime()));

                            // get the shiftID from the cursor
                            int shiftID = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SHIFT_ID));

                            // Update the database with the necessary values to corresponding shiftIDs
                            db.update(TABLE_SHIFTS, values, "ID=?", new String[]{String.valueOf(shiftID)});
                            db.setTransactionSuccessful();

                        }while(cursor.moveToNext());

                    } else {

                        Log.d(TAG, "No results found.");

                    }

                } catch (Exception e) {

                    Log.d(TAG, "Error while trying to update shift end time.");

                } finally {

                    if (cursor != null && !cursor.isClosed()) {

                        cursor.close();

                    }
                }

                success = true;
            } catch (Exception e) {

                Log.d(TAG, "Error while trying to add shift to database");

            } finally {

                db.endTransaction();

            }

            return success;
        }


        // Method to insert or update a user in the database
        public long addOrUpdateUser(Employee user) {

            // Create and/or open the database for writing
            SQLiteDatabase db = getWritableDatabase();

            // Initialize the return variable to -1
            long userId = -1;

            db.beginTransaction();
            try {

                // Set values to be stored in database
                ContentValues values = new ContentValues();
                values.put(KEY_EMPLOYEE_USERNAME, user.getUsername());
                values.put(KEY_EMPLOYEE_NAME, user.getName());
                values.put(KEY_EMPLOYEE_PASSWORD, user.getPassword());
                values.put(KEY_EMPLOYEE_EMAIL, user.getEmail());

                // Try to update user if already exists in database, username has unique constraint
                int rows = db.update(TABLE_EMPLOYEES, values, KEY_EMPLOYEE_USERNAME + "= ?", new String[]{user.getUsername()});

                // Check if update succeeded
                if (rows == 1) {

                    // Get the username for the user we just updated
                    String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                            KEY_EMPLOYEE_ID, TABLE_EMPLOYEES, KEY_EMPLOYEE_USERNAME);
                    Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.getUsername())});

                    try {

                        if (cursor.moveToFirst()) {

                            userId = cursor.getInt(0);
                            db.setTransactionSuccessful();

                        }
                    } finally {

                        if (cursor != null && !cursor.isClosed()) {

                            cursor.close();

                        }
                    }
                } else {

                    // user with this username did not already exist, so insert new user
                    userId = db.insertOrThrow(TABLE_EMPLOYEES, null, values);
                    db.setTransactionSuccessful();

                }

            } catch (Exception e) {

                Log.d(TAG, "Error while trying to add or update user");

            } finally {

                db.endTransaction();

            }

            return userId;

        }

        // Get all employees shifts in the database
        public ArrayList<Shift> getAllShifts(Employee employee, Context context) {
            ArrayList<Shift> shifts = new ArrayList<Shift>();

            // SELECT * FROM SHIFTS WHERE EmployeeID = user.getID()
            String SHIFTS_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = %s ORDER BY %s DESC",
                            TABLE_SHIFTS,
                            KEY_SHIFT_USER_ID_FK,
                            employee.getID(),
                            KEY_SHIFT_ID);

            // Create and/or open the database for writing
            SQLiteDatabase db = getReadableDatabase();

            // Instantiate a cursor object with our formatted select query
            Cursor cursor = db.rawQuery(SHIFTS_SELECT_QUERY, null);

            // Try except block
            try {

                if (cursor.moveToFirst()) {

                    do {
                        Shift newShift = new Shift(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_SHIFT_ID)), employee, cursor.getString(cursor.getColumnIndexOrThrow(KEY_SHIFT_START)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_SHIFT_END)), context.getApplicationContext());
                        shifts.add(newShift);
                    } while(cursor.moveToNext());

                }
            } catch (Exception e) {

                Log.d(TAG, "Error while trying to get shifts from database");

            } finally {

                if (cursor != null && !cursor.isClosed()) {

                    cursor.close();

                }

            }

            return shifts;

        }



        // Login - Get employee password from username
        public String loginUser(String username) {

            // Initialize password string
            String password = null;

            // SELECT * FROM EMPLOYEES WHERE username = username
            String SHIFTS_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = ?",
                            TABLE_EMPLOYEES,
                            KEY_EMPLOYEE_USERNAME);

            // Create and/or open the database for writing
            SQLiteDatabase db = getReadableDatabase();

            // Instantiate a cursor object with our formatted select query
            Cursor cursor = db.rawQuery(SHIFTS_SELECT_QUERY, new String[] {username});

            // Try except block
            try {
                if (cursor.moveToFirst()) {

                    do {

                        password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMPLOYEE_PASSWORD));

                    } while(cursor.moveToNext());
                }
            } catch (Exception e) {

                Log.d(TAG, "Error: username not found.");

            } finally {

                if (cursor != null && !cursor.isClosed()) {

                    cursor.close();

                }
            }

            return password;
        }

        // Get employee details from database and create employee object
        public Employee getUser(String username) {

            // Initialize employee object
            Employee employee = null;

            // SELECT * FROM EMPLOYEES WHERE username = username
            String EMPLOYEES_SELECT_QUERY =
                    String.format("SELECT * FROM %s WHERE %s = ?",
                            TABLE_EMPLOYEES,
                            KEY_EMPLOYEE_USERNAME);

            // Create and/or open the database for writing
            SQLiteDatabase db = getReadableDatabase();

            // Instantiate a cursor object with our formatted select query
            Cursor cursor = db.rawQuery(EMPLOYEES_SELECT_QUERY, new String[] {username});

            // Try except block
            try {
                if (cursor.moveToFirst()) {

                    do {

                        employee = new Employee(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_EMPLOYEE_ID)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMPLOYEE_NAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMPLOYEE_USERNAME)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMPLOYEE_PASSWORD)), cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMPLOYEE_EMAIL)));

                    } while(cursor.moveToNext());
                }
            } catch (Exception e) {

                Log.d(TAG, "Error: username not found.");

            } finally {

                if (cursor != null && !cursor.isClosed()) {

                    cursor.close();

                }
            }

            return employee;

        }

        // Update the user's password
        public int updateUserPassword(Employee employee) {

            // Create and/or open the database for writing
            SQLiteDatabase db = this.getWritableDatabase();

            // Set values to be stored in database
            ContentValues values = new ContentValues();
            values.put(KEY_EMPLOYEE_PASSWORD, employee.getPassword());

            // Update the password for the row containing username
            return db.update(TABLE_EMPLOYEES, values, KEY_EMPLOYEE_USERNAME + " = ?", new String[] { String.valueOf(employee.getUsername()) });

        }

        // Method to delete all users shifts in the database
        public boolean deleteAllShifts(Employee employee) {

            // Initialize result boolean
            boolean result = false;

            // Create and/or open the database for writing
            SQLiteDatabase db = getWritableDatabase();

            // Start db transaction
            db.beginTransaction();

            // Try except block
            try {

                // Delete all shifts belonging to userid foreign key
                db.delete(TABLE_SHIFTS, KEY_SHIFT_USER_ID_FK + "= ?", new String[]{employee.getID().toString()});
                db.setTransactionSuccessful();

                // Set result to true
                result = true;

            } catch (Exception e) {

                Log.d(TAG, "Error while trying to delete all shifts");

                // Set result to false
                result = false;

            } finally {

                db.endTransaction();

            }

            return result;

        }

        // Method to delete user from the database
        public boolean deleteAccount(Employee employee) {

            // Initialize result boolean
            boolean result = false;

            // Create and/or open the database for writing
            SQLiteDatabase db = getWritableDatabase();

            // Start db transaction
            db.beginTransaction();

            // Try except block
            try {

                // Delete user where row contains username
                db.delete(TABLE_EMPLOYEES, KEY_EMPLOYEE_USERNAME + "= ?", new String[]{employee.getUsername()});
                db.setTransactionSuccessful();

                // set result to true
                result = true;

            } catch (Exception e) {

                Log.d(TAG, "Error while trying to delete employee");

                // set result to false
                result = false;

            } finally {

                db.endTransaction();

            }

            return result;

        }
}
