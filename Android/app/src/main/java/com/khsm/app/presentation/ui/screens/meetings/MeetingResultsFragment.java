package com.khsm.app.presentation.ui.screens.meetings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khsm.app.R;

public class MeetingResultsFragment extends Fragment {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Toolbar toolbar;

    public static MeetingResultsFragment newInstance() {
        return new MeetingResultsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meeting_results_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        return view;
    }

}
