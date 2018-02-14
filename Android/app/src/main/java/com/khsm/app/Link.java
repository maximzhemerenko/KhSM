package com.khsm.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Link {
    @GET("api/users")
    Call<List<User>> getUsers();
}
