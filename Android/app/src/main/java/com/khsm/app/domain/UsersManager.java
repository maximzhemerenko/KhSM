package com.khsm.app.domain;

import com.khsm.app.data.api.entities.User;
import com.khsm.app.data.api.Api;
import com.khsm.app.data.api.ApiFactory;

import java.util.List;

import io.reactivex.Single;

public class UsersManager {
    private final Api api;

    public UsersManager() {
        api = ApiFactory.createApi();
    }

    public Single<List<User>> getUsers() {
        return api.getUsers();
    }
}
