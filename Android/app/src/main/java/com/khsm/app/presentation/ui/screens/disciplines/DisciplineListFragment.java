package com.khsm.app.presentation.ui.screens.disciplines;

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
import android.widget.ProgressBar;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.domain.DisciplinesManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DisciplineListFragment extends Fragment {
    public static DisciplineListFragment newInstance() {
        return new DisciplineListFragment();
    }

    private DisciplinesManager disciplinesManager;

    private DisciplineListAdapter adapter;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Nullable
    private Disposable loadDisposable;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disciplinesManager = new DisciplinesManager();

        adapter = new DisciplineListAdapter(getContext(), this);
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
        View view = inflater.inflate(R.layout.disciplines_list_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        loadDisciplines();

        return view;
    }

    private void loadDisciplines() {
        progressBar.setVisibility(View.VISIBLE);

        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }

        loadDisposable = disciplinesManager.getDisciplines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setDisciplines,
                        this::handleError
                );
    }

    private void setDisciplines(List<Discipline> disciplines) {
        progressBar.setVisibility(View.INVISIBLE);

        adapter.setDisciplines(disciplines);
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }


    public void onItemClicked(@NonNull Discipline discipline) {
        // TODO: 20.02.2018 display discipline details fragment
    }
}
