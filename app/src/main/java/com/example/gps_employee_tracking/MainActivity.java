package com.example.gps_employee_tracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ViewPager mSlideiewpager;
    LinearLayout mDotlayout;
    Button backbtn,nextbtn,skipbtn;
    TextView[] dots;
    ViewPagerAdapter viewPagerAdapter;

    ImageView elp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (SharedPrefManager.getInstance(this).isVerified()) {
            Intent intent = new Intent(MainActivity.this, Home_Screen.class);
            startActivity(intent);
            finish();
        } else {



        backbtn = findViewById(R.id.onboard_back);
        nextbtn = findViewById(R.id.onboard_next);
        skipbtn = findViewById(R.id.skipbutton);
        elp1 = findViewById(R.id.ellipse1);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getintem(0) > 0) {
                    mSlideiewpager.setCurrentItem(getintem(-1), true);
                }
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getintem(0) < 3) {
                    mSlideiewpager.setCurrentItem(getintem(1), true);
                } else {
                    Intent i = new Intent(MainActivity.this, Send_OTP.class);
                    startActivity(i);
                    finish();
                }

            }
        });

        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(50); // Vibrate for 50 milliseconds

                Intent i = new Intent(MainActivity.this, Send_OTP.class);
                startActivity(i);
                finish();
            }
        });

        mSlideiewpager = (ViewPager) findViewById(R.id.slideviewpager);
        mDotlayout = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);
        mSlideiewpager.setAdapter(viewPagerAdapter);

        setupIndicator(0);
        mSlideiewpager.addOnPageChangeListener(viewListener);
    }
    }

    public void setupIndicator(int position){
        dots = new TextView[4];
        mDotlayout.removeAllViews();

        for (int i=0;i <dots.length;i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            mDotlayout.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setupIndicator(position);

            if(position > 0)
            {
                backbtn.setVisibility(View.VISIBLE);
                elp1.setVisibility(View.VISIBLE);
            }
            else
            {
                backbtn.setVisibility(View.INVISIBLE);
                elp1.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getintem(int i){
        return mSlideiewpager.getCurrentItem() + i;
    }
}