package com.khsm.app.presentation.ui.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.khsm.app.R;
import com.khsm.app.presentation.ui.screens.meetings.MeetingListFragment;
import com.spryrocks.android.modules.ui.routing.context.IFrameRoutingContext;
import com.spryrocks.android.modules.ui.routing.context.IRoutingContext;
import com.spryrocks.android.modules.ui.routing.context.IScreenTarget;
import com.spryrocks.android.modules.ui.routing.endpoints.ActivityEndpoint;
import com.spryrocks.android.modules.ui.routing.endpoints.IScreenEndpoint;

public class MainActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    public static IScreenEndpoint endpoint(IScreenTarget target) {
        return new ActivityEndpoint<>(target, MainActivity.class);
    }

    private IFrameRoutingContext frameRoutingContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameRoutingContext = IRoutingContext.frame(this, R.id.content);

        if (savedInstanceState == null) {
            MeetingListFragment.endpoint(frameRoutingContext).navigate();
        }
    }

    public IFrameRoutingContext frameRoutingContext() {
        return frameRoutingContext;
    }
}
