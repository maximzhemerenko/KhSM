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

import io.reactivex.annotations.Nullable;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.ViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    private MeetingListFragment meetingListFragment;

    private LayoutInflater inflater;
    private List<Meeting> meetings;

    MeetingListAdapter(Context context, MeetingListFragment meetingListFragment) {
        this.meetings = new ArrayList<>();

        this.meetingListFragment = meetingListFragment;

        this.inflater = LayoutInflater.from(context);
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
        notifyDataSetChanged();
    }

    @Override
    public MeetingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meetings_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meeting meeting = viewHolder.meeting;
                if (meeting == null)
                    return;
                meetingListFragment.onItemClicked(meeting);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetingListAdapter.ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.meetingDate.setText(dateFormat.format(meeting.date));
        holder.meeting = meeting;
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView meetingDate;
        @Nullable Meeting meeting;
        ViewHolder(View view){
            super(view);
            meetingDate = view.findViewById(R.id.meetingDate);
        }
    }
}
