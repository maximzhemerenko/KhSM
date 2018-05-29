package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.Result;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class MeetingsManager {
    private final Api api;

    public MeetingsManager(@NonNull Context context) {
        api = ApiFactory.createApi(context);
    }

    public Single<List<Meeting>> getMeetings() {
        return api.getMeetings();
    }

    public Single<Meeting> getLastMeeting() {
        return api.getLastMeeting();
    }

    public Single<Meeting> createMeeting(Meeting meeting) {
        return api.createMeeting(meeting);
    }

    public Single<Result> createResult(Result result) {
        return api.createResult(result);
    }

}
