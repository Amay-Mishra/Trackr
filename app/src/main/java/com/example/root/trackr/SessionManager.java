package com.example.root.trackr;

/**
 * Created by joy on 8/5/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

public class SessionManager {

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    private String fname, lname, id, phone;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    //SharedPreferences
    private SharedPreferences pref;
    private Editor editor;
    private Context context;

    //SharedPreferences filename
    private static final String PREF_NAME = "TrackrLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn, String id, String fname, String lname, String phone) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.putString("lname", lname);
        editor.putString("fname", fname);
        editor.putString("id", id);
        editor.putString("phone", phone);
        editor.commit();

        Log.d(TAG, "User login session modified!");

    }

    //load session data
    public Bundle loadSessionData() {
        if(pref!= null) {
            fname= pref.getString("fname", fname);
            lname= pref.getString("lname", lname);
            id= pref.getString("id", id);
            phone= pref.getString("phone", phone);
        }

        Bundle bundle= new Bundle();
        bundle.putString("fname", fname);
        bundle.putString("lname", lname);
        bundle.putString("id", id);
        bundle.putString("phone", phone);

        return bundle;
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}

