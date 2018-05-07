package com.khsm.app.presentation.ui.screens.news;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.khsm.app.data.entities.News;
import com.khsm.app.data.entities.Session;
import com.khsm.app.domain.AuthManager;
import com.khsm.app.domain.NewsManager;
import com.khsm.app.presentation.ui.screens.MainActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsFragment extends Fragment {

    public static final String ROLE_ADMIN = "Admin";
    private AuthManager authManager;

    private NewsAdapter adapter;
    private NewsManager newsManager;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private FloatingActionButton addNews_fab;

    @Nullable
    private Disposable loadDisposable;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = requireContext();

        newsManager = new NewsManager(context);
        adapter = new NewsAdapter(context);
        authManager = new AuthManager(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.showMenu();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        addNews_fab = view.findViewById(R.id.addNews_fab);

        if (authManager.isAdmin()) {
            addNews_fab.setVisibility(View.VISIBLE);
        } else {
            addNews_fab.setVisibility(View.INVISIBLE);
        }

        loadDisciplines();

        View.OnClickListener addNews = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.replaceFragment(AddNewsFragment.newInstance());
            }
        };

        addNews_fab.setOnClickListener(addNews);
        return view;
    }

    private void loadDisciplines() {
        progressBar.setVisibility(View.VISIBLE);

        if (loadDisposable != null) {
            loadDisposable.dispose();
            loadDisposable = null;
        }

        loadDisposable = newsManager.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::setNews,
                        this::handleError
                );
    }

    private void setNews(List<News> news) {
        progressBar.setVisibility(View.INVISIBLE);

        adapter.setNews(news);
    }

    private void handleError(Throwable throwable) {
        progressBar.setVisibility(View.INVISIBLE);

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(throwable.getMessage())
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    public static Fragment newInstance() {
        return new NewsFragment();
    }
}
