package com.khsm.app.presentation.ui.screens.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khsm.app.R;
import com.khsm.app.data.entities.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());

    private NewsFragment newsFragment;

    private LayoutInflater inflater;
    private List<News> news;

    NewsAdapter(Context context, NewsFragment newsFragment) {
        this.news = new ArrayList<>();

        this.newsFragment = newsFragment;

        this.inflater = LayoutInflater.from(context);
    }

    public void setNews(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        News news = this.news.get(position);
        holder.newsDate.setText(dateFormat.format(news.dateAndTime));
        holder.newsText.setText(news.text);
        holder.newsAuthor.setText(news.user.firstName + news.user.lastName);
        holder.news = news;
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView newsText;
        final TextView newsDate;
        final TextView newsAuthor;

        @Nullable
        News news;
        ViewHolder(View view){
            super(view);
            newsText = view.findViewById(R.id.newsText);
            newsDate = view.findViewById(R.id.newsDate);
            newsAuthor = view.findViewById(R.id.newsAuthor);
        }
    }
}
