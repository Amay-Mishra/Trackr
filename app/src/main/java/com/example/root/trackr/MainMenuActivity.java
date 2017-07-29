package com.example.root.trackr;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenuActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView textView_tracking_status;
    private TextView textView_selected_friend;
    private TextView textView_profile_name;
    private TextView textView_profile_id;
    private TextView textView_profile_phone;
    private SessionManager session;
    private Switch switch_enable_tracking;
    private final String online_friends_url = AppConfig.ONLINE_FRIENDS_URL;
    private ListView listView_online_friends;
    private SharedPreferences sharedPreferencesProfileInformation;
    ArrayList<OnlineFriend> onlineFriends;
    private static OnlineFriendListAdapter onlineFriendListAdapter;
    private User currentUser;




    OnlineFriend onlineFriend;

    Location location;
    LocationManager locationManager;
    int noOfRetries;
    //inflater
    LayoutInflater inflater;

    // Array of strings...
    String[] onlineFriendsArray = {"Debargha Bhattacharjee", "Debojit Bhattacharjee", "Amay Mishra", "Debjyoti Pandit",
            "Hariom", "Jagdish Kumar Verma", "Amit Singh", "Ravi Kumar"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        initialize();


        loadListView();

        addListenerForActionBar();

        addListenerForSwitch();

        addListenerForListView();

        addListenerForNavigationView();


    }

    public void initialize() {
//        mDrawerList= (ListView) findViewById(R.id.navList);
        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        textView_tracking_status = (TextView) findViewById(R.id.textViewTrackingStatus);
        textView_selected_friend = (TextView) findViewById(R.id.textViewSelectedFriend);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        sharedPreferencesProfileInformation = getSharedPreferences("ProfileInformation", Context.MODE_PRIVATE);

        switch_enable_tracking = (Switch) findViewById(R.id.switchEnableTracking);

//        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_online_friends, onlineFriendsArray);
        listView_online_friends = (ListView) findViewById(R.id.listViewOnlineFriends);
//        listView_online_friends.setAdapter(adapter);

        loadProfileInformation();


        noOfRetries = 0;
    }

    public void loadListView() {
        onlineFriends = new ArrayList<>();
        Gson gson = new Gson();
        String postUser = gson.toJson(currentUser, User.class);
        Log.d("JSON REQUEST", postUser);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.POST,
                online_friends_url,
                postUser,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            Gson gson = new Gson();
                            OnlineFriend onlineFriend;
                            Log.d("JSON RESPONSE", jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("JSON RESPONSE", jsonArray.getJSONObject(i).toString());
                                onlineFriend = gson.fromJson(jsonArray.getJSONObject(i).toString(), OnlineFriend.class);
                                onlineFriends.add(onlineFriend);
                            }
                            onlineFriendListAdapter = new OnlineFriendListAdapter(MainMenuActivity.this, onlineFriends);
                            listView_online_friends.setAdapter(onlineFriendListAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(MainMenuActivity.this, "Can't fetch online friends, try again later.");
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                return headers;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        MySingleton.getInstance(MainMenuActivity.this).addToRequestQueue(jsonArrayRequest);


    }

    public void loadProfileInformation() {


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View header = navigationView.getHeaderView(0);
        textView_profile_id = (TextView) header.findViewById(R.id.textViewProfileId);
        textView_profile_name = (TextView) header.findViewById(R.id.textViewProfileName);
        textView_profile_phone = (TextView) header.findViewById(R.id.textViewProfilePhone);

        Gson gson = new Gson();
        String json = sharedPreferencesProfileInformation.getString("currentUser", "");
        currentUser = gson.fromJson(json, User.class);

        textView_profile_id.setText(String.valueOf(currentUser.getId()));
        textView_profile_name.setText(currentUser.getFname() + " " + currentUser.getLname());
        textView_profile_phone.setText(currentUser.getPhone());

    }

    public void addListenerForNavigationView() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_permission_manager:
                                Intent intent = new Intent("com.example.root.trackr.PermissionManagerActivity");
                                startActivity(intent);
                                break;
                            case R.id.nav_main_menu:
                                intent = new Intent("com.example.root.trackr.MainMenuActivity");
                                startActivity(intent);
                                break;
                            case R.id.nav_logout:
                                AlertDialog.Builder logoutAlertBuilder = new AlertDialog.Builder(MainMenuActivity.this);
                                logoutAlertBuilder.setMessage("Do you want to logout and Exit?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SharedPreferences.Editor editor = sharedPreferencesProfileInformation.edit();
                                                editor.clear();
                                                editor.commit();
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog logoutAlert = logoutAlertBuilder.create();
                                logoutAlert.setTitle("Logout & Exit!!!");
                                logoutAlert.show();
                                break;
                        }

                        return false;
                    }
                }
        );

    }

    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void addListenerForSwitch() {
        switch_enable_tracking.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String trackingStatus;
                        if (isChecked) {
                            trackingStatus = getResources().getString(R.string.tracking_enabled);
                            startTracking();
                        } else {
                            trackingStatus = getResources().getString(R.string.tracking_disabled);
                            stopTrackng();
                        }
                        textView_tracking_status.setText(trackingStatus);
                    }
                }
        );
    }


    public void addListenerForActionBar() {
        setSupportActionBar(mToolbar);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addListenerForListView() {
        listView_online_friends.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onlineFriend = onlineFriends.get(position);
                        String item = getString(R.string.selected_friend) + " " + onlineFriend.getName() + "\nID: " + onlineFriend.getId();
                        textView_selected_friend.setText(item);
                    }
                }
        );
    }



    public void track(View view){
        Intent intent = new Intent(MainMenuActivity.this,TrackFriend.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id",onlineFriend.getId());
        Log.d("bundle", String.valueOf(bundle));
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void addDrawerItems() {
//        String[] osArray= { "h"};
//    }


    public void startTracking() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        callAsynchronousTask();
    }

    public void stopTrackng() {
        locationManager.removeUpdates(locationListener);
        //TODO kill async task
    }

    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            upload();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000);
    }

    private void upload() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            noOfRetries++;
            if(noOfRetries>3){
                //TODO dialog box prompting user to disable tracking
            }
            Toast.makeText(MainMenuActivity.this, "location not available", Toast.LENGTH_SHORT).show();
            Log.d("No location recieved", "it was in vain");
        } else {
            noOfRetries = 0;
            Log.d("upload", "null");
            Toast.makeText(MainMenuActivity.this, "latitude: "+String.valueOf(location.getLatitude())+" longitude : "+String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();
            Log.d("lats", String.valueOf(location.getLatitude()));
            Log.d("longs", String.valueOf(location.getLongitude()));
            {
                Gson gson = new Gson();
                String json = sharedPreferencesProfileInformation.getString("currentUser", "");
                currentUser = gson.fromJson(json, User.class);
                CustomLocation customLocation = new CustomLocation(String.valueOf(location.getLatitude()),currentUser.getId(),String.valueOf(location.getLongitude()));
                String postUser = gson.toJson(customLocation, CustomLocation.class);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        AppConfig.MAP_URL_GET,
                        postUser,
                        new Response.Listener<JSONObject>() {
                            Location location;
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    Log.d("RESPONSE", jsonObject.toString());
                                    //TODO parse response
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainMenuActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        super.getHeaders();
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("charset", "utf-8");
                        return headers;
                    }
                };

                int socketTimeout = 10000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                jsonObjectRequest.setRetryPolicy(policy);
                MySingleton.getInstance(MainMenuActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
//            upload(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}

