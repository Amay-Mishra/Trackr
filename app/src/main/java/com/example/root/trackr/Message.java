package com.example.root.trackr;

/**
 * Created by joy on 8/5/17.
 */

import android.content.Context;
import android.widget.Toast;

public class Message {

    static void message(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}