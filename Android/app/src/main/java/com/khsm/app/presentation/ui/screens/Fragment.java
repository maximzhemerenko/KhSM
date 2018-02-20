package com.khsm.app.presentation.ui.screens;

import com.spryrocks.android.modules.ui.routing.context.IFrameRoutingContext;

public class Fragment extends android.support.v4.app.Fragment {
    public IFrameRoutingContext frameRoutingContext() {
        //noinspection ConstantConditions
        return ((MainActivity)getActivity()).frameRoutingContext();
    }
}
