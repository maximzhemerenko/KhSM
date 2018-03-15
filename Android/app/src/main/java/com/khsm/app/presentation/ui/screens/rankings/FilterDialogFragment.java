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

import com.khsm.app.R;
import com.khsm.app.presentation.ui.screens.MainActivity;

public class FilterDialogFragment extends DialogFragment {
    public static FilterDialogFragment newInstance() {return new FilterDialogFragment();}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.filter_dialog_fragment, null);

        return view;
    }
}
