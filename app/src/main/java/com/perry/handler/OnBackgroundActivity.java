package com.perry.handler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OnBackgroundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_background);
    }

    public void postBackgroundThread(View view) {

    }
}
