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
    private String auth_token;
    private Context context;
    private SharedPreferences mPrefs;
    private Editor prefsEditor;
    private String userData;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    //SharedPreferences filename
    private static final String PREF_NAME = "TrackrLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    SessionManager(Context context, User user, String token) {
        this.context = context;
        mPrefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prefsEditor = mPrefs.edit();
        currentUser = user;
        auth_token = token;
    }

    public void setLogin(boolean isLoggedIn) {
        Gson gson = new Gson();
        String userData = gson.toJson(currentUser, User.class);
        prefsEditor.putString("currentUser", userData);
        prefsEditor.putString("auth_token", auth_token);
        prefsEditor.commit();

        //To check in COnsole screen for Debugging
        Log.d(TAG, "User login session modified!");
    }

    public Bundle loadSessionData() {
        if (mPrefs != null) {
            userData = mPrefs.getString("currentUser", "");
            auth_token = mPrefs.getString("auth_token", "");
        }

        Bundle bundle = new Bundle();
        bundle.putString("currentUser", userData);
        bundle.putString("auth_token", auth_token);

        return bundle;
    }

    public boolean isLoggedIn(){
        return mPrefs.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
