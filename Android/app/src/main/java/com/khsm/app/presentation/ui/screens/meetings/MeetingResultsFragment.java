package com.khsm.app.presentation.ui.screens.meetings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.khsm.app.R;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.domain.MeetingsManager;
import com.khsm.app.domain.ResultsManager;
import com.khsm.app.presentation.ui.screens.MainActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MeetingResultsFragment extends Fragment implements MenuItem.OnMenuItemClickListener {
    private static final String KEY_MEETING = "KEY_MEETING";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public static MeetingResultsFragment newInstance(Meeting meeting) {
        MeetingResultsFragment fragment = new MeetingResultsFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_MEETING, meeting);

        fragment.setArguments(arguments);

        return fragment;
    }

    private ProgressBar progressBar;

    @Nullable
    private Disposable loadDisposable;

    private MeetingsManager meetingsManager;
    private ResultsManager resultsManager;

    private Meeting meeting;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Toolbar toolbar;

    private TabLayout tabLayout;

    private boolean lastMeetingMode;

    @Nullable
    private MenuItem meetings_menuItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meetingsManager = new MeetingsManager();
        resultsManager = new ResultsManager();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // load arguments
        Bundle arguments = getArguments();

        Meeting meeting = (Meeting) arguments.getSerializable(KEY_MEETING);

        lastMeetingMode = meeting == null;

        // init view
        View view = inflater.inflate(R.layout.meeting_results_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);

        Menu menu = toolbar.getMenu();
        if (lastMeetingMode) {
            meetings_menuItem = menu.add(R.string.Meetings);
            meetings_menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            meetings_menuItem.setOnMenuItemClickListener(this);
        }

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.INVISIBLE);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // load data
        if (!lastMeetingMode) {
            setMeeting(meeting);
        } else {
            loadLastMeeting();
        }

        return view;
    }

    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            DisciplineResults disciplineResults = (DisciplineResults) tab.getTag();
            onDisciplineClicked(disciplineResults);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void setMeeting(Meeting meeting) {
        this.meeting = meeting;

        progressBar.setVisibility(View.VISIBLE);

        toolbar.setTitle(dateFormat.format(meeting.date));

        loadResults();
    }

    private void loadLastMeeting() {
        toolbar.setTitle(R.string.Last_Meeting);

        progressBar.setVisibility(View.VISIBLE);

        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }

        loadDisposable = meetingsManager.getLastMeeting()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setMeeting,
                        this::handleError
                );
    }

    private void loadResults() {
        progressBar.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);

        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }

        loadDisposable = resultsManager.getMeetingResults(meeting.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setResults,
                        this::handleError
                );
    }

    private void setResults(List<DisciplineResults> disciplineResults) {
        progressBar.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(!disciplineResults.isEmpty() ? View.VISIBLE : View.INVISIBLE);

        tabLayout.removeAllTabs();

        for (DisciplineResults disciplineResult : disciplineResults) {
            tabLayout.addTab(tabLayout.newTab().setText(disciplineResult.discipline.name).setTag(disciplineResult));
        }
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void onDisciplineClicked(DisciplineResults disciplineResults) {
        Toast.makeText(getContext(), disciplineResults.discipline.name, Toast.LENGTH_SHORT).show();
        // TODO: 21.02.2018 show results in recycler view
    }

    private void showMeetingList() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(MeetingListFragment.newInstance());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item == meetings_menuItem) {
            showMeetingList();
            return true;
        }

        return false;
    }
}
