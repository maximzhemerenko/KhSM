package com.khsm.app.presentation.ui.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.khsm.app.R;
import com.khsm.app.presentation.ui.screens.meetings.MeetingListFragment;
import com.spryrocks.android.modules.ui.routing.context.IFrameRoutingContext;
import com.spryrocks.android.modules.ui.routing.context.IRoutingContext;

public class MainActivity extends AppCompatActivity {
    private IFrameRoutingContext frameRoutingContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameRoutingContext = IRoutingContext.frame(this, R.id.content);

        if (savedInstanceState == null) {
            frameRoutingContext.replaceFragment(MeetingListFragment.newInstance(), false);
        }
    }

    public void replaceFragment(Fragment fragment) {
        frameRoutingContext.replaceFragment(fragment, false);
    }
}
