# TUD-Mobile-Software-Dev-ClockingSystem-Assignment

Student Name: Shane Buckley
Student ID: C20703429
Submission Date: 04/12/2022
Description: Clocking app to record an employees arrival and departure times to/from work.


## Class Listing

###### Employee Class
Description: A class to handle user/employee attributes and methods.

This java class has the following attributes:
ID, Name, Username, Password, Email, isClockedIn.

And the following methods:
A constructor, getters and setters.

###### Shift Class
Description: A class to handle shift attributes and methods.

This java class has the following attributes:
ID, Context, Employee, startTime, endTime.

And the following methods:
A constructor, getters and setters, a toString method, an addShift method, an updateShift method, a calculateTime method, and a timeDifference method.


###### Login Activity Class
Description: A class to display the login activity. Simple login UI.

This java class has no attributes.

And the following methods:
A constructor to get context and an onCreate method.

###### Main Activity Class
Description: A class to display the main activity. This is the main homepage for a user after they login.

This java class has no attributes.

And the following methods:
A constructor to get context and an onCreate method.

###### My Shifts Activity Class
Description: A class to display the MyShifts activity. Shows a list of all the users recorded shifts.

This java class has no attributes.

And the following methods:
A constructor to get context and an onCreate method.

This has a class for a custom arrayAdapter to display an arraylist of shift objects.

###### Database Helper Class
Description: A class that extends the SQLiteOpenHelper class. Handles all interactions with the databse.

This java class has attributes for:
Error tag, Database Info, Table names, Shifts table columns and employees table columns.

And the following methods:
A constructor to get context, an onConfigure method, an onCreate method (to create db tables), onUpgrade method (to recreate tables if db version changes), an addShift method, an updateShift method, an addOrUpdateUser method, a getAllShifts method (returns arraylist), a loginUser method, a getUser method (returns employee object), an updateUserPassword method, a deleteAllShifts method, and a deleteAccount method.


###### Register Activity Class
Description: A class to display the registration activity. Contains a registration form.

This java class has no attributes.

And the following methods:
A constructor to get context and an onCreate method.


###### Setttings Activity Class
Description: A class to display the settings activity. Settings include delete all shifts and delete account.

This java class has no attributes.

And the following methods:
A constructor to get context and an onCreate method.

