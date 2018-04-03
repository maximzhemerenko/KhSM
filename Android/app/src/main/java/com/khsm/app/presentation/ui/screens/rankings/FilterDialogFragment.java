package com.khsm.app.presentation.ui.screens.rankings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.khsm.app.R;
import com.khsm.app.data.api.entities.RankingsFilterInfo;
import com.khsm.app.data.entities.Gender;

public class FilterDialogFragment extends DialogFragment {
    private static final String KEY_FILTER_INFO = "KEY_FILTER_INFO";

    public static FilterDialogFragment newInstance(@NonNull RankingsFilterInfo filterInfo) {
        FilterDialogFragment fragment = new FilterDialogFragment();

        Bundle bundle = new Bundle();

        bundle.putSerializable(KEY_FILTER_INFO, filterInfo);

        fragment.setArguments(bundle);

        return fragment;
    }

    private RadioButton average;
    private RadioButton single;
    private RadioButton ascending;
    private RadioButton descending;
    private RadioButton male;
    private RadioButton female;
    private RadioButton both;
    private Button cancel;
    private Button apply;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments == null)
            throw new RuntimeException("Arguments should not be null");

        View view = inflater.inflate(R.layout.filter_dialog_fragment, null);

        average = view.findViewById(R.id.average);
        single = view.findViewById(R.id.single);
        ascending = view.findViewById(R.id.ascending);
        descending = view.findViewById(R.id.descending);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        both = view.findViewById(R.id.both);
        cancel = view.findViewById(R.id.cancel);
        apply = view.findViewById(R.id.apply);

        if (savedInstanceState == null) {
            RankingsFilterInfo filterInfo = (RankingsFilterInfo) arguments.getSerializable(KEY_FILTER_INFO);
            setFilterInfo(filterInfo);
        }

        View.OnClickListener cancelClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };

        View.OnClickListener applyClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!male.isChecked() && !female.isChecked() && !both.isChecked())
                        || (!average.isChecked() && !single.isChecked())
                        || (!ascending.isChecked() && !descending.isChecked())) {
                    return;
                }

                Gender gender = male.isChecked() ? Gender.MALE :
                        female.isChecked() ? Gender.FEMALE : null;
                RankingsFilterInfo.FilterType filterType = average.isChecked() ? RankingsFilterInfo.FilterType.Average :
                        single.isChecked() ? RankingsFilterInfo.FilterType.Single : null;
                RankingsFilterInfo.SortType sortType = ascending.isChecked() ? RankingsFilterInfo.SortType.Ascending :
                        descending.isChecked() ? RankingsFilterInfo.SortType.Descending : null;

                RankingsFilterInfo rankingsFilterInfo = new RankingsFilterInfo(filterType, sortType, gender);

                RankingsFragment parentFragment = (RankingsFragment) getParentFragment();
                parentFragment.applyFilter(rankingsFilterInfo);

                dismiss();
            }
        };

        cancel.setOnClickListener(cancelClicked);
        apply.setOnClickListener(applyClicked);

        return view;
    }

    private void setFilterInfo(@NonNull RankingsFilterInfo filterInfo) {
        switch (filterInfo.filterType) {
            case Single:
                single.setChecked(true);
                break;
            case Average:
                average.setChecked(true);
                break;
        }

        if (filterInfo.gender != null) {
            switch (filterInfo.gender) {
                case MALE:
                    male.setChecked(true);
                    break;
                case FEMALE:
                    female.setChecked(true);
                    break;
            }
        } else {
            both.setChecked(true);
        }

        switch (filterInfo.sortType) {
            case Ascending:
                ascending.setChecked(true);
                break;
            case Descending:
                descending.setChecked(true);
                break;
        }
    }
}

