package com.khsm.app.data.api;

import com.khsm.app.data.api.base.ApiBase;
import com.khsm.app.data.api.entities.User;

import java.util.List;

import io.reactivex.Single;

public class Api extends ApiBase {
    private final RestApi restApi;

    Api(RestApi restApi) {
        this.restApi = restApi;
    }

    public Single<List<User>> getUsers() {
        return restApi.getUsers()
                .compose(this::processResponse);
    }
}
