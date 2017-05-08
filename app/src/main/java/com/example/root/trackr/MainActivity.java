package com.example.root.trackr;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchLoginScreen();
    }

    public void launchLoginScreen() {
        Intent i= new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
