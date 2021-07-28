package com.kamalbind.thepiratebay_android_api_test_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kamalbind.thepiratebaylib.ThePirateBayGateWay;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ThePirateBayGateWay(this);
    }
}