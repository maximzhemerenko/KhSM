package com.khsm.app.data.api;

import com.khsm.app.data.api.base.ApiBase;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.Session;

import java.util.List;

import io.reactivex.Single;

public class Api extends ApiBase {
    private final RestApi restApi;

    Api(RestApi restApi) {
        this.restApi = restApi;
    }

    public Single<List<Meeting>> getMeetings() {
        return restApi.getMeetings()
                .compose(this::processResponse);
    }

    public Single<List<Discipline>> getDisciplines() {
        return restApi.getDisciplines()
                .compose(this::processResponse);
    }

    public Single<List<DisciplineResults>> getMeetingResults(int id) {
        return restApi.getMeetingResults(id)
                .compose(this::processResponse);
    }

    public Single<Meeting> getLastMeeting() {
        return restApi.getLastMeeting()
                .compose(this::processResponse);
    }

    public Single<Session> createUser(CreateSessionRequest createSessionRequest) {
        return restApi.createUser(createSessionRequest)
                .compose(this::processResponse);
    }
}
