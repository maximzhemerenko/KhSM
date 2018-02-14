package com.khsm.app.data.api;

import com.khsm.app.data.api.entities.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

interface RestApi {
    @GET("api/users")
    Single<List<User>> getUsers();
}