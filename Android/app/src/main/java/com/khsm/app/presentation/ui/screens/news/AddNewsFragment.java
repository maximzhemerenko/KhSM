package com.khsm.app.presentation.ui.screens.news;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.khsm.app.R;
import com.khsm.app.data.entities.News;
import com.khsm.app.data.entities.User;
import com.khsm.app.domain.NewsManager;
import com.khsm.app.presentation.ui.screens.MainActivity;
import com.khsm.app.presentation.ui.utils.maskedittext.EditTextMask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddNewsFragment extends Fragment {
    public static AddNewsFragment newInstance() {
    return new AddNewsFragment();
    }

    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private Toolbar toolbar;
    //private EditText add_news_date;
    private EditText message_news_text;
    private Button add_button;

    private NewsManager newsManager;

    @Nullable
    private ProgressDialog progressDialog;

    @Nullable
    private Disposable addNewsDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newsManager = new NewsManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_news_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.showMenu();
            }
        });

        //add_news_date = view.findViewById(R.id.add_news_date);
        //EditTextMask.setup(add_news_date, "##-##-####");

        add_button = view.findViewById(R.id.add_button);
        add_button.setOnClickListener(cm -> addNews());

        message_news_text = view.findViewById(R.id.message_news_text);

        return view;
    }

    public void addNews() {

        if (message_news_text.length() < 1
                /*|| add_news_date.length() < 1*/) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        /*try {
            newsDate = stringToJavaDate(this.add_news_date.getText().toString());
        } catch (ParseException e) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }*/
        Date newsDate = Calendar.getInstance().getTime();

        News news = new News( null, null, message_news_text.getText().toString(), newsDate);

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.show(requireContext(), null, getString(R.string.Please_WaitD3), true, false);

        if (addNewsDisposable != null) {
            addNewsDisposable.dispose();
            addNewsDisposable = null;
        }

        addNewsDisposable = newsManager.addNews(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::addDone,
                        this::handleError
                );
    }

    private void addDone(News news) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        requireActivity().onBackPressed(); // FIXME: 30.03.2018
    }

   /* private java.util.Date stringToJavaDate(@NonNull String dateString) throws ParseException {
        dateString = dateString.trim();

        if (dateString.isEmpty())
            return null;

        return dateFormat.parse(dateString);
    }*/

    private void showErrorMessage(String errorMessage) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(errorMessage)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private void handleError(Throwable throwable) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.Error)
                .setMessage(R.string.AddNews_Error_NewsCreationError)
                .setPositiveButton(R.string.OK, null)
                .show();
    }
}
