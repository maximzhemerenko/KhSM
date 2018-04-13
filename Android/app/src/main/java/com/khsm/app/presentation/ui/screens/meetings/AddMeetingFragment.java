package com.khsm.app.presentation.ui.screens.meetings;

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
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.domain.MeetingsManager;
import com.khsm.app.presentation.ui.screens.MainActivity;
import com.khsm.app.presentation.ui.utils.maskedittext.EditTextMask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddMeetingFragment extends Fragment{
    public static AddMeetingFragment newInstance() {
        return new AddMeetingFragment();
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private Toolbar toolbar;
    private EditText meetingNumber;
    private EditText meetingDate;
    private Button done;

    private MeetingsManager meetingsManager;

    @Nullable
    private ProgressDialog progressDialog;

    @Nullable
    private Disposable createMeetingDisposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        meetingsManager = new MeetingsManager(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_meeting_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                mainActivity.showMenu();
            }
        });

        meetingDate = view.findViewById(R.id.meeting_date);
        EditTextMask.setup(meetingDate, "##-##-####");
        meetingNumber = view.findViewById(R.id.meeting_number);

        done = view.findViewById(R.id.doneButton);
        done.setOnClickListener(cm -> createMeeting());
        return view;
    }

    public void createMeeting() {
        if (meetingNumber.length() < 1
                || meetingDate.length() < 1) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        Date meetingDate;
        try {
            meetingDate = stringToJavaDate(this.meetingDate.getText().toString());
        } catch (ParseException e) {
            showErrorMessage(getString(R.string.Register_Error_CheckInputData));
            return;
        }

        Meeting meeting = new Meeting(
                Integer.parseInt(meetingNumber.getText().toString()),
                meetingDate
        );

        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = ProgressDialog.show(requireContext(), null, getString(R.string.Please_WaitD3), true, false);

        if (createMeetingDisposable != null) {
            createMeetingDisposable.dispose();
            createMeetingDisposable = null;
        }

         createMeetingDisposable = meetingsManager.createMeeting(meeting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::creatingDone,
                        this::handleError
                );
    }

    private void creatingDone(Meeting meeting) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        MainActivity mainActivity = (MainActivity) requireActivity();
        mainActivity.replaceFragment(AddResultsFragment.newInstance(meeting));

    }

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
                .setMessage(R.string.AddMeeting_Error_MeetingCreationError)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private java.util.Date stringToJavaDate(@NonNull String dateString) throws ParseException {
        dateString = dateString.trim();

        if (dateString.isEmpty())
            return null;

        return dateFormat.parse(dateString);
    }

}

