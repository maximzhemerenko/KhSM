package com.khsm.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.data.api.entities.User;

import org.w3c.dom.Text;

import java.util.List;

public class DataRecyclerAdapter extends RecyclerView.Adapter<DataRecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private List<User> users;

    DataRecyclerAdapter(Context context, List<User> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public DataRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataRecyclerAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.studentId.setText(String.valueOf(user.getId())); // тут нельзя сестить инт, надо стринг! он думает что инт это ресурс
        holder.studentName.setText(user.getFirstName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView studentId;
        final TextView studentName;

        public ViewHolder(View itemView) {
            super(itemView);

            studentId = itemView.findViewById(R.id.id);
            studentName = itemView.findViewById(R.id.firstName);
        }
    }
}
