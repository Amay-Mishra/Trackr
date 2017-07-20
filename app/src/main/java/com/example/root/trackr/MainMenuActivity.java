package com.example.root.trackr;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity{

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
    private Switch switch_enable_tracking;
    private ListView listView_online_friends;
    private SharedPreferences sharedPreferencesProfileInformation;
    ArrayList<OnlineFriend> onlineFriends;
    private static OnlineFriendListAdapter onlineFriendListAdapter;
    //inflater
    LayoutInflater inflater;

    // Array of strings...
    String[] onlineFriendsArray = {"Debargha Bhattacharjee","Debojit Bhattacharjee","Amay Mishra","Debjyoti Pandit",
            "Hariom","Jagdish Kumar Verma","Amit Singh","Ravi Kumar"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);




        initialize();


        loadListView();

        addListenerForActionBar();

        addListenerForSwitch();

        addListenerForListView();


    }

    public void initialize() {
//        mDrawerList= (ListView) findViewById(R.id.navList);
        mToolbar= (Toolbar) findViewById(R.id.nav_action);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle= new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        textView_tracking_status = (TextView) findViewById(R.id.textViewTrackingStatus);
        textView_selected_friend = (TextView) findViewById(R.id.textViewSelectedFriend);


        sharedPreferencesProfileInformation = getSharedPreferences("ProfileInformation", Context.MODE_PRIVATE);

        switch_enable_tracking = (Switch) findViewById(R.id.switchEnableTracking);

//        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_online_friends, onlineFriendsArray);
        listView_online_friends = (ListView) findViewById(R.id.listViewOnlineFriends);
//        listView_online_friends.setAdapter(adapter);

        loadProfileInformation();
    }

    public void loadListView() {
        onlineFriends = new ArrayList<>();

        onlineFriends.add(new OnlineFriend(1, "Debargha"));
        onlineFriends.add(new OnlineFriend(2, "Debojit"));
        onlineFriends.add(new OnlineFriend(3, "Amay"));
        onlineFriends.add(new OnlineFriend(4, "Hariom"));
        onlineFriends.add(new OnlineFriend(5, "Jagdish"));

        onlineFriendListAdapter = new OnlineFriendListAdapter(MainMenuActivity.this, onlineFriends);

        listView_online_friends.setAdapter(onlineFriendListAdapter);
    }

    public void loadProfileInformation() {


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View header=navigationView.getHeaderView(0);
        textView_profile_id = (TextView)header.findViewById(R.id.textViewProfileId);
        textView_profile_name = (TextView)header.findViewById(R.id.textViewProfileName);
        textView_profile_phone= (TextView)header.findViewById(R.id.textViewProfilePhone);






        Gson gson = new Gson();
        String json = sharedPreferencesProfileInformation.getString("currentUser", "");
        User currentUser = gson.fromJson(json, User.class);

        textView_profile_id.setText(String.valueOf(currentUser.getId()));
        textView_profile_name.setText(currentUser.getFname() + " " + currentUser.getLname());
        textView_profile_phone.setText(currentUser.getPhone());

    }
    public void addListenerForSwitch() {
        switch_enable_tracking.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String trackingStatus;
                        if (isChecked) {
                            trackingStatus = getResources().getString(R.string.tracking_enabled);
                        }
                        else{
                            trackingStatus = getResources().getString(R.string.tracking_disabled);
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
                        OnlineFriend onlineFriend = onlineFriends.get(position);
                        String item = getString(R.string.selected_friend) + " " + onlineFriend.getName() + "\nID: " + onlineFriend.getId();
                        textView_selected_friend.setText(item);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void addDrawerItems() {
//        String[] osArray= { "h"};
//    }
}

