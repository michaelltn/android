package com.michaelcamerongames.framework;

import com.michaelcamerongames.percula.R;

import android.app.Activity;
import android.os.Bundle;

public class PerculaActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}