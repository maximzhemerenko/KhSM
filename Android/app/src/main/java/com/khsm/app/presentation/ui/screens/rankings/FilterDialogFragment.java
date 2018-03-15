package com.khsm.app.presentation.ui.screens.rankings;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.RadioButton;

import com.khsm.app.R;
import com.khsm.app.data.entities.Gender;

public class FilterDialogFragment extends DialogFragment {
    public static FilterDialogFragment newInstance() {return new FilterDialogFragment();}

    private RadioButton average;
    private RadioButton single;
    private RadioButton ascending;
    private RadioButton descending;
    private RadioButton male;
    private RadioButton female;
    private RadioButton both;
    private Button cancel;
    private Button apply;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                FilterInfo.FilterType filterType = average.isChecked() ? FilterInfo.FilterType.Average :
                        single.isChecked() ? FilterInfo.FilterType.Single : null;
                FilterInfo.SortType sortType = ascending.isChecked() ? FilterInfo.SortType.Ascending :
                        descending.isChecked() ? FilterInfo.SortType.Descending : null;

                FilterInfo filterInfo = new FilterInfo(filterType, sortType, gender);

                RankingsFragment parentFragment = (RankingsFragment) getParentFragment();
                parentFragment.applyFilter(filterInfo);
            }
        };

        cancel.setOnClickListener(cancelClicked);
        apply.setOnClickListener(applyClicked);

        return view;
    }

    public static class FilterInfo {
        public FilterInfo(FilterType filterType, SortType sortType, Gender gender) {
            this.filterType = filterType;
            this.sortType = sortType;
            this.gender = gender;
        }

        public enum FilterType {
            Average, Single
        }

        public enum SortType {
            Ascending, Descending
        }

        private final FilterType filterType;
        private final SortType sortType;
        private final Gender gender;

    }


}

