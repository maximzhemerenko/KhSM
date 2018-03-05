package com.khsm.app.domain;

import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;
import com.khsm.app.data.entities.CreateUserRequest;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Session;

import java.util.List;

import io.reactivex.Single;

public class UsersManager {
    private final Api api;

    public UsersManager() {
        api = ApiFactory.createApi();
    }

    public Single<Session> register(CreateUserRequest createUserRequest) {
        return api.register(createUserRequest);
    }
}
