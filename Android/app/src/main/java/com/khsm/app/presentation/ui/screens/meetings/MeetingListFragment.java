package com.khsm.app.presentation.ui.screens.meetings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khsm.app.R;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.domain.MeetingsManager;

import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MeetingListFragment extends Fragment{
    public static MeetingListFragment newInstance() {
        return new MeetingListFragment();
    }

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @SuppressLint("CheckResult")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meetings_fragment, container, false);

        Context context = getContext();

        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        MeetingsManager meetingsManager = new MeetingsManager();

        meetingsManager.getMeetings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setMeetings,
                        this::handleError
                );

        return view;
    }

    private void setMeetings(List<Meeting> meetings) {
        MeetingListAdapter adapter = new MeetingListAdapter(getContext(), meetings);
        recyclerView.setAdapter(adapter);
    }

    private void handleError(Throwable throwable) {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }
}
