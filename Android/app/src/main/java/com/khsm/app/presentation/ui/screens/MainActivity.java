package com.khsm.app.presentation.ui.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khsm.app.R;
import com.khsm.app.presentation.ui.screens.meetings.MeetingListFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, MeetingListFragment.newInstance())
                    .commit();
        }
    }
}
