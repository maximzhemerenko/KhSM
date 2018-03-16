package com.khsm.app.presentation.ui.screens.rankings;

import android.os.Bundle;
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
                RankingsFilterInfo.FilterType filterType = average.isChecked() ? RankingsFilterInfo.FilterType.Average :
                        single.isChecked() ? RankingsFilterInfo.FilterType.Single : null;
                RankingsFilterInfo.SortType sortType = ascending.isChecked() ? RankingsFilterInfo.SortType.Ascending :
                        descending.isChecked() ? RankingsFilterInfo.SortType.Descending : null;

                RankingsFilterInfo rankingsFilterInfo = new RankingsFilterInfo(filterType, sortType, gender);

                RankingsFragment parentFragment = (RankingsFragment) getParentFragment();
                parentFragment.applyFilter(rankingsFilterInfo);
            }
        };

        cancel.setOnClickListener(cancelClicked);
        apply.setOnClickListener(applyClicked);

        return view;
    }
}

