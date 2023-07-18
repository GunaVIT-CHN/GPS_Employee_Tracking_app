package com.example.gps_employee_tracking;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sync_Data {

    public static void populateUserData() {
        // Get the Firebase database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Create a "users" node in the database
        DatabaseReference usersRef = databaseRef.child("users");

        // Populate the user data
        populateUser(usersRef, 1, "johnsmith12", "John", "Smith", "johnsmith@example.com", "1234567890", 37.1234, -122.1234, "Hourly", "OT Hourly");
        populateUser(usersRef, 2, "sarahbrown45", "Sarah", "Brown", "sarahbrown@example.com", "9876543210", 34.5678, -118.5678, "Daily", "OT Hourly");
        populateUser(usersRef, 3, "davidjones78", "David", "Jones", "davidjones@example.com", "5551234567", 39.8765, -121.8765, "Hourly", "OT Hourly");
        populateUser(usersRef, 4, "emilywilson23", "Emily", "Wilson", "emilywilson@example.com", "2223334444", 38.9876, -120.9876, "Monthly", "OT Hourly");
        populateUser(usersRef, 5, "michaeljohnson", "Michael", "Johnson", "michaeljohnson@example.com", "7778889999", 36.5432, -119.5432, "Hourly", "OT Hourly");
        populateUser(usersRef, 6, "jennyparker56", "Jenny", "Parker", "jennyparker@example.com", "4445556666", 35.6789, -117.6789, "Daily", "OT Hourly");
        populateUser(usersRef, 7, "matthewwright", "Matthew", "Wright", "matthewwright@example.com", "1112223333", 33.2109, -116.2109, "Monthly", "OT Hourly");
        populateUser(usersRef, 8, "lisasmith32", "Lisa", "Smith", "lisasmith@example.com", "6667778888", 32.1098, -115.1098, "Hourly", "OT Hourly");
        populateUser(usersRef, 9, "alexturner21", "Alex", "Turner", "alexturner@example.com", "9998887777", 30.5432, -114.5432, "Daily", "OT Hourly");
        populateUser(usersRef, 10, "amycampbell", "Amy", "Campbell", "amycampbell@example.com", "3334445555", 28.7654, -112.7654, "Monthly", "OT Hourly");
    }

    private static void populateUser(DatabaseReference usersRef, int userId, String username, String firstName, String lastName, String email, String phoneNumber, double latitude, double longitude, String payroll, String overtimePayroll) {
        // Create a user object
        User user = new User(phoneNumber, username, firstName, lastName, email, userId, latitude, longitude, payroll, overtimePayroll);

        // Set the user data in the "users" node with the user ID as the key
        usersRef.child(String.valueOf(userId)).setValue(user);
    }

}
