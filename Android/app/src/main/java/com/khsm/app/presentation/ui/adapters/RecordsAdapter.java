package com.khsm.app.presentation.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.DisciplineRecord;
import com.khsm.app.data.entities.Result;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    private final Context context;
    private final LayoutInflater inflater;

    private List<DisciplineRecord> disciplineRecords;

    public RecordsAdapter(@NonNull Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setResults(List<DisciplineRecord> disciplineRecords) {
        this.disciplineRecords = disciplineRecords;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecordsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.records_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsAdapter.ViewHolder holder, int position) {
        DisciplineRecord disciplineRecord = disciplineRecords.get(position);

        holder.discipline_textView.setText(disciplineRecord.discipline.name);
        holder.bestSingleResult_textView.setText(formatResult(disciplineRecord.bestSingleResult));
        holder.bestAverageResult_textView.setText(formatResult(disciplineRecord.bestAverageResult));
    }

    private String formatResult(Result result) {
        return String.format("%s (%s)",
                formatTime(result.average),
                dateFormat.format(result.meeting.date)
        );
    }

    private String formatTime(Float time) {
        return time != null ? String.format(Locale.ENGLISH, "%.2f", time) : context.getString(R.string.DNF);
    }

    @Override
    public int getItemCount() {
        return disciplineRecords != null ? disciplineRecords.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView discipline_textView;
        final TextView bestSingleResult_textView;
        final TextView bestAverageResult_textView;

        ViewHolder(View view) {
            super(view);
            discipline_textView = view.findViewById(R.id.discipline_textView);
            bestSingleResult_textView = view.findViewById(R.id.bestResult_textView);
            bestAverageResult_textView = view.findViewById(R.id.averageResult_textView);
        }
    }
}
