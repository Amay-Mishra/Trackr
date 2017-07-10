package com.example.root.trackr;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView textView_tracking_status;
    private Switch switch_enable_tracking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initialize();

        addListenerForActionBar();

        addListenerForSwitch();
    }

    public void initialize() {
//        mDrawerList= (ListView) findViewById(R.id.navList);
        mToolbar= (Toolbar) findViewById(R.id.nav_action);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle= new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        textView_tracking_status = (TextView) findViewById(R.id.textViewTrackingStatus);
        switch_enable_tracking = (Switch) findViewById(R.id.switchEnableTracking);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDrawerItems() {
        String[] osArray= { "h"};
    }
}
