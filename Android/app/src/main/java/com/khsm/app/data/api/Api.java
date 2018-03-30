package com.khsm.app.data.api;

import android.support.annotation.NonNull;

import com.khsm.app.data.api.base.ApiBase;
import com.khsm.app.data.api.entities.RankingsFilterInfo;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.CreateUserRequest;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.DisciplineRecord;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.News;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class Api extends ApiBase {
    private final RestApi restApi;

    Api(RestApi restApi, AuthInterceptor authInterceptor) {
        super(authInterceptor);
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

    public Single<Session> login(CreateSessionRequest createSessionRequest) {
        return restApi.login(createSessionRequest)
                .compose(this::processResponse);
    }

    public Single<Session> register(CreateUserRequest createUserRequest) {
        return restApi.register(createUserRequest)
                .compose(this::processResponse);
    }

    public Single<Meeting> createMeeting(Meeting meeting){
        return restApi.createMeeting(meeting)
                .compose(this::processResponse);
    }

    public Single<User> updateUser(User user){
        return restApi.updateUser(user)
                .compose(this::processResponse);
    }

    public Single<List<DisciplineResults>> getMyResults() {
        return restApi.getMyResults()
                .compose(this::processResponse);
    }

    public Single<List<DisciplineRecord>> getMyRecords() {
        return restApi.getMyRecords()
                .compose(this::processResponse);
    }

    public Single<List<DisciplineResults>> getRankings(@NonNull RankingsFilterInfo rankingsFilterInfo) {
        return restApi.getRankings(rankingsFilterInfo.filterType, rankingsFilterInfo.sortType, rankingsFilterInfo.gender)
                .compose(this::processResponse);
    }

    public Single<List<News>> getNews() {
        return restApi.getNews()
                .compose(this::processResponse);
    }
}
