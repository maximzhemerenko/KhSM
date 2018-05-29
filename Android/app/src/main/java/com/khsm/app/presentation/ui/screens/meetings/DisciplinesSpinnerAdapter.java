package com.khsm.app.presentation.ui.screens.meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;

import java.util.ArrayList;
import java.util.List;

public class DisciplinesSpinnerAdapter extends BaseAdapter {
    private List<Discipline> disciplines;
    private LayoutInflater inflater;
    private AddResultsFragment addResultsFragment;

    DisciplinesSpinnerAdapter(Context context, AddResultsFragment addResultsFragment) {
        this.disciplines = new ArrayList<>();

        this.addResultsFragment = addResultsFragment;

        this.inflater = LayoutInflater.from(context);
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return disciplines.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.disciplines_spinner_item, parent, false);
        }

        Discipline discipline = getDiscipline(position);
        ((TextView)view.findViewById(R.id.item)).setText(discipline.name);
        return view;
    }

    Discipline getDiscipline(int position) {
        return ((Discipline) getItem(position));
    }
}
