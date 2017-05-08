package com.example.root.trackr;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainMenuActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initialize();
    }

    public void initialize() {
//        mDrawerList= (ListView) findViewById(R.id.navList);
    }

    private void addDrawerItems() {
        String[] osArray= { "h"};
    }
}
