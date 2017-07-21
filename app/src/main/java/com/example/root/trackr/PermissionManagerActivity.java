package com.example.root.trackr;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PermissionManagerActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView textView_selected_friend_permission;
    private Button button_change_permission_status;
    //inflater
    Inflater inflater;

    private ListView listView_search_friend;
    private ListView listView_friend_list;
    ArrayList<FriendList> friendLists = new ArrayList<>();
    private static FriendListListAdapter friendListListAdapter;

    String name, id;

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

        loadFriendList();
//


        addListenerForActionBar();


        addListenerForListView();

//        addListenerForSwitch();
//
//        addListenerForListView();


    }

    public void initialize() {
//        mDrawerList= (ListView) findViewById(R.id.navList);
        mToolbar= (Toolbar) findViewById(R.id.nav_action);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle= new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        textView_selected_friend_permission = (TextView) findViewById(R.id.textViewSelectedFriendPermission);
        button_change_permission_status = (Button) findViewById(R.id.buttonChangePermissionStatus);

        ArrayAdapter adapter1 = new ArrayAdapter<String>(this, R.layout.list_search_friend, searchFriendArray);
        listView_search_friend = (ListView) findViewById(R.id.listViewSearchFriend);
        listView_search_friend.setAdapter(adapter1);

        listView_friend_list = (ListView) findViewById(R.id.listViewFriendList);


    }

    public void loadFriendList() {
        friendLists = new ArrayList<>();

        friendLists.add(new FriendList(1, "Debargha Bhattacharjee", 1));
        friendLists.add(new FriendList(2, "Debojit Bhattacharjee", 0));
        friendLists.add(new FriendList(3, "Amay Mishra", 1));
        friendLists.add(new FriendList(4, "Hariom", 1));
        friendLists.add(new FriendList(5, "Jagdish Kumar Verma", 1));
        friendLists.add(new FriendList(6, "Debjyoti Pandit", 0));
        friendLists.add(new FriendList(7, "Ravi Teja", 0));

        friendListListAdapter= new FriendListListAdapter(PermissionManagerActivity.this, friendLists);

        listView_friend_list.setAdapter(friendListListAdapter);

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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos = listView_friend_list.getPositionForView(buttonView);
        if(pos != ListView.INVALID_POSITION) {
            FriendList friendListBackup = friendLists.get(pos);
            Log.d("STATUS CHANGED", String.valueOf(friendListBackup.getPermissionStatus()));
            if(friendListBackup.getPermissionStatus() == 1) {
                friendListBackup.setPermissionStatus(1);
                Log.d("STATUS CHANGED", String.valueOf(friendListBackup.getPermissionStatus()) + " " + friendListBackup.getName());

            } else {
                friendListBackup.setPermissionStatus(0);
                Log.d("STATUS CHANGED", String.valueOf(friendListBackup.getPermissionStatus()) + " " + friendListBackup.getName());

            }
        }
    }

    public void addListenerForListView() {
        listView_friend_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FriendList friendList = friendLists.get(position);
                        String item = getString(R.string.selected) + " " + friendList.getName() + "\nID: " + friendList.getId();
                        textView_selected_friend_permission.setText(item);
                    }
                }
        );
    }

    public void addListenerForButton() {
        button_change_permission_status.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
    }


//    private void addDrawerItems() {
//        String[] osArray= { "h"};
//    }
}

