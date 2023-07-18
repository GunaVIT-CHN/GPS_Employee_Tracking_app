package com.example.gps_employee_tracking.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gps_employee_tracking.FB_Data_Uploaders.Sites_FirebaseUploader;
import com.example.gps_employee_tracking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<Sites_FirebaseUploader.Site> siteList;

    public CustomAdapter() {
        siteList = new ArrayList<>();
        loadSitesFromFirebase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_sites_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sites_FirebaseUploader.Site site = siteList.get(position);

        // Log the retrieved data
        Log.d("CustomAdapter", "Username: " + site.getUsername());
        Log.d("CustomAdapter", "Phone: " + site.getPhone());
        Log.d("CustomAdapter", "Role: " + site.getRole());
        Log.d("CustomAdapter", "Availability: " + site.getAvailability());
        Log.d("CustomAdapter", "Address: " + site.getAddress());
        Log.d("CustomAdapter", "City: " + site.getCity());
        Log.d("CustomAdapter", "State: " + site.getState());
        Log.d("CustomAdapter", "Latitude: " + site.getLatitude());
        Log.d("CustomAdapter", "Longitude: " + site.getLongitude());
        Log.d("CustomAdapter", "Timezone: " + site.getTimezone());
    }

    @Override
    public int getItemCount() {
        return siteList.size();
    }

    private void loadSitesFromFirebase() {
        DatabaseReference sitesRef = FirebaseDatabase.getInstance().getReference().child("DB_SITE_LIST");
        sitesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                siteList.clear();
                for (DataSnapshot siteSnapshot : dataSnapshot.getChildren()) {
                    Sites_FirebaseUploader.Site site = siteSnapshot.getValue(Sites_FirebaseUploader.Site.class);

                    if (site != null) {
                        Log.d("CustomAdapter", "Got");
                        siteList.add(site);
                    } else {
                        Log.d("CustomAdapter", "NULL: ");
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("CustomAdapter", "Failed to load sites from Firebase: " + databaseError.getMessage());
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

// This is Previous method to show the direction using android map intent from generated data

//package com.example.gps_employee_tracking.Adapters;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.gps_employee_tracking.R;
//
//import java.util.List;
//
//public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
//
//    private static List<String> items;
//    private static List<String> site_Phone_Numbers;
//    private static List<String> site_services;
//    private static List<String> site_cords;
//
//    public CustomAdapter(Context context, List<String> items, List<String> site_Phone_Numbers, List<String> site_services, List<String> site_cords) {
//        CustomAdapter.items = items;
//        CustomAdapter.site_Phone_Numbers = site_Phone_Numbers;
//        CustomAdapter.site_services = site_services;
//        CustomAdapter.site_cords = site_cords;
//    }
//
//    public void setData(List<String> siteNames, List<String> phoneNumbers, List<String> services, List<String> locationCords) {
//        items.clear();
//        site_Phone_Numbers.clear();
//        site_services.clear();
//        site_cords.clear();
//
//        //new data
//        items = siteNames;
//        this.site_Phone_Numbers = phoneNumbers;
//        this.site_services = services;
//        this.site_cords = locationCords;
//
//        notifyDataSetChanged();
//    }
//
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_sites_items, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.itemTextView.setText(items.get(position));
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
//
//        public TextView itemTextView;
//        public ImageButton imageButton;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            itemTextView = itemView.findViewById(R.id.itemTextView_sites);
//            imageButton = itemView.findViewById(R.id.item_opt);
//
//            imageButton.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            Log.d("catch", "onclick: " + getAdapterPosition());
//            overlay_menu(v);
//
//        }
//
//        private void overlay_menu(View view) {
//            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
//            popupMenu.inflate(R.menu.recycler_overlay_menu);
//            popupMenu.setOnMenuItemClickListener(this);
//            popupMenu.show();
//
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.overlay_info:
//                    String siteName = items.get(getAdapterPosition());
//                    String sitePhoneNumber = site_Phone_Numbers.get(getAdapterPosition());
//                    String siteService = site_services.get(getAdapterPosition());
//
//                    // Display the details in a dialog or a new activity
//                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
//                    builder.setTitle(siteName);
//                    builder.setMessage("Phone Number: " + sitePhoneNumber + "\nService: " + siteService);
//                    builder.setPositiveButton("OK", null);
//                    builder.show();
//                    return true;
//
//                case R.id.overlay_direction:
//                    Log.d("onMenuItem", "info available at direction " + getAdapterPosition());
//                    // Handle direction click for search string
////                    String location = items.get(getAdapterPosition()); // Location to be passed to Google Maps
////                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location);
////                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
////                    mapIntent.setPackage("com.google.android.apps.maps");
////                    if (mapIntent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
////                        itemView.getContext().startActivity(mapIntent);
////                    }
////                    else {
////                        Toast.makeText(itemView.getContext(), "Google Maps app not found!", Toast.LENGTH_SHORT).show();
////                    }
//                    String cords = site_cords.get(getAdapterPosition()); // get the coordinates for the current site
//                    String[] parts = cords.split(","); // split the coordinates into latitude and longitude
//                    double latitude = Double.parseDouble(parts[0]);
//                    double longitude = Double.parseDouble(parts[1]);
//                    Log.d("Location_tag", "Longitude " + longitude + " lattitude:" + latitude);
//                    // create a Uri for the Google Maps intent with the extracted coordinates
//                    Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
//                    // create an Intent to open the Google Maps app
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//
//                    // check if the Google Maps app is installed and start the activity
//                    if (mapIntent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
//                        itemView.getContext().startActivity(mapIntent);
//                    } else {
//                        Toast.makeText(itemView.getContext(), "Google Maps app not found!", Toast.LENGTH_SHORT).show();
//                    }
//                    return true;
//
//            }
//            return false;
//        }
//    }
//}
//