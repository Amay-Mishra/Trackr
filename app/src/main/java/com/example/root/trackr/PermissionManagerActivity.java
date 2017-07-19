package com.example.root.trackr;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class PermissionManagerActivity extends AppCompatActivity{

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private ListView listView_search_friend;
    private ListView listView_friend_list;

    // Array of strings...
    String[] searchFriendArray = {"Debargha Bhattacharjee","Debojit Bhattacharjee","Amay Mishra","Debjyoti Pandit",
            "Hariom","Jagdish Kumar Verma","Amit Singh","Ravi Kumar"};
    String[] friendListArray = {"Debargha Bhattacharjee","Debojit Bhattacharjee","Amay Mishra","Debjyoti Pandit",
            "Hariom","Jagdish Kumar Verma","Amit Singh","Ravi Kumar"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_manager);

        initialize();

        addListenerForActionBar();

//        addListenerForSwitch();
//
//        addListenerForListView();


    }

    public void initialize() {
//        mDrawerList= (ListView) findViewById(R.id.navList);
        mToolbar= (Toolbar) findViewById(R.id.nav_action);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle= new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);


        ArrayAdapter adapter1 = new ArrayAdapter<String>(this, R.layout.list_search_friend, searchFriendArray);
        listView_search_friend = (ListView) findViewById(R.id.listViewSearchFriend);
        listView_search_friend.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.list_friend_list, friendListArray);
        listView_friend_list = (ListView) findViewById(R.id.listViewFriendList);
        listView_friend_list.setAdapter(adapter2);

    }



    public void addListenerForActionBar() {
        setSupportActionBar(mToolbar);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    public void addListenerForListView() {
//        listView_search_friend.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String item = getString(R.string.selected_friend) + " " + (String) parent.getItemAtPosition(position);
//                        textView_selected_friend.setText(item);
//                    }
//                }
//        );
//    }

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

