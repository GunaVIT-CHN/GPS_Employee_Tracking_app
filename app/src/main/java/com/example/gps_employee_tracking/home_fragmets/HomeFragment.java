package com.example.gps_employee_tracking.home_fragmets;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gps_employee_tracking.Profile_view;
import com.example.gps_employee_tracking.R;
import com.example.gps_employee_tracking.TimerService;
import com.example.gps_employee_tracking.sites_and_work;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton event_and_sites;

    //time to start record
    private TextView timerTextView;
    private Handler handler;
    private boolean isTimerRunning;
    private TextView detail_punches_time;
    private BroadcastReceiver timerReceiver;
    private Date startTime;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Spinner spinner_sites = view.findViewById(R.id.site_spinner);
        Spinner spinner_task = view.findViewById(R.id.task_spinner);

        ImageButton button = view.findViewById(R.id.ham_menu_btn);

        Button punch_in_btn = view.findViewById(R.id.punch_in_button);
        TextView detail_punches = view.findViewById(R.id.textView3);
        detail_punches_time = view.findViewById(R.id.textView2);

//        // employee name setup
//        TextView name_text = view.findViewById(R.id.name_text);
//        String phoneNumber_user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
//        phoneNumber_user = phoneNumber_user.substring(3);
//
//        // Initialize the Firebase Realtime Database reference
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
//
//        String finalPhoneNumber_user = phoneNumber_user;
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    String phoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);
//                    if (phoneNumber != null && phoneNumber.equals(finalPhoneNumber_user)) {
//                        // Match found, retrieve the associated data
//                        String userName = userSnapshot.child("firstName").getValue(String.class);
//                        String userName2 = userSnapshot.child("lastName").getValue(String.class);
//                        String new_name = userName + " " + userName2;
//
//                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//                        usersRef.child("Name").setValue(new_name).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    // User added successfully
//                                    //Toast.makeText(getContext(), "User Name added to database", Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    // Failed to add user to database
//                                    Toast.makeText(getContext(), "Failed to add user name to database", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//                        name_text.setText(userName);
//
//                        Toast.makeText(getContext(), "Welcome " + userName, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//                Toast.makeText(getContext(), "No data in DB", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Something Went Wrong !", Toast.LENGTH_LONG).show();
//            }
//        });

//        List<String> site_items = new ArrayList<>();
//        site_items.add("Vit Chennai");
//        site_items.add("Ambattur OT");
//        site_items.add("Office");
//
//
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("DB_SITES").child("site_name");
//        DatabaseReference dbRef1 = FirebaseDatabase.getInstance().getReference().child("DB_SITES").child("services");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_spinner_item, site_items);
//
//        // Set the dropdown layout style for the adapter
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
////        // Set the adapter to the spinner
//        spinner_sites.setAdapter(adapter);
//
//        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<String> siteNames = new ArrayList<>();
//
//                for (DataSnapshot siteSnapshot : dataSnapshot.getChildren()) {
//                    String siteName = siteSnapshot.getValue(String.class);
//                    siteNames.add(siteName);
//                }
//
//                // Remove duplicates from the siteNames list
//                List<String> uniqueSiteNames = new ArrayList<>(new HashSet<>(siteNames));
//
//                // Now you have the unique site names in the uniqueSiteNames list
//                // You can use this list to populate the spinner or perform any other operation
//
//                // Clear the existing site_items list
//                site_items.clear();
//
//                // Add the unique site names to the site_items list
//                site_items.addAll(uniqueSiteNames);
//
//                // Notify the adapter that the data has changed
//                adapter.notifyDataSetChanged();
//                // For example, to log the unique site names
////                for (String siteName : uniqueSiteNames) {
////                    Log.d("Site Name", siteName);
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "Problem occurred with site_name",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Task
//        //-----------------------------------------
//        Spinner spinner_task = view.findViewById(R.id.task_spinner);
//        List<String> task_items = new ArrayList<>();
//        task_items.add("Electrical");
//        task_items.add("Plumbing");
//        task_items.add("Cleaner");
//
//        // Create an adapter for the spinner
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_spinner_item, task_items);
//
//        // Set the dropdown layout style for the adapter
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Set the adapter to the spinner
//        spinner_task.setAdapter(adapter1);
//
//        dbRef1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<String> siteServices = new ArrayList<>();
//
//                for (DataSnapshot siteSnapshot : dataSnapshot.getChildren()) {
//                    String siteservc = siteSnapshot.getValue(String.class);
//                    siteServices.add(siteservc);
//                }
//
//                // Remove duplicates from the siteNames list
//                List<String> uniqueSiteservc = new ArrayList<>(new HashSet<>(siteServices));
//
//                // Now you have the unique site names in the uniqueSiteNames list
//                // You can use this list to populate the spinner or perform any other operation
//
//                // Clear the existing site_items list
//                task_items.clear();
//
//                // Add the unique site names to the site_items list
//                task_items.addAll(uniqueSiteservc);
//
//                // Notify the adapter that the data has changed
//                adapter1.notifyDataSetChanged();
//                // For example, to log the unique site names
////                for (String siteName : uniqueSiteNames) {
////                    Log.d("Site Name", siteName);
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "Problem occured with site_name",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });


        //New Data
        // Spinner adapters
        ArrayList<String> siteNamesList = new ArrayList<>();
        ArrayList<String> siteServicesList = new ArrayList<>();

        ArrayAdapter<String> siteAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, siteNamesList);
        siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sites.setAdapter(siteAdapter);

        ArrayAdapter<String> taskAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, siteServicesList);
        taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_task.setAdapter(taskAdapter);

        // Retrieve site names from the database
        DatabaseReference siteRef = FirebaseDatabase.getInstance().getReference().child("DB_SITES");
        siteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                siteNamesList.clear();

                for (DataSnapshot siteSnapshot : dataSnapshot.getChildren()) {
                    String siteName = siteSnapshot.child("site_name").getValue(String.class);
                    if (siteName != null) {
                        siteNamesList.add(siteName);
                        Log.d("siteNames", "" + siteName);
                    }
                }

                siteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch site names", Toast.LENGTH_SHORT).show();
            }
        });

