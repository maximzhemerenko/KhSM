package com.khsm.app.presentation.ui.screens.meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UsersSpinnerAdapter extends BaseAdapter {
    private List<User> users;
    private LayoutInflater inflater;
    private AddResultsFragment addResultsFragment;

    UsersSpinnerAdapter(Context context, AddResultsFragment addResultsFragment) {
        this.users = new ArrayList<>();

        this.addResultsFragment = addResultsFragment;

        this.inflater = LayoutInflater.from(context);
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.users_spinner_item, parent, false);
        }

        User user = getUser(position);
        ((TextView)view.findViewById(R.id.item)).setText(user.firstName + " " + user.lastName);
        return view;
    }

    User getUser(int position) {
        return ((User) getItem(position));
    }
}
