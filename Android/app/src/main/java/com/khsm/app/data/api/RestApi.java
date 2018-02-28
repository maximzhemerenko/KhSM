package com.khsm.app.data.api;

import android.media.MediaCas;

import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.Session;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface RestApi {
    @GET("api/meetings")
    Single<List<Meeting>> getMeetings();

    @GET("api/meetings/last")
    Single<Meeting> getLastMeeting();

    @GET("api/disciplines")
    Single<List<Discipline>> getDisciplines();

    @GET("api/meetings/{id}/results")
    Single<List<DisciplineResults>> getMeetingResults(@Path("id") int id);

    @POST("api/users")
    Single<Session> createUser(@Body CreateSessionRequest createSessionRequest);
}