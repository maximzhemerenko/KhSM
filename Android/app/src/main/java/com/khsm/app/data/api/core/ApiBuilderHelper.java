package com.khsm.app.data.api.core;

import com.google.gson.GsonBuilder;
import com.khsm.app.data.api.converters.EnumRetrofitConverterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilderHelper {
    private Retrofit retrofit;
    private List<Interceptor> interceptors;
    private Map<Class<?>, Object> gsonTypeAdapters;

    public ApiBuilderHelper() {
        this.interceptors = new ArrayList<>();
        this.gsonTypeAdapters = new HashMap<>();
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    public ApiBuilderHelper addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ApiBuilderHelper addLoggingInterceptor(HttpLoggingInterceptor.Level level) {
        addInterceptor(new HttpLoggingInterceptor().setLevel(level));
        return this;
    }

    public <T> T createRetrofitApi(String url, Class<T> service) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(provideGsonConverterFactory())
                    .addConverterFactory(new EnumRetrofitConverterFactory())
                    .client(createOkHttpClient())
                    .build();
        }

        return retrofit.create(service);
    }

    public OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().
                connectTimeout(30, TimeUnit.SECONDS).
                readTimeout(30, TimeUnit.SECONDS);

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    private GsonConverterFactory provideGsonConverterFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        for (Map.Entry<Class<?>, Object> entry : gsonTypeAdapters.entrySet()) {
            gsonBuilder.registerTypeAdapter(entry.getKey(), entry.getValue());
        }

        return GsonConverterFactory.create(gsonBuilder.create());
    }

    public ApiBuilderHelper addGsonTypeAdapter(Class<?> type, Object typeAdapter) {
        gsonTypeAdapters.put(type, typeAdapter);
        return this;
    }
}
