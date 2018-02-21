package com.khsm.app.presentation.ui.screens.disciplines;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.Meeting;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DisciplineDetailsFragment extends Fragment {
    private static final String KET_DISCIPLINE = "KET_DISCIPLINE";

    public static DisciplineDetailsFragment newInstance(Discipline discipline) {
        DisciplineDetailsFragment fragment = new DisciplineDetailsFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(KET_DISCIPLINE, discipline);

        fragment.setArguments(arguments);

        return fragment;
    }

    private Discipline discipline;

    private Toolbar toolbar;
    private TextView disciplineDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        discipline = (Discipline) arguments.getSerializable(KET_DISCIPLINE);
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discipline_details_fragment, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(discipline.name);

        disciplineDetails = view.findViewById(R.id.disciplineDetails);
        disciplineDetails.setText(discipline.description);
        disciplineDetails.setMovementMethod(new ScrollingMovementMethod());

        return view;
    }
}
