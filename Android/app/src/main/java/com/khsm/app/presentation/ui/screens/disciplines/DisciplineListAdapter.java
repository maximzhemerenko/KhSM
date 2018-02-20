package com.khsm.app.presentation.ui.screens.disciplines;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;

import java.util.ArrayList;
import java.util.List;

public class DisciplineListAdapter extends RecyclerView.Adapter<DisciplineListAdapter.ViewHolder> {
    private DisciplineListFragment disciplineListFragment;

    private LayoutInflater inflater;
    private List<Discipline> disciplines;

    DisciplineListAdapter(Context context, DisciplineListFragment disciplineListFragment) {
        this.disciplines = new ArrayList<>();

        this.disciplineListFragment = disciplineListFragment;

        this.inflater = LayoutInflater.from(context);
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
        notifyDataSetChanged();
    }

    @Override
    public DisciplineListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.disciplines_list_item, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disciplineListFragment.onItemClicked();
                Toast.makeText(v.getContext(), "jkjk", Toast.LENGTH_SHORT).show();
            }
        });

        return new DisciplineListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DisciplineListAdapter.ViewHolder holder, int position) {
        Discipline discipline = disciplines.get(position);
        holder.disciplineName.setText(discipline.name);
    }

    @Override
    public int getItemCount() {
        return disciplines.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView disciplineName;
        ViewHolder(View view){
            super(view);
            disciplineName = view.findViewById(R.id.disciplineName);
        }
    }
}
