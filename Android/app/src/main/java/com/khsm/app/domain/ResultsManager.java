package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.DisciplineResults;

import java.util.List;

import io.reactivex.Single;

public class ResultsManager {
    private final Api api;

    public ResultsManager(@NonNull Context context) {
        api = ApiFactory.createApi(context);
    }

    public Single<List<DisciplineResults>> getMeetingResults(int id) {
        return api.getMeetingResults(id);
    }
}
