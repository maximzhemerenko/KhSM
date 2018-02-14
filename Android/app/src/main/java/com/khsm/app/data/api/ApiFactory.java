package com.khsm.app.data.api;

import com.khsm.app.BuildConfig;
import com.khsm.app.data.api.core.ApiBuilderHelper;

import okhttp3.logging.HttpLoggingInterceptor;

public abstract class ApiFactory {
    public static Api createApi() {
        ApiBuilderHelper apiBuilderHelper = new ApiBuilderHelper();

        if (BuildConfig.DEBUG) {
            apiBuilderHelper.addLoggingInterceptor(HttpLoggingInterceptor.Level.BODY);
        }

        String serviceUrl = "http://10.0.2.2:5000/";

        RestApi restApi = apiBuilderHelper.createRetrofitApi(serviceUrl, RestApi.class);

        return new Api(restApi);
    }
}
