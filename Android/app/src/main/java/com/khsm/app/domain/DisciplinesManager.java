package com.khsm.app.domain;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.Discipline;

import java.util.List;

import io.reactivex.Single;

public class DisciplinesManager {
    private final Api api;

    public DisciplinesManager() { api = ApiFactory.createApi(); }

    public Single<List<Discipline>> getDisciplines() { return api.getDisciplines(); }
}
