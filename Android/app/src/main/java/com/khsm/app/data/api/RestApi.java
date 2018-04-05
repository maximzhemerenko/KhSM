package com.khsm.app.data.api;

import com.khsm.app.data.api.entities.RankingsFilterInfo;
import com.khsm.app.data.entities.CreateSessionRequest;
import com.khsm.app.data.entities.CreateUserRequest;
import com.khsm.app.data.entities.Discipline;
import com.khsm.app.data.entities.DisciplineRecord;
import com.khsm.app.data.entities.DisciplineResults;
import com.khsm.app.data.entities.Gender;
import com.khsm.app.data.entities.Meeting;
import com.khsm.app.data.entities.News;
import com.khsm.app.data.entities.Session;
import com.khsm.app.data.entities.User;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface RestApi {
    @GET("meetings")
    Single<List<Meeting>> getMeetings();

    @GET("meetings/last")
    Single<Meeting> getLastMeeting();

    @GET("disciplines")
    Single<List<Discipline>> getDisciplines();

    @GET("meetings/{id}/results")
    Single<List<DisciplineResults>> getMeetingResults(@Path("id") int id);

    @POST("sessions")
    Single<Session> login(@Body CreateSessionRequest createSessionRequest);

    @POST("users")
    Single<Session> register(@Body CreateUserRequest createUserRequest);

    @POST("meetings")
    Single<Meeting> createMeeting(@Body Meeting meeting);

    @PUT("users/me")
    Single<User> updateUser(@Body User user);

    @GET("users/me/results")
    Single<List<DisciplineResults>> getMyResults();

    @GET("users/me/records")
    Single<List<DisciplineRecord>> getMyRecords();

    @GET("rankings")
    Single<List<DisciplineResults>> getRankings(@Query("type") RankingsFilterInfo.FilterType type, @Query("sort") RankingsFilterInfo.SortType sort, @Query("gender") Gender gender);

    @GET("news")
    Single<List<News>> getNews();

    @POST("news")
    Single<News> addNews(@Body News news);
}