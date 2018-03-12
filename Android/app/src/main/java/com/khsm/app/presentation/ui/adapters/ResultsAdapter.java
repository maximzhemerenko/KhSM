package com.khsm.app.presentation.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Result;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    private final Context context;

    private final LayoutInflater inflater;
    private final DisplayMode displayMode;

    @Nullable
    private List<Result> results;

    public ResultsAdapter(@NonNull Context context, DisplayMode displayMode) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.displayMode = displayMode;
    }

    public void setResults(List<Result> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.results_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsAdapter.ViewHolder holder, int position) {
        if (results == null)
            throw new RuntimeException("results should not be null");

        Result result = results.get(position);

        String title;
        if (displayMode.equals(DisplayMode.UserName)) {
            title = (position + 1) + " " + result.user.firstName + " " + result.user.lastName;
        } else if (displayMode.equals(DisplayMode.Date)) {
            title = dateFormat.format(result.meeting.date);
        } else {
            throw new RuntimeException("Not supported display mode");
        }
        holder.title.setText(title);

        String results = formatTime(result.average);

        List<Float> attempts = result.attempts;
        if (attempts.size() > 0) {
            results += " (";
            for (int i = 0; i < attempts.size(); i++) {
                if (i > 0)
                    results += " ";

                results += formatTime(attempts.get(i));
            }

            int dnsCount = result.attemptCount - attempts.size();
            if (dnsCount < 0) dnsCount = 0;

            for (int i = 0; i < dnsCount; i++) {
                if (attempts.size() > 0 || i > 0) {
                    results += " ";
                }
                results += context.getString(R.string.DNS);
            }

            results += ")";
        }

        holder.results.setText(results);
    }

    private String formatTime(Float time) {
        return time != null ? String.format(Locale.ENGLISH, "%.2f", time) : context.getString(R.string.DNF);
    }

    @Override
    public int getItemCount() {
        if (results == null)
            return 0;

        return results.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView results;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            results = view.findViewById(R.id.results);
        }
    }

    public enum DisplayMode {
        UserName, Date
    }
}
