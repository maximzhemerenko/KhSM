package com.khsm.app.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.DisciplineRecord;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;
import com.khsm.app.data.preferences.SessionStore;

import java.util.List;

import io.reactivex.Single;

public class UserManager {
    private final Api api;
    private final SessionStore sessionStore;

    public UserManager(@NonNull Context context) {
        api = ApiFactory.createApi(context);
        sessionStore = new SessionStore(context);
    }

    @Nullable
    public User getUser() {
        Session session = sessionStore.getSession();
        if (session == null)
            return null;

        return session.user;
    }

    public Single<User> updateUser(@NonNull User user) {
        return api.updateUser(user)
                .doOnSuccess(sessionStore::updateUser);
    }

    public Single<List<DisciplineResults>> getMeetingResults(int id) {
        return api.getMeetingResults(id);
    }

    public Single<List<DisciplineResults>> getNewsResults(int id) {
        return api.getNewsResults(id);
    }

    public Single<List<DisciplineResults>> getMyResults() {
        return api.getMyResults();
    }

    public Single<List<DisciplineRecord>> getMyRecords() {
        return api.getMyRecords();
    }
}
