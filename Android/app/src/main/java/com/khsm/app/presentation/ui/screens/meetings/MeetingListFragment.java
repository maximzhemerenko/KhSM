package com.khsm.app.presentation.ui.screens.meetings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.khsm.app.R;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.domain.MeetingsManager;
import com.khsm.app.presentation.ui.screens.MainActivity;
import com.khsm.app.presentation.ui.screens.disciplines.DisciplineListFragment;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MeetingListFragment extends Fragment implements MenuItem.OnMenuItemClickListener {
    public static MeetingListFragment newInstance() {
        return new MeetingListFragment();
    }

    private MeetingsManager meetingsManager;

    @Nullable
    private Disposable loadDisposable;

    private MeetingListAdapter adapter;

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar toolbar;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private MenuItem disciplines_menuItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meetingsManager = new MeetingsManager();

        adapter = new MeetingListAdapter(getContext(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meetings_list_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        Menu menu = toolbar.getMenu();

        disciplines_menuItem = menu.add(R.string.Disciplines);
        disciplines_menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        disciplines_menuItem.setOnMenuItemClickListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        loadMeetings();

        return view;
    }

    private void loadMeetings() {
        progressBar.setVisibility(View.VISIBLE);

        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }

        loadDisposable = meetingsManager.getMeetings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setMeetings,
                        this::handleError
                );
    }

    private void setMeetings(List<Meeting> meetings) {
        progressBar.setVisibility(View.INVISIBLE);

        adapter.setMeetings(meetings);
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    public void onItemClicked(@NonNull Meeting meeting) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(MeetingResultsFragment.newInstance(meeting));
    }

    private void showDisciplineList() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(DisciplineListFragment.newInstance());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item == disciplines_menuItem) {
            showDisciplineList();
            return true;
        }

        return false;
    }
}
