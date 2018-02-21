package com.khsm.app.domain;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.Meeting;

import java.util.List;

import io.reactivex.Single;

public class MeetingsManager {
    private final Api api;

    public MeetingsManager() {
        api = ApiFactory.createApi();
    }

    public Single<List<Meeting>> getMeetings() {
        return api.getMeetings();
    }

    public Single<Meeting> getLastMeeting() {
        return api.getLastMeeting();
    }
}
