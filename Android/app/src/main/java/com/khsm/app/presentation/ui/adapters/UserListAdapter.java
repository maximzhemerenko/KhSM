package com.khsm.app.presentation.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.api.entities.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private final LayoutInflater inflater;

    private final List<User> users;

    public UserListAdapter(Context context, List<User> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        User user = users.get(position);

        holder.studentId.setText(String.valueOf(user.getId())); // тут нельзя сестить инт, надо стринг! он думает что инт это ресурс
        holder.studentName.setText(user.getFirstName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView studentId;
        final TextView studentName;

        ViewHolder(View itemView) {
            super(itemView);

            studentId = itemView.findViewById(R.id.id);
            studentName = itemView.findViewById(R.id.firstName);
        }
    }
}
