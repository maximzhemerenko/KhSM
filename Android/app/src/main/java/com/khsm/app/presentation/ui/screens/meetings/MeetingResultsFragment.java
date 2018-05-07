package com.khsm.app.presentation.ui.screens.meetings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
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
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.Result;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.AuthManager;
import com.khsm.app.domain.MeetingsManager;
import com.khsm.app.domain.UserManager;
import com.khsm.app.presentation.ui.adapters.AdapterUtils;
import com.khsm.app.presentation.ui.adapters.ResultsAdapter;
import com.khsm.app.presentation.ui.screens.MainActivity;
import com.khsm.app.presentation.ui.utils.PrintBitmapBuilder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MeetingResultsFragment extends Fragment implements MenuItem.OnMenuItemClickListener {
    private static final String KEY_MEETING = "KEY_MEETING";
    public static final String ROLE_ADMIN = "Admin";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public static MeetingResultsFragment newInstance(Meeting meeting) {
        MeetingResultsFragment fragment = new MeetingResultsFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_MEETING, meeting);

        fragment.setArguments(arguments);

        return fragment;
    }

    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    @Nullable
    private Disposable loadDisposable;

    private MeetingsManager meetingsManager;
    private UserManager userManager;
    private AuthManager authManager;

    private Meeting meeting;
    @Nullable
    private List<DisciplineResults> disciplineResults;

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Toolbar toolbar;

    private TabLayout tabLayout;

    private ResultsAdapter adapter;

    @SuppressWarnings("FieldCanBeLocal")
    private boolean lastMeetingMode;

    private FloatingActionButton faButton;

    @Nullable
    private MenuItem meetings_menuItem;
    private MenuItem print_menuItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = requireContext();

        meetingsManager = new MeetingsManager(context);
        userManager = new UserManager(context);
        authManager = new AuthManager(context);

        adapter = new ResultsAdapter(context, AdapterUtils.DisplayMode.User);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // init view
        View view = inflater.inflate(R.layout.meeting_results_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.showMenu();
            }
        });

        Menu menu = toolbar.getMenu();

        meetings_menuItem = menu.add(R.string.Meetings);
        meetings_menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        meetings_menuItem.setOnMenuItemClickListener(this);
        meetings_menuItem.setVisible(false);

        print_menuItem = menu.add(R.string.Print);
        print_menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        print_menuItem.setOnMenuItemClickListener(this);

        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setVisibility(View.INVISIBLE);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        faButton = view.findViewById(R.id.fabutton);
        if (authManager.isAdmin()) {
            faButton.setVisibility(View.VISIBLE);
        } else {
            faButton.setVisibility(View.INVISIBLE);
        }

        View.OnClickListener faClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.replaceFragment(AddResultsFragment.newInstance(meeting));
            }
        };

        faButton.setOnClickListener(faClicked);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle arguments = getArguments();
        if (arguments == null)
            throw new RuntimeException("Arguments should be provided");

        Meeting meeting = (Meeting) arguments.getSerializable(KEY_MEETING);

        lastMeetingMode = meeting == null;

        if (lastMeetingMode) {
            //noinspection ConstantConditions
            meetings_menuItem.setVisible(true);
        }

        if (!lastMeetingMode) {
            //noinspection ConstantConditions
            setMeeting(meeting);
        } else {
            loadLastMeeting();
        }
    }

    public boolean isAdmin(List<String> roles) {
        return roles.contains(ROLE_ADMIN);
    }

    @Override
    public void onStop() {
        super.onStop();

        cancelLoadOperation();
    }

    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            DisciplineResults disciplineResults = (DisciplineResults) tab.getTag();
            if (disciplineResults != null) {
                setDisciplineResults(disciplineResults);
            }
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

        toolbar.setTitle(dateFormat.format(meeting.date) + " (" +  meeting.number + ")");

        loadResults();
    }

    private void cancelLoadOperation() {
        if (loadDisposable == null)
            return;

        loadDisposable.dispose();
        loadDisposable = null;
    }

    private void loadLastMeeting() {
        toolbar.setTitle(R.string.Last_Meeting);

        progressBar.setVisibility(View.VISIBLE);

        cancelLoadOperation();
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

        cancelLoadOperation();
        loadDisposable = userManager.getMeetingResults(meeting.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setDisciplineResults,
                        this::handleError
                );
    }

    private void setDisciplineResults(List<DisciplineResults> disciplineResults) {
        this.disciplineResults = disciplineResults;

        progressBar.setVisibility(View.INVISIBLE);

        tabLayout.removeAllTabs();

        for (DisciplineResults disciplineResult : disciplineResults) {
            tabLayout.addTab(tabLayout.newTab().setText(disciplineResult.discipline.name).setTag(disciplineResult));
        }

        boolean showTabs = !disciplineResults.isEmpty();
        tabLayout.setVisibility(showTabs ? View.VISIBLE : View.INVISIBLE);
        if (showTabs) {
            tabLayout.setAlpha(0);
            tabLayout.animate().alpha(1).setDuration(300).start();
            recyclerView.setAlpha(0);
            recyclerView.animate().alpha(1).setDuration(300).start();
        }
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void setDisciplineResults(@NonNull DisciplineResults disciplineResults) {
        adapter.setResults(disciplineResults.results, null);
    }

    private void showMeetingList() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.replaceFragment(MeetingListFragment.newInstance());
    }

    private void print() {
        Context context = requireContext();

        @Nullable List<DisciplineResults> disciplineResults = this.disciplineResults;
        if (disciplineResults == null)
            return;

        PrintBitmapBuilder builder = new PrintBitmapBuilder(context);

        StringBuilder sb = new StringBuilder();
        sb.append(dateFormat.format(meeting.date)).append(" (").append(meeting.number).append(")\n\n");
        builder.setTextAlign(PrintBitmapBuilder.ReceiptTextAlign.CENTER);
        builder.appendString(sb.toString());

        sb = new StringBuilder();

        for (DisciplineResults disciplineResult : disciplineResults) {
            sb.append("--- ").append(disciplineResult.discipline.name).append(" ---").append("\n");

            for (int i = 0; i < disciplineResult.results.size(); i++) {
                Result result = disciplineResult.results.get(i);
                User user = result.user;
                sb.append(i).append(". ").append(user.firstName).append(" ").append(user.lastName).append(" ");
                sb.append(AdapterUtils.formatResults(AdapterUtils.SortMode.Average, result, context)).append("\n");
            }

            sb.append("\n");
        }

        builder.setTextAlign(PrintBitmapBuilder.ReceiptTextAlign.LEFT);
        builder.appendString(sb.toString());

        PrintHelper printHelper = new PrintHelper(context);
        printHelper.printBitmap("Print", builder.build());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item == meetings_menuItem) {
            showMeetingList();
            return true;
        } else if (item == print_menuItem) {
            print();
            return true;
        }

        return false;
    }
}
