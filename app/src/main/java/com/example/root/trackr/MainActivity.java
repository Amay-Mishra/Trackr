package com.example.root.trackr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static Button button_regLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        addListenerForButton();
    }

    public void initialize() {
        button_regLink= (Button) findViewById(R.id.buttonRegLink);
    }

    public void addListenerForButton() {
        button_regLink.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent("com.example.root.trackr.RegisterActivity");
                        startActivity(intent);
                    }
                }
        );
    }
}