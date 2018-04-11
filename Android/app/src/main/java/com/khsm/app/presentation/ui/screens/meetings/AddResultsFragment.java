package com.khsm.app.presentation.ui.screens.meetings;

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
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.DisciplinesManager;
import com.khsm.app.domain.UserManager;
import com.khsm.app.presentation.ui.screens.MainActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddResultsFragment extends Fragment {
    public static AddResultsFragment newInstance() {
        return new AddResultsFragment();
    }

    private Toolbar toolbar;
    private EditText results;
    private Button done;
    private Button add;
    private Spinner spinnerDisciplines;
    private Spinner spinnerUsers;
    private TextView meetingNumber;

    private DisciplinesSpinnerAdapter disciplinesSpinnerAdapter;
    private UsersSpinnerAdapter usersSpinnerAdapter;
    private ProgressBar progressBar;


    @Nullable
    private Disposable loadDisposable;

    DisciplinesManager disciplinesManager;
    UserManager userManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userManager = new UserManager(requireContext());
        disciplinesManager = new DisciplinesManager(requireContext());

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

        done = view.findViewById(R.id.doneButton);
        add = view.findViewById(R.id.buttonAdd);
        meetingNumber = view.findViewById(R.id.tvTitle);
        return view;
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
