package com.khsm.app.presentation.ui.screens.meetings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.ViewHolder> {
    private SimpleDateFormat dateFormat;

    private LayoutInflater inflater;
    private List<Meeting> meetings;

    MeetingListAdapter(Context context) {
        this.meetings = new ArrayList<>();

        this.inflater = LayoutInflater.from(context);

        this.dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
        notifyDataSetChanged();
    }

    @Override
    public MeetingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meetings_list_item, parent, false);
        return new MeetingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeetingListAdapter.ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.dateMeeting.setText(dateFormat.format(meeting.date));
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView dateMeeting;
        ViewHolder(View view){
            super(view);
            dateMeeting = view.findViewById(R.id.dateMeeting);
        }
    }
}
