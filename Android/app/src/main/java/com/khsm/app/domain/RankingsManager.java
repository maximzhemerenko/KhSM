package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.api.entities.RankingsFilterInfo;
import com.khsm.app.data.entities.DisciplineResults;

import java.util.List;

import io.reactivex.Single;

public class RankingsManager {
    private final Api api;

    public RankingsManager(@NonNull Context context) { api = ApiFactory.createApi(context); }

    public Single<List<DisciplineResults>> getRankings(@NonNull RankingsFilterInfo rankingsFilterInfo) { return api.getRankings(rankingsFilterInfo); }
}
