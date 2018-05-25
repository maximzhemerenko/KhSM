package com.khsm.app.presentation.ui.screens.meetings;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.Result;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.DisciplinesManager;
import com.khsm.app.domain.MeetingsManager;
import com.khsm.app.domain.UserManager;
import com.khsm.app.presentation.ui.screens.MainActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddResultsFragment extends Fragment {
    public static AddResultsFragment newInstance(Meeting meeting) {
        AddResultsFragment fragment = new AddResultsFragment();
        fragment.meeting = meeting;
        return fragment;
    }

    private Toolbar toolbar;
    private EditText results;
    private Button done;
    private Button add;
    private Spinner spinnerDisciplines;
    private Spinner spinnerUsers;
    private TextView meetingNumber;
    private TextView tvDisciplines;
    private TextView tvUsers;
    private TextView tvWriteResults;

    private Meeting meeting;

    private DisciplinesSpinnerAdapter disciplinesSpinnerAdapter;
    private UsersSpinnerAdapter usersSpinnerAdapter;
    private ProgressBar progressBar;

    @Nullable
    private ProgressDialog progressDialog;

    @Nullable
    private Disposable loadDisposable;

    private DisciplinesManager disciplinesManager;
    private UserManager userManager;
    private MeetingsManager meetingsManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userManager = new UserManager(requireContext());
        disciplinesManager = new DisciplinesManager(requireContext());
        meetingsManager = new MeetingsManager(requireContext());

        disciplinesSpinnerAdapter = new DisciplinesSpinnerAdapter(getContext(), this);
        usersSpinnerAdapter = new UsersSpinnerAdapter(getContext(), this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_results_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.showMenu();
            }
        });

        results = view.findViewById(R.id.results);

        progressBar = ViewCompat.requireViewById(view, R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        spinnerDisciplines = view.findViewById(R.id.spinnerDisciplines);
        loadDisciplinesToSpinner();
        spinnerDisciplines.setAdapter(disciplinesSpinnerAdapter);

        spinnerUsers = view.findViewById(R.id.spinnerUsers);
        loadUsersToSpinner();
        spinnerUsers.setAdapter(usersSpinnerAdapter);

        meetingNumber = view.findViewById(R.id.tvTitle);
        tvDisciplines = view.findViewById(R.id.tvDisciplines);
        tvUsers = view.findViewById(R.id.tvUsers);
        tvWriteResults = view.findViewById(R.id.tvAddResults);

        done = view.findViewById(R.id.doneButton);
        View.OnClickListener doneClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.replaceFragment(MeetingListFragment.newInstance());
            }
        };
        done.setOnClickListener(doneClicked);

        add = view.findViewById(R.id.buttonAdd);
        add.setOnClickListener(cr -> createResult());
        return view;
    }

    public void createResult() {
        if (results.length() < 1) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        Discipline discipline = (Discipline) spinnerDisciplines.getSelectedItem();
        User user = (User) spinnerUsers.getSelectedItem();

        String resultsString = results.getText().toString();
        List<Float> attempts;
        attempts = parseStringToFloatArray(resultsString);

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.show(requireContext(), null, getString(R.string.Please_WaitD3), true, false);

        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }

        Result result = new Result(meeting, user, attempts, discipline);

        loadDisposable = meetingsManager.createResult(result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::creatingDone,
                        this::handleCreatingError
                );
    }

    private void creatingDone(Result result) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        results.setText(null);
    }

    private void handleCreatingError(Throwable throwable) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(R.string.AddResult_Error_ResultCreationError)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    public List<Float> parseStringToFloatArray(String results) {
        String[] array;
        array = results.split("\n");
        List<Float> attempts = new ArrayList<>();
        for (String a : array) {
            if (a.equals("DNF") || a.equals("dnf") || a.equals("ДНФ") || a.equals("днф") || a.equals("-")) {
                attempts.add(null);
            }
            else {
                try {
                    attempts.add(Float.parseFloat(a));
                } catch (Exception e) {
                    showErrorMessage(getString(R.string.Register_Error_CheckInputData));
                    return null;
                }
            }
        }
        return attempts;
    }

    private void showErrorMessage(String errorMessage) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void cancelLoadOperation() {
        if (loadDisposable == null)
            return;

        loadDisposable.dispose();
        loadDisposable = null;
    }

    private void loadDisciplinesToSpinner() {
        progressBar.setVisibility(View.VISIBLE);

        disciplinesManager.getDisciplines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setDisciplines,
                        this::handleError
                );
    }

    private void setDisciplines(List<Discipline> disciplines) {
        progressBar.setVisibility(View.INVISIBLE);

        disciplinesSpinnerAdapter.setDisciplines(disciplines);
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void loadUsersToSpinner() {
        progressBar.setVisibility(View.VISIBLE);

        userManager.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setUsers,
                        this::handleError
                );
    }

    private void setUsers(List<User> users) {
        progressBar.setVisibility(View.INVISIBLE);

        usersSpinnerAdapter.setUsers(users);
    }

}
