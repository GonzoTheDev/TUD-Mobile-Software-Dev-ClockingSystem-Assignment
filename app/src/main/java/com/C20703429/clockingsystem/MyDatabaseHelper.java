package com.C20703429.clockingsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

        // Error Tag
        public static final String TAG = "ERR: ";

        // Database Info
        private static final String DATABASE_NAME = "clockingSystemDatabase";
        private static final int DATABASE_VERSION = 1;

        // Table Names
        private static final String TABLE_SHIFTS = "ShiftRecords";
        private static final String TABLE_EMPLOYEES = "Employees";

        // Shifts Table Columns
        private static final String KEY_SHIFT_ID = "ID";
        private static final String KEY_SHIFT_USER_ID_FK = "EmployeeID";
        private static final String KEY_SHIFT_DATE = "Date";
        private static final String KEY_SHIFT_START = "startTime";
        private static final String KEY_SHIFT_END = "endTime";

        // Employees Table Columns
        private static final String KEY_EMPLOYEE_ID = "ID";
        private static final String KEY_EMPLOYEE_USERNAME = "Username";
        private static final String KEY_EMPLOYEE_NAME = "Name";
        private static final String KEY_EMPLOYEE_PASSWORD = "Password";
        private static final String KEY_EMPLOYEE_EMAIL = "Email";

        private static MyDatabaseHelper sInstance;

        public static synchronized MyDatabaseHelper getInstance(Context context) {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            if (sInstance == null) {
                sInstance = new MyDatabaseHelper(context.getApplicationContext());
            }
            return sInstance;
        }

        public MyDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Called when the database connection is being configured.
        // Configure database settings for things like foreign key support, write-ahead logging, etc.
        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        // Called when the database is created for the FIRST time.
        // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_SHIFTS_TABLE = "CREATE TABLE " + TABLE_SHIFTS +
                    "(" +
                    KEY_SHIFT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                    KEY_SHIFT_USER_ID_FK + " INTEGER REFERENCES " + TABLE_EMPLOYEES + "," + // Define a foreign key
                    KEY_SHIFT_DATE + " DATE," +
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

        // Called when the database needs to be upgraded.
        // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
        // but the DATABASE_VERSION is different than the version of the database that exists on disk.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                // Simplest implementation is to drop all old tables and recreate them
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIFTS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
                onCreate(db);
            }
        }

        // Insert a shift into the database
        public void addShift(Shift shift) {
            // Create and/or open the database for writing
            SQLiteDatabase db = getWritableDatabase();

            // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
            // consistency of the database.
            db.beginTransaction();
            try {

                ContentValues values = new ContentValues();
                values.put(KEY_SHIFT_USER_ID_FK, shift.employeeID);
                values.put(KEY_SHIFT_DATE, String.valueOf(shift.Date));
                values.put(KEY_SHIFT_START, String.valueOf(shift.startTime));
                values.put(KEY_SHIFT_END, String.valueOf(shift.endTime));

                // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
                db.insertOrThrow(TABLE_SHIFTS, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d(TAG, "Error while trying to add shift to database");
            } finally {
                db.endTransaction();
            }
        }



        // Insert or update a user in the database
        // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
        // user already exists) optionally followed by an INSERT (in case the user does not already exist).
        // Unfortunately, there is a bug with the insertOnConflict method
        // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
        // verbose option of querying for the user's primary key if we did an update.
        public long addOrUpdateUser(Employee user) {
            // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
            SQLiteDatabase db = getWritableDatabase();
            long userId = -1;

            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_EMPLOYEE_USERNAME, user.Username);
                values.put(KEY_EMPLOYEE_NAME, user.Name);
                values.put(KEY_EMPLOYEE_PASSWORD, user.Password);
                values.put(KEY_EMPLOYEE_EMAIL, user.Email);

                // First try to update the user in case the user already exists in the database
                // This assumes userNames are unique
                int rows = db.update(TABLE_EMPLOYEES, values, KEY_EMPLOYEE_USERNAME + "= ?", new String[]{user.Username});

                // Check if update succeeded
                if (rows == 1) {
                    // Get the primary key of the user we just updated
                    String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                            KEY_EMPLOYEE_ID, TABLE_EMPLOYEES, KEY_EMPLOYEE_USERNAME);
                    Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.Username)});
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
                    // user with this userName did not already exist, so insert new user
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
        public List<Shift> getAllShifts(Employee user) {
            List<Shift> posts = new ArrayList<>();

            // SELECT * FROM POSTS
            // LEFT OUTER JOIN USERS
            // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
            String POSTS_SELECT_QUERY =
                    String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
                            TABLE_POSTS,
                            TABLE_USERS,
                            TABLE_POSTS, KEY_POST_USER_ID_FK,
                            TABLE_USERS, KEY_USER_ID);

            // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
            // disk space scenarios)
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        User newUser = new User();
                        newUser.userName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                        newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL));

                        Post newPost = new Post();
                        newPost.text = cursor.getString(cursor.getColumnIndex(KEY_POST_TEXT));
                        newPost.user = newUser;
                        posts.add(newPost);
                    } while(cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.d(TAG, "Error while trying to get posts from database");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return posts;
        }

        // Update the user's profile picture url
        public int updateUserProfilePicture(User user) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);

            // Updating profile picture url for user with that userName
            return db.update(TABLE_USERS, values, KEY_USER_NAME + " = ?",
                    new String[] { String.valueOf(user.userName) });
        }

        // Delete all posts and users in the database
        public void deleteAllPostsAndUsers() {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try {
                // Order of deletions is important when foreign key relationships exist.
                db.delete(TABLE_POSTS, null, null);
                db.delete(TABLE_USERS, null, null);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d(TAG, "Error while trying to delete all posts and users");
            } finally {
                db.endTransaction();
            }
        }
}
