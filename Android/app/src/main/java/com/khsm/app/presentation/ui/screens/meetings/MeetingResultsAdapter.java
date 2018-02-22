package com.khsm.app.presentation.ui.screens.meetings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.Nullable;

public class MeetingResultsAdapter extends RecyclerView.Adapter<MeetingResultsAdapter.ViewHolder> {
    private MeetingResultsFragment meetingResultsFragment;

    private LayoutInflater inflater;
    private DisciplineResults disciplineResults;

    MeetingResultsAdapter(Context context, MeetingResultsFragment meetingResultsFragment) {
        this.meetingResultsFragment = meetingResultsFragment;

        this.inflater = LayoutInflater.from(context);
    }

    public void setResults(DisciplineResults disciplineResults) {
        this.disciplineResults = disciplineResults;
        notifyDataSetChanged();
    }

    @Override
    public MeetingResultsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.discipline_details_result_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetingResultsAdapter.ViewHolder holder, int position) {;
        Result result = disciplineResults.results.get(position);
        holder.userName.setText((position + 1) + " " + result.user.firstName + " " + result.user.lastName);

        String results = formatTime(result.average);

        List<Float> attempts = result.attempts;
        if (attempts.size() > 0) {
            results += " (";
            for (int i = 0; i < attempts.size(); i++) {
                if (i > 0)
                    results += " ";

                results += formatTime(attempts.get(i));
            }

            int dnsCount = disciplineResults.discipline.attemptsCount - attempts.size();
            if (dnsCount < 0) dnsCount = 0;

            for (int i = 0; i < dnsCount; i++) {
                if (attempts.size() > 0 || i > 0) {
                    results += " ";
                }
                results += "DNS";
            }

            results += ")";
        }

        holder.results.setText(results);
    }

    private String formatTime(Float time)
    {
        return time != null ? String.format("%.2f", time) : "DNF";
    }

    @Override
    public int getItemCount() {
        return disciplineResults != null ? disciplineResults.results.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView userName;
        final TextView results;
        ViewHolder(View view){
            super(view);
            userName = view.findViewById(R.id.userName);
            results = view.findViewById(R.id.results);
        }
    }
}
