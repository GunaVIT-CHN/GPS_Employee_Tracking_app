package com.example.gps_employee_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gps_employee_tracking.Adapters.CustomAdapter;
import com.example.gps_employee_tracking.FB_Data_Uploaders.Sites_FirebaseUploader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sites_and_work extends AppCompatActivity {

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites_and_work);

        Button button = findViewById(R.id.save_btn_sites);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_sites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        CustomAdapter adapter = new CustomAdapter(this, generateData(), generatePN(), generateservices(), generateLocation_cord());
//        recyclerView.setAdapter(adapter);
        CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);


        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // back to home
        ImageButton back_hm = findViewById(R.id.back_home);

        back_hm.setOnClickListener(v -> {
            Intent intent = new Intent(sites_and_work.this, Home_Screen.class);
            startActivity(intent);
            finish();
        });

        // sync the sites to the RTDB
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sites_FirebaseUploader uploader = new Sites_FirebaseUploader();
                uploader.uploadData();
                Toast.makeText(sites_and_work.this, "Data synced successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    private List<String> generateData() {
//        List<String> data = new ArrayList<>();
//        data.add("Vit Chennai");
//        data.add("Ambattur OT");
//        data.add("Office");
//        data.add("Home");
//        return data;
//    }
//
//    private List<String> generatePN() {
//        List<String> data_PN = new ArrayList<>();
//        data_PN.add("1111111111");
//        data_PN.add("2222222222");
//        data_PN.add("3333333333");
//        data_PN.add("9999999999");
//        return data_PN;
//    }
//
//    private List<String> generateservices() {
//        List<String> data_Services = new ArrayList<>();
//        data_Services.add("Plumbing");
//        data_Services.add("Electrical");
//        data_Services.add("Plumbing");
//        data_Services.add("Home");
//        return data_Services;
//    }
//
//    private List<String> generateLocation_cord() {
//        List<String> data_Loc = new ArrayList<>();
//        data_Loc.add("12.847503013144635, 80.16166803600242");
//        data_Loc.add("13.119867332446761, 80.150092720911");
//        data_Loc.add("13.093833, 80.288987");
//        data_Loc.add("14.093833, 70.288987");
//        return data_Loc;
//    }
}
