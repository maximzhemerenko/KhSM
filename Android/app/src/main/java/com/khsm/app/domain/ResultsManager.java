package com.khsm.app.domain;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Meeting;

import java.util.List;

import io.reactivex.Single;

public class ResultsManager {
    private final Api api;

    public ResultsManager() {
        api = ApiFactory.createApi();
    }

    public Single<List<DisciplineResults>> getMeetingResults(int id) {
        return api.getMeetingResults(id);
    }
}
