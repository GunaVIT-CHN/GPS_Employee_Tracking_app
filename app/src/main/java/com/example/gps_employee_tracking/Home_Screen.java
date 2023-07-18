package com.example.gps_employee_tracking;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gps_employee_tracking.home_fragmets.HomeFragment;
import com.example.gps_employee_tracking.home_fragmets.NewSheetFragment;
import com.example.gps_employee_tracking.home_fragmets.TimeSheetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_Screen extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        replace_fragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_frag:
                    replace_fragment(new HomeFragment());

                    break;
                case R.id.new_shift_frag:
                    replace_fragment(new NewSheetFragment());
                    break;
                case R.id.timesheet_frag:
                    replace_fragment(new TimeSheetFragment());
                    break;

            }
            setNavigationIconColor(item, R.color.bottom_nav_item_color);
            return false;
        });
    }

    private void replace_fragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void setNavigationIconColor(MenuItem item, int colorId) {
        Drawable icon = item.getIcon();
        if (icon != null) {
            if (item.isChecked()) {
                icon.setColorFilter(ContextCompat.getColor(this, R.color.buttontext), PorterDuff.Mode.SRC_IN);
            } else {
                icon.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_IN);
            }
        }
    }
}