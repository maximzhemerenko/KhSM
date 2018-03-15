package com.khsm.app.presentation.ui.screens.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.khsm.app.R;
import com.khsm.app.data.entities.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());

    private final Context context;
    private LayoutInflater inflater;
    private List<News> news;

    NewsAdapter(Context context) {
        this.context = context;
        this.news = new ArrayList<>();

        this.inflater = LayoutInflater.from(context);
    }

    public void setNews(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.newsText.setMovementMethod(LinkMovementMethod.getInstance());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        News news = this.news.get(position);
        holder.newsDate.setText(dateFormat.format(news.dateAndTime));
        holder.newsText.setText(prepareNewsText(news.text));
        holder.newsAuthor.setText(news.user.firstName + " " + news.user.lastName);
        holder.news = news;
    }

    private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                Toast.makeText(context, span.getURL(), Toast.LENGTH_LONG).show();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private CharSequence prepareNewsText(String source) {
        SpannableStringBuilder text = new SpannableStringBuilder(Html.fromHtml(source));
        URLSpan[] spans = text.getSpans(0, text.length(), URLSpan.class);
        for (URLSpan span : spans) {
            makeLinkClickable(text, span);
        }
        return text;
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
