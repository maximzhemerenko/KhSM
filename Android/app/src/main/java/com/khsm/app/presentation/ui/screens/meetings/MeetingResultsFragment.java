package com.khsm.app.presentation.ui.screens.meetings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khsm.app.R;
import com.khsm.app.data.entities.Meeting;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MeetingResultsFragment extends Fragment {
    private static final String KET_MEETING = "KET_MEETING";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public static MeetingResultsFragment newInstance(Meeting meeting) {
        MeetingResultsFragment fragment = new MeetingResultsFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(KET_MEETING, meeting);

        fragment.setArguments(arguments);

        return fragment;
    }

    private Meeting meeting;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Toolbar toolbar;

    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        meeting = (Meeting) arguments.getSerializable(KET_MEETING);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meeting_results_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        toolbar.setTitle(dateFormat.format(meeting.date));

        tabLayout = view.findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("2 x 2"));
        tabLayout.addTab(tabLayout.newTab().setText("3 x 3"));
        tabLayout.addTab(tabLayout.newTab().setText("5 x 5"));
        tabLayout.addTab(tabLayout.newTab().setText("7 x 7"));

        return view;
    }

}