// Retrieve site services from the database
        DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference().child("DB_SITES");
        serviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                siteServicesList.clear();

                for (DataSnapshot siteSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot serviceSnapshot : siteSnapshot.child("service_item").getChildren()) {
                        String serviceName = serviceSnapshot.getValue(String.class);
                        if (serviceName != null) {
                            siteServicesList.add(serviceName);
                            Log.d("siteServices", "" + serviceName);
                        }
                    }
                }

                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch site services", Toast.LENGTH_SHORT).show();
            }
        });

        // image button for events activity
        event_and_sites = view.findViewById(R.id.sites_img_btn);

        event_and_sites.setOnClickListener(v -> {
            // Create a new Intent object to start the new activity
            Intent intent = new Intent(getActivity(), sites_and_work.class);
            startActivity(intent);

            // Display a toast message with haptic feedback
            Toast.makeText(getActivity(), "Loading sites available", Toast.LENGTH_SHORT).show();

            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Raise a menu on profile
                Intent intent = new Intent(getActivity(), Profile_view.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "Loading Your Profile", Toast.LENGTH_SHORT).show();
            }
        });

        //DAY to Day Timer
        boolean val = checkTimerServiceStatus();

        if (val) {
            punch_in_btn.setText("Punch-Out");
            detail_punches.setText("Punched-In");
            int color = 0xFF00FF00; // Green color
            detail_punches.setTextColor(color);
        } else {
            punch_in_btn.setText("Punch-In");
            detail_punches.setText("Punched-Out");
            int color = 0xFFFF0000; // Red color
            detail_punches.setTextColor(color);
        }

        timerTextView = view.findViewById(R.id.timer_textview);
        handler = new Handler();
        isTimerRunning = false;

        punch_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    // Pause the timer
                    isTimerRunning = false;
                    punch_in_btn.setText("Punch-In");
                    detail_punches.setText("Punched-Out");
                    int color = 0xFFFF0000; // Red color
                    detail_punches.setTextColor(color);
                    stopTimerService();
                } else {
                    // Start the timer
                    startTime = new Date();
                    isTimerRunning = true;
                    punch_in_btn.setText("Punch-Out");
                    detail_punches.setText("Punched-In");
                    int color = 0xFF00FF00; // Green color
                    detail_punches.setTextColor(color);
                    startTimerService();
                }
            }
        });


        return view;
    }

    private void startTimerService() {
        Intent intent = new Intent(getActivity(), TimerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(intent);
        } else {
            requireActivity().startService(intent);
        }
    }

    //    private void stopTimerService() {
//        Intent intent = new Intent(getActivity(), TimerService.class);
//        requireActivity().stopService(intent);
//    }
    private void stopTimerService() {
        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date());

        // Update the RTDB with the stop time and start time
        DatabaseReference userWorkDataRef = FirebaseDatabase.getInstance().getReference().child("User_Work_data")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child(getCurrentDate())
                .child(currentTime);
        userWorkDataRef.child("time_stopped").setValue(currentTime);
        userWorkDataRef.child("time_started").setValue(getStartTime());

        // Stop the TimerService
        Intent intent = new Intent(getActivity(), TimerService.class);
        requireActivity().stopService(intent);
    }


    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }


    @Override
    public void onResume() {
        super.onResume();

        // Register a broadcast receiver to receive timer updates from the service
        timerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int hours = intent.getIntExtra("hours", 0);
                int minutes = intent.getIntExtra("minutes", 0);
                int seconds = intent.getIntExtra("seconds", 0);

                // Update the UI with the received timer values
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
                timerTextView.setText(time);
            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(timerReceiver, new IntentFilter("TIMER_UPDATED"));
    }

    @Override
    public void onPause() {
        super.onPause();

        // Unregister the broadcast receiver
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(timerReceiver);
    }

    private boolean isTimerServiceRunning() {
        ActivityManager activityManager = (ActivityManager) requireContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            // Get a list of running services
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                // Check if the TimerService class is present in the list
                if (TimerService.class.getName().equals(service.service.getClassName())) {
                    return true; // TimerService is running
                }
            }
        }
        return false; // TimerService is not running
    }

    private boolean checkTimerServiceStatus() {
        return isTimerServiceRunning();
    }

    private String getStartTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(startTime);
    }

}