package com.example.gps_employee_tracking;

import static com.example.gps_employee_tracking.R.id.back_btn_profile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Profile_view extends AppCompatActivity {

    Button logout_btn;
    TextView profile_name;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        Button leave_btn = findViewById(R.id.leave_button), sync_button = findViewById(R.id.sync_button),
                notification_btn = findViewById(R.id.notifications_button);
        logout_btn = findViewById(R.id.logout_btn);

        ImageButton back = findViewById(back_btn_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profile_name = findViewById(R.id.profile_name);

        String phoneNumber_user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        phoneNumber_user = phoneNumber_user.substring(3);

        // Initialize the Firebase Realtime Database reference
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        String finalPhoneNumber_user = phoneNumber_user;
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String phoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);
                    if (phoneNumber != null && phoneNumber.equals(finalPhoneNumber_user)) {
                        // Match found, retrieve the associated data
                        String userName = userSnapshot.child("firstName").getValue(String.class);
                        profile_name.setText(userName);

                        Toast.makeText(getApplicationContext(), "Welcome " + userName, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Toast.makeText(getApplicationContext(), "No data in DB", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong !", Toast.LENGTH_LONG).show();
            }
        });

        // Sync data
        sync_button.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Collecting Data", Toast.LENGTH_SHORT).show());

        // Notification
        notification_btn.setOnClickListener(v -> {
            // Raise intent to open the app's notification panel in system settings
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        });

        // Leave Button
        leave_btn.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Opening Leave Sheet", Toast.LENGTH_SHORT).show());

        // Logout
        logout_btn.setOnClickListener(v -> {
            // Clear the verification status
            SharedPrefManager.getInstance(Profile_view.this).setVerified(false);
            //logout DB
            FirebaseAuth.getInstance().signOut();
            //New Activity
            Intent intent = new Intent(Profile_view.this, Send_OTP.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finishAffinity();
        });

    }
}
