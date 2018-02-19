package com.khsm.app.data.api;

import com.khsm.app.data.entities.Meeting;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

interface RestApi {
    @GET("api/meetings")
    Single<List<Meeting>> getMeetings();
}