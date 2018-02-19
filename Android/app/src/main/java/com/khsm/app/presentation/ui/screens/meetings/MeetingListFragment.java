package com.khsm.app.presentation.ui.screens.meetings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khsm.app.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeetingListFragment extends Fragment{
    public static MeetingListFragment newInstance() {
        return new MeetingListFragment();
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meetings_fragment, container, false);

        Context context = getContext();

        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<Meeting> meetingList = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            meetingList.add(new Meeting(i, i+1, new Date()));
        }

        MeetingListAdapter adapter = new MeetingListAdapter(context, meetingList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
