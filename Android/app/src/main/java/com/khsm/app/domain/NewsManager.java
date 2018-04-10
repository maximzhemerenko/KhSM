package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.News;

import java.util.List;

import io.reactivex.Single;

public class NewsManager {
    private final Api api;

    public NewsManager(@NonNull Context context) { api = ApiFactory.createApi(context); }

    public Single<List<News>> getNews() { return api.getNews(); }

    public Single<News> getLastNews() {
        return api.getLastNews();
    }

    public Single<News> addNews(News news) {
        return api.addNews(news);
    }
}
