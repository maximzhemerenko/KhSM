package com.khsm.app.presentation.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;
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

        ViewHolder viewHolder = new ViewHolder(view);

        SpannableString bestSingleResultDetailsString = new SpannableString(context.getString(R.string.details));
        bestSingleResultDetailsString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (viewHolder.disciplineRecord != null)
                    bestSingleResultDetailsClick(viewHolder.disciplineRecord);
            }
        }, 0, bestSingleResultDetailsString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        SpannableString bestAverageResultDetailsString = new SpannableString(context.getString(R.string.details));
        bestAverageResultDetailsString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (viewHolder.disciplineRecord != null)
                    bestAverageResultDetailsClick(viewHolder.disciplineRecord);
            }
        }, 0, bestAverageResultDetailsString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        viewHolder.bestSingleResultDetails_textView.setText(bestSingleResultDetailsString);
        viewHolder.bestSingleResultDetails_textView.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.bestAverageResultDetails_textView.setText(bestAverageResultDetailsString);
        viewHolder.bestAverageResultDetails_textView.setMovementMethod(LinkMovementMethod.getInstance());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsAdapter.ViewHolder holder, int position) {
        DisciplineRecord disciplineRecord = disciplineRecords.get(position);

        holder.discipline_textView.setText(disciplineRecord.discipline.name);
        holder.bestSingleResult_textView.setText(formatResult(disciplineRecord.bestSingleResult));
        holder.bestAverageResult_textView.setText(formatResult(disciplineRecord.bestAverageResult));

        holder.disciplineRecord = disciplineRecord;
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

    private void bestSingleResultDetailsClick(@NonNull DisciplineRecord disciplineRecord) {
        showResultDetails(disciplineRecord.discipline, disciplineRecord.bestSingleResult, context.getString(R.string.Best));
    }

    private void bestAverageResultDetailsClick(@NonNull DisciplineRecord disciplineRecord) {
        showResultDetails(disciplineRecord.discipline, disciplineRecord.bestAverageResult, context.getString(R.string.Average));
    }

    private void showResultDetails(Discipline discipline, Result result, String recordType) {
        String title = discipline.name + " " + recordType + " (" + dateFormat.format(result.meeting.date) + ")";

        SpannableStringBuilder message = new SpannableStringBuilder(formatResultTime(result.average));

        List<Float> attempts = result.attempts;
        if (attempts.size() > 0) {
            Float min = null;

            for (int i = 0; i < attempts.size(); i++) {
                @Nullable Float time = attempts.get(i);

                if (min == null) {
                    min = time;
                    continue;
                }

                if (time != null && time < min)
                    min = time;
            }

            message.append(" (");
            for (int i = 0; i < attempts.size(); i++) {
                if (i > 0)
                    message.append(" ");

                Float time = attempts.get(i);
                SpannableString spannableString = new SpannableString(formatResultTime(time));
                if (time != null && min != null && time.equals(min))
                    spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                message.append(spannableString);
            }

            int dnsCount = result.attemptCount - attempts.size();
            if (dnsCount < 0) dnsCount = 0;

            for (int i = 0; i < dnsCount; i++) {
                if (attempts.size() > 0 || i > 0) {
                    message.append(" ");
                }
                message.append(context.getString(R.string.DNS));
            }

            message.append(")");
        }

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.OK, null)
                .show();
    }

    private String formatResultTime(Float time) {
        return time != null ? String.format(Locale.ENGLISH, "%.2f", time) : context.getString(R.string.DNF);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView discipline_textView;
        final TextView bestSingleResult_textView;
        final TextView bestAverageResult_textView;
        final TextView bestSingleResultDetails_textView;
        final TextView bestAverageResultDetails_textView;

        @Nullable
        DisciplineRecord disciplineRecord;

        ViewHolder(View view) {
            super(view);
            discipline_textView = view.findViewById(R.id.discipline_textView);
            bestSingleResult_textView = view.findViewById(R.id.bestSingleResult_textView);
            bestAverageResult_textView = view.findViewById(R.id.bestAverageResult_textView);
            bestSingleResultDetails_textView = view.findViewById(R.id.bestSingleResultDetails_textView);
            bestAverageResultDetails_textView = view.findViewById(R.id.bestAverageResultDetails_textView);
        }
    }
}
