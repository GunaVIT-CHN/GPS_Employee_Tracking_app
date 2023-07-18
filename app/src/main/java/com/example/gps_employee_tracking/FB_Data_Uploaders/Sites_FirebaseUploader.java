package com.example.gps_employee_tracking.FB_Data_Uploaders;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sites_FirebaseUploader {
    // Class which updates the Database with new Site Names
    public void uploadData() {
        DatabaseReference siteListRef = FirebaseDatabase.getInstance().getReference().child("DB_SITE_LIST");

        siteListRef.child("1").setValue(new Site("johnsmith12", "1234567890", "Electricity", "Yes",
                "Vellachiri Road", "Chennai", "TN", 12.847503013144635, 80.16166803600242, "GMT+5:30"));
        siteListRef.child("2").setValue(new Site("sarahbrown45", "9876543210", "Plumbing", "No",
                "Kelambakkam", "Chennai", "TN", 13.093833, 80.288987, "GMT+5:30"));
        siteListRef.child("3").setValue(new Site("davidjones78", "5551234567", "Technician", "Yes",
                "Indira Gandhi Marg", "Lucknow", "UP", 13.093833, 80.288987, "GMT+5:30"));
        siteListRef.child("4").setValue(new Site("emilywilson23", "2223334444", "Insulation", "Yes",
                "Vellachiri Road", "Chennai", "TN", 14.093833, 70.288987, "GMT+5:30"));
        siteListRef.child("5").setValue(new Site("michaeljohnson", "7778889999", "Plumbing", "Yes",
                "Kelambakkam", "Chennai", "TN", 12.847503013144635, 80.16166803600242, "GMT+5:30"));
        siteListRef.child("6").setValue(new Site("jennyparker56", "4445556666", "Technician", "Yes",
                "Vandalur", "Chennai", "TN", 14.093833, 70.288987, "GMT+5:30"));
        siteListRef.child("7").setValue(new Site("matthewwright", "1112223333", "Security", "No",
                "Vellachiri Road", "Chennai", "TN", 14.093833, 70.288987, "GMT+5:30"));
        siteListRef.child("8").setValue(new Site("lisasmith32", "6667778888", "Plumbing", "No",
                "Kelambakkam", "Chennai", "TN", 12.847503013144635, 80.16166803600242, "GMT+5:30"));
        siteListRef.child("9").setValue(new Site("alexturner21", "9998887777", "Technician", "Yes",
                "Indira Gandhi Marg", "Lucknow", "UP", 13.093833, 80.288987, "GMT+5:30"));
        siteListRef.child("10").setValue(new Site("amycampbell", "3334445555", "Community Organiser", "No",
                "Vandalur", "Chennai", "TN", 14.093833, 70.288987, "GMT+5:30"));
    }

    public static class Site {
        private String username;
        private String phone;
        private String role;
        private String availability;
        private String address;
        private String city;
        private String state;
        private double latitude;
        private double longitude;
        private String timezone;

        public Site() {
        }

        public Site(String username, String phone, String role, String availability, String address, String city,
                    String state, double latitude, double longitude, String timezone) {
            this.username = username;
            this.phone = phone;
            this.role = role;
            this.availability = availability;
            this.address = address;
            this.city = city;
            this.state = state;
            this.latitude = latitude;
            this.longitude = longitude;
            this.timezone = timezone;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getAvailability() {
            return availability;
        }

        public void setAvailability(String availability) {
            this.availability = availability;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }
}
