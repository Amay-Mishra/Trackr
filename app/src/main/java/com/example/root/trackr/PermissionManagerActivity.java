package com.example.root.trackr;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

public class PermissionManagerActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;
    private TextView textView_selected_friend_permission;
    private Button button_change_permission_status;
    private Button button_search_friend;
    private SharedPreferences sharedPreferencesProfileInformation;
    private TextView textView_profile_name;
    private TextView textView_profile_id;
    private TextView textView_profile_phone;
    private EditText editText_search_friend;
    private final String permission_list_url= AppConfig.PERMISSION_LIST_URL;
    private final String change_status_url= AppConfig.CHANGE_STATUS_URL;
    private final String search_friend_url= AppConfig.SEARCH_FRIEND;
    private final String add_friend_url = AppConfig.ADD_FRIEND;
    private User currentUser;

    String userId;
    String authToken;
    //inflater
    Inflater inflater;

    private ListView listView_search_friend;
    private ListView listView_friend_list;
    ArrayList<FriendList> friendLists = new ArrayList<>();
    ArrayList<User> searchFriends = new ArrayList<>();
    private static FriendListListAdapter friendListListAdapter;
    private static SearchFriendListAdapter searchFriendListAdapter;

   int friendPermissionStatus, friendId;

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


        addListenerForActionBar();


        addListenerForListView();

        addListenerForButton();

        addListenerForNavigationView();

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
        button_search_friend = (Button) findViewById(R.id.buttonSearchFriend);
        editText_search_friend =(EditText) findViewById(R.id.editTextSearchFriend);

        sharedPreferencesProfileInformation = getSharedPreferences("ProfileInformation", Context.MODE_PRIVATE);

        loadProfileInformation();


        listView_search_friend = (ListView) findViewById(R.id.listViewSearchFriend);


        listView_friend_list = (ListView) findViewById(R.id.listViewFriendList);


    }

    public void loadProfileInformation() {


        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View header=navigationView.getHeaderView(0);
        textView_profile_id = (TextView)header.findViewById(R.id.textViewProfileId);
        textView_profile_name = (TextView)header.findViewById(R.id.textViewProfileName);
        textView_profile_phone= (TextView)header.findViewById(R.id.textViewProfilePhone);

        Gson gson = new Gson();
        String json = sharedPreferencesProfileInformation.getString("currentUser", "");
        currentUser = gson.fromJson(json, User.class);
        userId = String.valueOf(currentUser.getId());
        authToken = currentUser.getAuthToken();

        textView_profile_id.setText(String.valueOf(currentUser.getId()));
        textView_profile_name.setText(currentUser.getFname() + " " + currentUser.getLname());
        textView_profile_phone.setText(currentUser.getPhone());

    }

    public void loadFriendList() {
        friendLists = new ArrayList<>();

        Gson gson = new Gson();
        String postUser = gson.toJson(currentUser, User.class);
        Log.d("JSON REQUEST", postUser);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.POST,
                permission_list_url,
                postUser,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            Gson gson = new Gson();
                            FriendList friendList;
                            for(int i= 0; i < jsonArray.length(); i++) {
                                Log.d("JSON RESPONSE", jsonArray.getJSONObject(i).toString());
                                friendList = gson.fromJson(jsonArray.getJSONObject(i).toString(), FriendList.class);
                                friendLists.add(friendList);
                            }
                            friendListListAdapter= new FriendListListAdapter(PermissionManagerActivity.this, friendLists);
                            listView_friend_list.setAdapter(friendListListAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(PermissionManagerActivity.this, "Can't fetch friend list, try again later.");
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
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        MySingleton.getInstance(PermissionManagerActivity.this).addToRequestQueue(jsonArrayRequest);


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
                        friendPermissionStatus = friendList.getPermissionStatus();
                        friendId = friendList.getId();
                    }
                }
        );

        listView_search_friend.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        User friend = searchFriends.get(position);
                        UserHasFriend addFriend = new UserHasFriend(currentUser.getId(), 1, friend.getId());

                        Gson gson = new Gson();
                        String postUser = gson.toJson(addFriend, UserHasFriend.class);


                        //authentication logic.
                        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(
                                Request.Method.POST,
                                add_friend_url,
                                postUser,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        try {
                                            Log.d("RESPONSE", jsonObject.toString());
                                            String response = jsonObject.getString("response");
                                            Message.message(PermissionManagerActivity.this, response);
                                            loadFriendList();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Message.message(PermissionManagerActivity.this, "Error...");
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
                                headers.put("abc", currentUser.getAuthToken());
                                return headers;
                            }
                        };

                        int socketTimeout = 10000;//10 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsonObjectRequest.setRetryPolicy(policy);
                        MySingleton.getInstance(PermissionManagerActivity.this).addToRequestQueue(jsonObjectRequest);
                        return true;
                    }
                }
        );
    }

    public void addListenerForNavigationView() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.nav_permission_manager :
                                Intent intent = new Intent("com.example.root.trackr.PermissionManagerActivity");
                                startActivity(intent);
                                break;
                            case R.id.nav_main_menu :
                                intent = new Intent("com.example.root.trackr.MainMenuActivity");
                                startActivity(intent);
                                break;
                            case R.id.nav_logout:
                                AlertDialog.Builder logoutAlertBuilder = new AlertDialog.Builder(PermissionManagerActivity.this);
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

    public void addListenerForButton() {
        button_change_permission_status.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // change permission status
                        if(friendPermissionStatus == 0)
                            friendPermissionStatus = 1;
                        else
                            friendPermissionStatus = 0;

                        FriendList requestFriendStatusChange = new FriendList(friendId, null, friendPermissionStatus);
                        Gson gson = new Gson();
                        String postUser = gson.toJson(requestFriendStatusChange, FriendList.class);
                        Log.d("REQUEST", postUser.toString());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                change_status_url,
                                postUser,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        try {
                                            Log.d("RESPONSE", jsonObject.getString("response"));
                                            String response = "Permission status updated";
                                            Message.message(PermissionManagerActivity.this, response);
                                            loadFriendList();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Message.message(PermissionManagerActivity.this, "Coudn't make status change request, try after sometime.");
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
                                headers.put("user_id", userId);
                                headers.put("abc", authToken);
                                return headers;
                            }
                        };
                        int socketTimeout = 10000;//10 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsonObjectRequest.setRetryPolicy(policy);
                        MySingleton.getInstance(PermissionManagerActivity.this).addToRequestQueue(jsonObjectRequest);


                    }
                }
        );


        button_search_friend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadSearchFriendList();
                    }
                }
        );
    }




    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void loadSearchFriendList() {
        searchFriends = new ArrayList<>();

        Gson gson = new Gson();
        User searchUser = new User(editText_search_friend.getText().toString(), null, 0, null, null, authToken);
        String postUser = gson.toJson(searchUser, User.class);
        Log.d("JSON REQUEST", postUser);
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(
                Request.Method.POST,
                search_friend_url,
                postUser,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            Gson gson = new Gson();
                            User friend;
                            for(int i= 0; i < jsonArray.length(); i++) {
                                Log.d("JSON RESPONSE", jsonArray.getJSONObject(i).toString());
                                friend = gson.fromJson(jsonArray.getJSONObject(i).toString(), User.class);
                                searchFriends.add(friend);
                            }
                            searchFriendListAdapter= new SearchFriendListAdapter(PermissionManagerActivity.this, searchFriends);
                            listView_search_friend.setAdapter(searchFriendListAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(PermissionManagerActivity.this, "Can't fetch users list, try again later.");
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
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonArrayRequest.setRetryPolicy(policy);
        MySingleton.getInstance(PermissionManagerActivity.this).addToRequestQueue(jsonArrayRequest);


    }

}

