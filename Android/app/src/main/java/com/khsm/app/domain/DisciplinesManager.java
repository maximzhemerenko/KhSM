package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.Discipline;

import java.util.List;

import io.reactivex.Single;

public class DisciplinesManager {
    private final Api api;

    public DisciplinesManager(@NonNull Context context) { api = ApiFactory.createApi(context); }

    public Single<List<Discipline>> getDisciplines() { return api.getDisciplines(); }
}
