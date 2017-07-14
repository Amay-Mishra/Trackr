package com.example.root.trackr;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by root on 13/7/17.
 */

public class SessionManager {

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    private User currentUser;
    private Context context;
    private SharedPreferences mPrefs;
    private Editor prefsEditor;
    private String userData;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    //SharedPreferences filename
    private static final String PREF_NAME = "TrackrLogin";

    SessionManager(Context context, User user) {
        this.context = context;
        mPrefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prefsEditor = mPrefs.edit();
        currentUser = user;
    }

    public void setLogin() {
        Gson gson = new Gson();
        String userData = gson.toJson(currentUser, User.class);
        prefsEditor.putString("currentUser", userData);
        prefsEditor.putBoolean("loginStatus", true);
        prefsEditor.commit();

        //To check in COnsole screen for Debugging
        Log.d(TAG, "User login session modified!");
    }

    public Bundle loadSessionData() {
        if (mPrefs != null) {
            userData = mPrefs.getString("currentUser", "");
        }

        Bundle bundle = new Bundle();
        bundle.putString("currentUser", userData);

        return bundle;
    }

    public boolean loginStatus() {
       return mPrefs.getBoolean("loginStatus", false);
    }
}