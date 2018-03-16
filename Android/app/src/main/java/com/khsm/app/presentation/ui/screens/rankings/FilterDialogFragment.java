package com.khsm.app.presentation.ui.screens.rankings;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.khsm.app.R;
import com.khsm.app.presentation.ui.screens.MainActivity;

public class FilterDialogFragment extends DialogFragment {
    public static FilterDialogFragment newInstance() {return new FilterDialogFragment();}

    private RadioGroup average_single_radios;
    private RadioButton average;
    private RadioButton single;

    private RadioGroup ascending_descending_radios;
    private RadioButton ascending;
    private RadioButton descending;

    private RadioGroup both_male_female_radios;
    private RadioButton both;
    private RadioButton male;
    private RadioButton female;

    private Button cancel;
    private Button apply;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.filter_dialog_fragment, null);

        average_single_radios = view.findViewById(R.id.average_single_radios);
        average = view.findViewById(R.id.average);
        single = view.findViewById(R.id.single);

        ascending_descending_radios = view.findViewById(R.id.ascending_descending_radios);
        ascending = view.findViewById(R.id.ascending);
        descending = view.findViewById(R.id.descending);

        both_male_female_radios = view.findViewById(R.id.both_male_female_radios);
        both = view.findViewById(R.id.both);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        cancel = view.findViewById(R.id.cancel);
        apply = view.findViewById(R.id.apply);

        return view;
    }

    static class FilterInfo {

    }
}
