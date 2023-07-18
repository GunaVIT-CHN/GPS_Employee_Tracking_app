package com.example.gps_employee_tracking.home_fragmets;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.gps_employee_tracking.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class TimeSheetFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private GoogleMap mMap;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final long LOCATION_UPDATE_INTERVAL = 40 * 1000; // 40 seconds

    private Handler locationUpdateHandler;
    private Runnable locationUpdateRunnable;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseAuth mAuth;
    private DatabaseReference locationRef;

    public TimeSheetFragment() {
        // Required empty public constructor
    }

    public static TimeSheetFragment newInstance(String param1, String param2) {
        TimeSheetFragment fragment = new TimeSheetFragment();
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

        mAuth = FirebaseAuth.getInstance();
        locationRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time_sheet, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.maps);
        if (mapFragment == null) {
            Log.d("Maps", "MapFragment is null");
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.maps, mapFragment, "map_fragment")
                    .commit();
            getChildFragmentManager().executePendingTransactions();
        }
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            locationUpdateHandler = new Handler();
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
            startLocationUpdates();
        } else {
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_CODE);
    }

    private void startLocationUpdates() {
        if (!checkLocationPermission()) {
            requestLocationPermission();
            return;
        }

        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        }

        locationUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateCurrentLocation();
                locationUpdateHandler.postDelayed(this, LOCATION_UPDATE_INTERVAL);
            }
        };

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    Toast.makeText(getContext(), "Viewing the Current Location", Toast.LENGTH_SHORT).show();

                    // Save the location to Firebase Realtime Database
                    saveLocationToFirebase(location);

                    // Show a message to indicate that the data is updating
                    Toast.makeText(getContext(), "Location data updated", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Maps", "Last location is null");
                }
                locationUpdateHandler.postDelayed(locationUpdateRunnable, LOCATION_UPDATE_INTERVAL);
            }
        });
    }

    private void stopLocationUpdates() {
        locationUpdateHandler.removeCallbacks(locationUpdateRunnable);
    }

    private void updateCurrentLocation() {
        if (!checkLocationPermission()) {
            requestLocationPermission();
            return;
        }

        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocation));
                } else {
                    Log.d("Maps", "Last location is null");
                }
            }
        });
    }

    private void saveLocationToFirebase(Location location) {
        String userId = mAuth.getCurrentUser().getUid();
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());

        // Create a map of location data
        Map<String, Object> locationData = new HashMap<>();
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);
        String LiveLoc = longitude + "," + latitude;
        // Save the location data to the database
        locationRef.child("Live Location").setValue(LiveLoc);
    }

    private void Update_On_Map() {

//        String phoneNumber_user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
//        phoneNumber_user = phoneNumber_user.substring(3);
//        // Initialize the Firebase Realtime Database reference
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
//        String finalPhoneNumber_user = phoneNumber_user;
//
//        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    String phoneNumber = userSnapshot.child("phoneNumber").getValue(String.class);
//                    if (phoneNumber != null && phoneNumber.equals(finalPhoneNumber_user)) {
//                        // Match found, retrieve the associated data
//                        String longitude = userSnapshot.child("firstName").getValue(String.class);
//                        String lattitude = userSnapshot.child("lastName").getValue(String.class);
//
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

    }
}
